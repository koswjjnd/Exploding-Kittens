package explodingkittens.integration;

import explodingkittens.controller.GameContext;
import explodingkittens.model.*;
import explodingkittens.controller.GameController;
import explodingkittens.controller.GameSetupController;
import explodingkittens.view.GameView;
import explodingkittens.view.GameSetupView;
import explodingkittens.service.TurnService;
import explodingkittens.service.CardEffectService;
import explodingkittens.model.PlayerService;
import explodingkittens.service.DealService;
import explodingkittens.exceptions.InvalidPlayerCountException;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import java.util.*;

public class GameLoopSteps {
    private GameController controller;
    private GameSetupController setupController;
    private GameView view;
    private GameSetupView setupView;
    private TurnService turnService;
    private CardEffectService cardEffectService;
    private PlayerService playerService;
    private DealService dealService;
    private boolean gameInitialized = false;
    private List<Card> deckCards;
    private List<Boolean> drawCardResponses;
    private int currentDrawResponseIndex;
    private Exception thrownException;

    public GameLoopSteps() {
        view = Mockito.mock(GameView.class);
        setupView = Mockito.mock(GameSetupView.class);
        controller = new GameController(view);
        playerService = new PlayerService();
        dealService = new DealService();
        setupController = new GameSetupController(setupView, playerService, dealService);
        cardEffectService = new CardEffectService(view);
        turnService = new TurnService(view, cardEffectService);
        deckCards = new ArrayList<>();
        drawCardResponses = new ArrayList<>();
        currentDrawResponseIndex = 0;
        
        // 默认模拟玩家行为
        Mockito.when(view.confirmDefuse(Mockito.any())).thenReturn(true);
        Mockito.when(view.selectExplodingKittenPosition(Mockito.anyInt())).thenReturn(0);
    }

    /**
     * Initializes the game system before each test.
     */
    @Given("^the game loop system is ready$")
    public void gameSystemIsReady() {
        setupView = Mockito.mock(GameSetupView.class);
        view = Mockito.mock(GameView.class);
        playerService = new PlayerService();
        dealService = new DealService();
        cardEffectService = new CardEffectService(view);
        setupController = new GameSetupController(setupView, playerService, dealService);
        turnService = new TurnService(view, cardEffectService);
        GameContext.reset();
        gameInitialized = false;
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        GameContext.reset();
        gameInitialized = false;
    }

    /**
     * Initializes the game with players having specific hands for game loop testing.
     * @param dataTable The table containing player and hand information
     */
    @When("^the game loop is initialized with players having hands:$")
    public void gameIsInitializedWithPlayersHavingHands(DataTable dataTable) {
        if (!gameInitialized) {
            try {
                List<Map<String, String>> players = dataTable.asMaps();
                Mockito.when(setupView.promptPlayerCount()).thenReturn(players.size());
                
                // Set player names
                for (int i = 0; i < players.size(); i++) {
                    Mockito.when(setupView.promptNickname(i + 1))
                        .thenReturn(players.get(i).get("Player"));
                }
                
                setupController.setupGame();
                
                // Get player list from game context
                List<Player> playerList = GameContext.getTurnOrder();
                
                // Clear each player's hand and set leftTurns to 0
                for (Player player : playerList) {
                    List<Card> hand = player.getHand();
                    for (Card card : hand) {
                        player.removeCard(card);
                    }
                    player.setLeftTurns(0);  // 初始化为0，这样出牌后不会自动抽牌
                }
                
                // Add specified cards to each player
                for (int i = 0; i < players.size(); i++) {
                    String[] cards = players.get(i).get("Hand").split(", ");
                    for (String cardType : cards) {
                        Card card = createCardFromType(cardType.trim());
                        if (card != null) {
                            playerList.get(i).receiveCard(card);
                        }
                    }
                }
                
                gameInitialized = true;
            }
            catch (InvalidPlayerCountException e) {
                thrownException = e;
            }
        }
    }

    @Given("^the deck is stacked with cards:$")
    public void deckIsStackedWithCards(DataTable dataTable) {
        List<Map<String, String>> cards = dataTable.asMaps();
        deckCards.clear();
        
        for (Map<String, String> card : cards) {
            Card newCard = createCardFromType(card.get("Card"));
            if (newCard != null) {
                deckCards.add(newCard);
            }
        }
        
        // Set up deck with specified cards
        Deck deck = GameContext.getGameDeck();
        if (deck == null) {
            throw new IllegalStateException("Game deck is not initialized. Make sure to initialize the game first.");
        }
        
        // 保存原有的牌堆状态
        List<Card> originalCards = new ArrayList<>(deck.getCards());
        
        // 清空牌堆并添加指定的牌
        deck.clear();
        for (Card card : deckCards) {
            deck.addCard(card);
        }
        
        // 打印牌堆内容以验证
        System.out.println("\nDeck contents after stacking:");
        for (Card card : deck.getCards()) {
            System.out.println("- " + card.getClass().getSimpleName());
        }
        System.out.println();
    }

    @When("^player \"([^\"]*)\" plays \"([^\"]*)\"$")
    public void playerPlaysCard(String playerName, String cardType) {
        Player player = findPlayerByName(playerName);
        Card card = findCardInHand(player, cardType);
        if (card != null) {
            // 模拟玩家选择出牌
            Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("play");
            Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList())).thenReturn(card);
            turnService.takeTurn(player);
        }
    }

    @When("^player \"([^\"]*)\" chooses to draw a card$")
    public void playerChoosesToDrawCard(String playerName) {
        Player player = findPlayerByName(playerName);
        // 设置leftTurns为1，这样玩家可以选择抽牌
        player.setLeftTurns(1);
        // 模拟玩家选择抽牌
        Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList())).thenReturn(null);
        Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("draw");
        turnService.takeTurn(player);
    }

    @Then("^player \"([^\"]*)\" should have \"([^\"]*)\" in hand$")
    public void playerShouldHaveCardInHand(String playerName, String cardType) {
        Player player = findPlayerByName(playerName);
        boolean hasCard = player.getHand().stream()
            .anyMatch(card -> card.getClass().getSimpleName().equals(cardType + "Card"));
        assert hasCard : "Player " + playerName + " should have " + cardType + " in hand";
    }

    @Then("^player \"([^\"]*)\" should have no \"([^\"]*)\" cards$")
    public void playerShouldHaveNoCards(String playerName, String cardType) {
        Player player = findPlayerByName(playerName);
        boolean hasCard = player.getHand().stream()
            .anyMatch(card -> card.getClass().getSimpleName().equals(cardType + "Card"));
        assert !hasCard : "Player " + playerName + " should not have any " + cardType + " cards";
    }

    @Then("^the turn should end$")
    public void turnShouldEnd() {
        // 验证当前玩家是否已经改变
        Player currentPlayer = GameContext.getCurrentPlayer();
        assert currentPlayer.getName().equals("P2") : "Turn should have ended and current player should be P2";
    }

    @Then("^the next player should be \"([^\"]*)\"$")
    public void nextPlayerInGameLoopShouldBe(String playerName) {
        Player nextPlayer = GameContext.getCurrentPlayer();
        assert nextPlayer.getName().equals(playerName) : 
            "Expected next player to be " + playerName + " but was " + nextPlayer.getName();
    }

    @Then("^player \"([^\"]*)\" should be alive$")
    public void playerShouldBeAlive(String playerName) {
        Player player = findPlayerByName(playerName);
        assert player.isAlive() : "Player " + playerName + " should be alive";
    }

    @Then("^the deck should contain \"([^\"]*)\"$")
    public void deckShouldContainCard(String cardType) {
        Deck deck = GameContext.getGameDeck();
        boolean containsCard = deck.getCards().stream()
            .anyMatch(card -> card.getClass().getSimpleName().equals(cardType + "Card"));
        assert containsCard : "Deck should contain " + cardType;
    }

    @Then("^player \"([^\"]*)\" should be eliminated$")
    public void playerShouldBeEliminated(String playerName) {
        Player player = findPlayerByName(playerName);
        assert !player.isAlive() : "Player " + playerName + " should be eliminated";
    }

    @Then("^player \"([^\"]*)\" should not be in turn order$")
    public void playerShouldNotBeInTurnOrder(String playerName) {
        List<Player> turnOrder = GameContext.getTurnOrder();
        boolean playerInTurnOrder = turnOrder.stream()
            .anyMatch(p -> p.getName().equals(playerName) && p.isAlive());
        assert !playerInTurnOrder : "Player " + playerName + " should not be in turn order";
    }

    private Card createCardFromType(String cardType) {
        switch (cardType.toUpperCase()) {
            case "ATTACK":
                return new AttackCard();
            case "SKIP":
                return new SkipCard();
            case "FAVOR":
                return new FavorCard();
            case "CATCARD":
                return new CatCard(CatType.TACOCAT);
            case "DEFUSE":
                return new DefuseCard();
            case "NOPE":
                return new NopeCard();
            case "SHUFFLE":
                return new ShuffleCard();
            case "SEE_THE_FUTURE":
                return new SeeTheFutureCard();
            case "EXPLODINGKITTEN":
                return new ExplodingKittenCard();
            default:
                return null;
        }
    }

    private Player findPlayerByName(String name) {
        return GameContext.getTurnOrder().stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Player not found: " + name));
    }

    private Card findCardInHand(Player player, String cardType) {
        return player.getHand().stream()
            .filter(card -> card.getClass().getSimpleName().equals(cardType + "Card"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Card not found in hand: " + cardType));
    }
} 