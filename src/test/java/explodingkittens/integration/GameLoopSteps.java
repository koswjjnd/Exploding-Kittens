package explodingkittens.integration;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.model.*;
import explodingkittens.controller.GameController;
import explodingkittens.controller.GameSetupController;
import explodingkittens.view.FavorCardView;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                    player.setLeftTurns(0);
                }
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
        Deck deck = GameContext.getGameDeck();
        if (deck == null) {
            throw new IllegalStateException("Game deck is not initialized. Make sure to initialize the game first.");
        }
        List<Card> originalCards = new ArrayList<>(deck.getCards());
        deck.clear();
        for (Card card : deckCards) {
            deck.addCard(card);
        }
        System.out.println("\nDeck contents after stacking:");
        for (Card card : deck.getCards()) {
            System.out.println("- " + card.getClass().getSimpleName());
        }
        System.out.println();
    }

    @When("^player \"([^\"]*)\" plays \"([^\"]*)\"$")
    public void playerPlaysCard(String playerName, String cardType) {
        Player player = findPlayerByName(playerName);
        String handInfo = player.getHand().stream()
            .map(card -> card.getClass().getSimpleName())
            .collect(Collectors.joining(", "));
        System.out.println("Player " + playerName + " current hand: [" + handInfo + "]");
        
        Card card = findCardInHand(player, cardType);
        if (card != null) {
            Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("play");
            Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList())).thenReturn(card);
            if (cardType.equals("Favor")) {
                Mockito.when(view.checkForNope(Mockito.any(), Mockito.eq(card))).thenReturn(true);
                Mockito.when(view.promptPlayNope(Mockito.any(), Mockito.any())).thenReturn(false);
                FavorCard favorCard = (FavorCard) card;
                favorCard.getView().setUserInput("0");
            }
            if (cardType.equals("Skip")) {
                Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("end");
            }
            try {
                turnService.playCard(player, card);
            } catch (InvalidCardException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @When("^player \"([^\"]*)\" counters with \"([^\"]*)\"$")
    public void playerCountersWithCard(String playerName, String cardType) {
        if (cardType.equals("Nope")) {
            Player player = findPlayerByName(playerName);
            Card card = findCardInHand(player, cardType);
            if (card != null) {
                Mockito.when(view.promptPlayNope(Mockito.eq(player), Mockito.any())).thenReturn(true);
                Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList())).thenReturn(card);
                try {
                    turnService.playCard(player, card);
                } catch (InvalidCardException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @When("^player \"([^\"]*)\" chooses to draw a card$")
    public void playerChoosesToDrawCard(String playerName) {
        Player player = findPlayerByName(playerName);
        player.setLeftTurns(1);
        Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList())).thenReturn(null);
        Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("draw");
        turnService.takeTurn(player);
    }

    @When("^player \"([^\"]*)\" chooses not to use defuse$")
    public void playerChoosesNotToUseDefuse(String playerName) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.confirmDefuse(Mockito.eq(player))).thenReturn(false);
        Mockito.doNothing().when(view).displayPlayerEliminated(Mockito.eq(player));
    }

    @When("^player \"([^\"]*)\" chooses to use defuse$")
    public void playerChoosesToUseDefuse(String playerName) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.confirmDefuse(Mockito.eq(player))).thenReturn(true);
    }

    @When("^player \"([^\"]*)\" places the exploding kitten at position \"([^\"]*)\"$")
    public void playerPlacesExplodingKitten(String playerName, String position) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.selectExplodingKittenPosition(Mockito.anyInt())).thenReturn(Integer.parseInt(position));
    }

    @When("^player \"([^\"]*)\" has no defuse card$")
    public void playerHasNoDefuseCard(String playerName) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.confirmDefuse(Mockito.eq(player))).thenReturn(false);
        Mockito.doNothing().when(view).displayPlayerEliminated(Mockito.eq(player));
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

    @Then("^player \"([^\"]*)\" should have (\\d+) turns left$")
    public void playerShouldHaveTurnsLeft(String playerName, int expectedTurns) {
        Player player = findPlayerByName(playerName);
        assertEquals(expectedTurns, player.getLeftTurns(), 
            "Player " + playerName + " should have " + expectedTurns + " turns left");
    }

    @Then("^player \"([^\"]*)\" should be at the end of turn order$")
    public void playerShouldBeAtEndOfTurnOrder(String playerName) {
        Player player = findPlayerByName(playerName);
        List<Player> turnOrder = GameContext.getTurnOrder();
        assertEquals(player, turnOrder.get(turnOrder.size() - 1), 
            "Player " + playerName + " should be at the end of turn order");
    }

    @When("^player \"([^\"]*)\" gives a card to player \"([^\"]*)\"$")
    public void playerGivesCardToPlayer(String fromPlayerName, String toPlayerName) {
        Player fromPlayer = findPlayerByName(fromPlayerName);
        Player toPlayer = findPlayerByName(toPlayerName);
        if (fromPlayer.getHand().isEmpty()) {
            throw new IllegalStateException("Player " + fromPlayerName + " has no cards to give");
        }
        Card cardToGive = fromPlayer.getHand().get(0);
        fromPlayer.getHand().remove(cardToGive);
        toPlayer.receiveCard(cardToGive);
    }

    @Then("^player \"([^\"]*)\" should have (\\d+) card in hand$")
    public void playerShouldHaveCardsInHand(String playerName, int expectedCount) {
        Player player = findPlayerByName(playerName);
        assert player.getHand().size() == expectedCount : 
            "Player " + playerName + " should have " + expectedCount + " cards, but has " + player.getHand().size();
    }

    @Then("^player \"([^\"]*)\" should give a card to player \"([^\"]*)\"$")
    public void playerShouldGiveCardToPlayer(String fromPlayerName, String toPlayerName) {
        Player fromPlayer = findPlayerByName(fromPlayerName);
        Player toPlayer = findPlayerByName(toPlayerName);
        if (fromPlayer.getHand().isEmpty()) {
            throw new IllegalStateException("Player " + fromPlayerName + " has no cards to give");
        }
        Card cardToGive = fromPlayer.getHand().stream()
            .filter(card -> card instanceof CatCard)
            .findFirst()
            .orElse(fromPlayer.getHand().get(0));
        fromPlayer.getHand().remove(cardToGive);
        toPlayer.receiveCard(cardToGive);
    }

    @Given("^the deck is empty$")
    public void theDeckIsEmpty() {
        GameContext.getGameDeck().clear();
    }

    @Then("^the game should be over$")
    public void theGameShouldBeOver() {
        assert GameContext.isGameOver() : "Game should be over";
    }

    @Then("^player \"([^\"]*)\" should be the winner$")
    public void playerShouldBeTheWinner(String playerName) {
        Player player = findPlayerByName(playerName);
        assert player.isAlive() : "Player " + playerName + " should be alive";
        assert GameContext.getTurnOrder().size() == 1 : "Should only be one player left";
        assert GameContext.getTurnOrder().get(0) == player : "Player " + playerName + " should be the last player";
    }

    @When("^player \"([^\"]*)\" is eliminated$")
    public void playerIsEliminated(String playerName) {
        Player player = findPlayerByName(playerName);
        player.setAlive(false);
        GameContext.removePlayer(player);
    }

    @When("^the game is marked as over$")
    public void theGameIsMarkedAsOver() {
        GameContext.setGameOver(true);
    }

    @Then("^the game turn order should be \"([^\"]*)\"$")
    public void turnOrderShouldBe(String expectedOrder) {
        List<Player> turnOrder = GameContext.getTurnOrder();
        String[] expectedPlayers = expectedOrder.split(", ");
        
        assertEquals(expectedPlayers.length, turnOrder.size(), 
            "Expected " + expectedPlayers.length + " players in turn order, but got " + turnOrder.size());
            
        for (int i = 0; i < expectedPlayers.length; i++) {
            assertEquals(expectedPlayers[i], turnOrder.get(i).getName(),
                "Player at position " + i + " should be " + expectedPlayers[i] + 
                ", but was " + turnOrder.get(i).getName());
        }
    }

    @Then("^player \"([^\"]*)\" should not be eliminated$")
    public void playerShouldNotBeEliminated(String playerName) {
        Player player = findPlayerByName(playerName);
        assertTrue(player.isAlive(), "Player " + playerName + " should not be eliminated");
    }

    @Given("^player \"([^\"]*)\" has (\\d+) turn left$")
    public void playerHasTurnLeft(String playerName, int turns) {
        Player player = findPlayerByName(playerName);
        player.setLeftTurns(turns);
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
            case "REVERSE":
                return new ReverseCard();
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
            .filter(card -> {
                String cardClassName = card.getClass().getSimpleName();
                String normalizedCardType = cardType + "Card";
                return cardClassName.equals(normalizedCardType);
            })
            .findFirst()
            .orElseThrow(() -> {
                String handInfo = player.getHand().stream()
                    .map(card -> card.getClass().getSimpleName())
                    .collect(Collectors.joining(", "));
                return new IllegalStateException(
                    String.format("Card not found in hand: %s. Current hand: [%s]", 
                    cardType, handInfo));
            });
    }
} 