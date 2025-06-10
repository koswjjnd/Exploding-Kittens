package explodingkittens.integration;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.model.AttackCard;
import explodingkittens.model.SkipCard;
import explodingkittens.model.FavorCard;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.NopeCard;
import explodingkittens.model.ShuffleCard;
import explodingkittens.model.SeeTheFutureCard;
import explodingkittens.model.ExplodingKittenCard;
import explodingkittens.model.ReverseCard;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Supplier;

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
                for (int i = 0; i < players.size(); i++) {
                    Mockito.when(setupView.promptNickname(i + 1))
                        .thenReturn(players.get(i).get("Player"));
                }
                setupController.setupGame();
                List<Player> playerList = GameContext.getTurnOrder();
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

    /**
     * Sets up the cards in the deck.
     * @param dataTable The table containing card information
     * @throws IllegalStateException if the game deck is not initialized
     */
    @Given("^the deck is stacked with cards:$")
    public void deckIsStackedWithCards(DataTable dataTable) {
        Deck deck = GameContext.getGameDeck();
        if (deck == null) {
            throw new IllegalStateException(
                "Game deck is not initialized. Make sure to initialize the game first.");
        }
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

    /**
     * Player plays a specific card.
     * @param playerName The name of the player
     * @param cardType The type of card to play
     * @throws RuntimeException if there is an error playing the card
     */
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
            Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList()))
                .thenReturn(card);
            if (cardType.equals("Favor")) {
                Mockito.when(view.checkForNope(Mockito.any(), Mockito.eq(card)))
                    .thenReturn(true);
                Mockito.when(view.promptPlayNope(Mockito.any(), Mockito.any()))
                    .thenReturn(false);
                FavorCard favorCard = (FavorCard) card;
                favorCard.getView().setUserInput("0");
            }
            if (cardType.equals("Skip")) {
                Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("end");
            }
            try {
                turnService.playCard(player, card);
            } 
            catch (InvalidCardException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Player counters with a specific card.
     * @param playerName The name of the player
     * @param cardType The type of card to counter with
     * @throws RuntimeException if there is an error playing the counter card
     */
    @When("^player \"([^\"]*)\" counters with \"([^\"]*)\"$")
    public void playerCountersWithCard(String playerName, String cardType) {
        if (cardType.equals("Nope")) {
            Player player = findPlayerByName(playerName);
            Card card = findCardInHand(player, cardType);
            if (card != null) {
                Mockito.when(view.promptPlayNope(Mockito.eq(player), Mockito.any()))
                    .thenReturn(true);
                Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList()))
                    .thenReturn(card);
                try {
                    turnService.playCard(player, card);
                } 
                catch (InvalidCardException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Player chooses to draw a card.
     * @param playerName The name of the player
     */
    @When("^player \"([^\"]*)\" chooses to draw a card$")
    public void playerChoosesToDrawCard(String playerName) {
        Player player = findPlayerByName(playerName);
        player.setLeftTurns(1);
        Mockito.when(view.promptPlayCard(Mockito.eq(player), Mockito.anyList()))
            .thenReturn(null);
        Mockito.when(view.promptPlayerAction(Mockito.eq(player))).thenReturn("draw");
        turnService.takeTurn(player);
    }

    /**
     * Player chooses not to use defuse card.
     * @param playerName The name of the player
     */
    @When("^player \"([^\"]*)\" chooses not to use defuse$")
    public void playerChoosesNotToUseDefuse(String playerName) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.confirmDefuse(Mockito.eq(player))).thenReturn(false);
        Mockito.doNothing().when(view).displayPlayerEliminated(Mockito.eq(player));
    }

    /**
     * Player chooses to use defuse card.
     * @param playerName The name of the player
     */
    @When("^player \"([^\"]*)\" chooses to use defuse$")
    public void playerChoosesToUseDefuse(String playerName) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.confirmDefuse(Mockito.eq(player))).thenReturn(true);
    }

    /**
     * Player places the exploding kitten at a specific position.
     * @param playerName The name of the player
     * @param position The position to place the card
     */
    @When("^player \"([^\"]*)\" places the exploding kitten at position \"([^\"]*)\"$")
    public void playerPlacesExplodingKitten(String playerName, String position) {
        Mockito.when(view.selectExplodingKittenPosition(Mockito.anyInt()))
            .thenReturn(Integer.parseInt(position));
    }

    /**
     * Player has no defuse card.
     * @param playerName The name of the player
     */
    @When("^player \"([^\"]*)\" has no defuse card$")
    public void playerHasNoDefuseCard(String playerName) {
        Player player = findPlayerByName(playerName);
        Mockito.when(view.confirmDefuse(Mockito.eq(player))).thenReturn(false);
        Mockito.doNothing().when(view).displayPlayerEliminated(Mockito.eq(player));
    }

    /**
     * Verifies that player has a specific card in hand.
     * @param playerName The name of the player
     * @param cardType The type of card to check for
     */
    @Then("^player \"([^\"]*)\" should have \"([^\"]*)\" in hand$")
    public void playerShouldHaveCardInHand(String playerName, String cardType) {
        Player player = findPlayerByName(playerName);
        boolean hasCard = player.getHand().stream()
            .anyMatch(card -> card.getClass().getSimpleName().equals(cardType + "Card"));
        assert hasCard : "Player " + playerName + " should have " + cardType + " in hand";
    }

    /**
     * Verifies that player has no specific cards in hand.
     * @param playerName The name of the player
     * @param cardType The type of card to check for
     */
    @Then("^player \"([^\"]*)\" should have no \"([^\"]*)\" cards$")
    public void playerShouldHaveNoCards(String playerName, String cardType) {
        Player player = findPlayerByName(playerName);
        boolean hasCard = player.getHand().stream()
            .anyMatch(card -> card.getClass().getSimpleName().equals(cardType + "Card"));
        assert !hasCard : "Player " + playerName + " should not have any " + cardType + " cards";
    }

    /**
     * Verifies that the turn has ended.
     */
    @Then("^the turn should end$")
    public void turnShouldEnd() {
        Player currentPlayer = GameContext.getCurrentPlayer();
        assert currentPlayer.getName().equals("P2") :
                "Turn should have ended and current player should be P2";
    }

    /**
     * Verifies that the next player is the specified player.
     * @param playerName The name of the expected next player
     */
    @Then("^the next player should be \"([^\"]*)\"$")
    public void nextPlayerInGameLoopShouldBe(String playerName) {
        Player nextPlayer = GameContext.getCurrentPlayer();
        assert nextPlayer.getName().equals(playerName) : 
            "Expected next player to be " + playerName + " but was " + nextPlayer.getName();
    }

    /**
     * Verifies that the player is alive.
     * @param playerName The name of the player
     */
    @Then("^player \"([^\"]*)\" should be alive$")
    public void playerShouldBeAlive(String playerName) {
        Player player = findPlayerByName(playerName);
        assert player.isAlive() : "Player " + playerName + " should be alive";
    }

    /**
     * Verifies that the player is eliminated.
     * @param playerName The name of the player
     */
    @Then("^player \"([^\"]*)\" should be eliminated$")
    public void playerShouldBeEliminated(String playerName) {
        Player player = findPlayerByName(playerName);
        assert !player.isAlive() : "Player " + playerName + " should be eliminated";
    }

    /**
     * Verifies that the player is not in turn order.
     * @param playerName The name of the player
     */
    @Then("^player \"([^\"]*)\" should not be in turn order$")
    public void playerShouldNotBeInTurnOrder(String playerName) {
        List<Player> turnOrder = GameContext.getTurnOrder();
        boolean playerInTurnOrder = turnOrder.stream()
            .anyMatch(p -> p.getName().equals(playerName) && p.isAlive());
        assert !playerInTurnOrder : "Player " + playerName + " should not be in turn order";
    }

    /**
     * Verifies the number of turns left for a player.
     * @param playerName The name of the player
     * @param expectedTurns The expected number of turns
     */
    @Then("^player \"([^\"]*)\" should have (\\d+) turns left$")
    public void playerShouldHaveTurnsLeft(String playerName, int expectedTurns) {
        Player player = findPlayerByName(playerName);
        assertEquals(expectedTurns, player.getLeftTurns(), 
            "Player " + playerName + " should have " + expectedTurns + " turns left");
    }

    /**
     * Verifies that the player is at the end of turn order.
     * @param playerName The name of the player
     */
    @Then("^player \"([^\"]*)\" should be at the end of turn order$")
    public void playerShouldBeAtEndOfTurnOrder(String playerName) {
        Player player = findPlayerByName(playerName);
        List<Player> turnOrder = GameContext.getTurnOrder();
        assertEquals(player, turnOrder.get(turnOrder.size() - 1), 
            "Player " + playerName + " should be at the end of turn order");
    }

    /**
     * Player gives a card to another player.
     * @param fromPlayerName The name of the player giving the card
     * @param toPlayerName The name of the player receiving the card
     * @throws IllegalStateException if the giving player has no cards
     */
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

    /**
     * Verifies the number of cards in player's hand.
     * @param playerName The name of the player
     * @param expectedCount The expected number of cards
     */
    @Then("^player \"([^\"]*)\" should have (\\d+) card in hand$")
    public void playerShouldHaveCardsInHand(String playerName, int expectedCount) {
        Player player = findPlayerByName(playerName);
        assert player.getHand().size() == expectedCount : 
            "Player " + playerName + " should have " + expectedCount + " cards, but has " 
            + player.getHand().size();
    }

    /**
     * Verifies that a player gives a card to another player.
     * @param fromPlayerName The name of the player giving the card
     * @param toPlayerName The name of the player receiving the card
     * @throws IllegalStateException if the giving player has no cards
     */
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

    /**
     * Sets the deck to be empty.
     */
    @Given("^the deck is empty$")
    public void theDeckIsEmpty() {
        GameContext.getGameDeck().clear();
    }

    /**
     * Verifies that the game is over.
     */
    @Then("^the game should be over$")
    public void theGameShouldBeOver() {
        assert GameContext.isGameOver() : "Game should be over";
    }

    /**
     * Verifies that the player is the winner.
     * @param playerName The name of the player
     */
    @Then("^player \"([^\"]*)\" should be the winner$")
    public void playerShouldBeTheWinner(String playerName) {
        Player player = findPlayerByName(playerName);
        assert player.isAlive() : "Player " + playerName + " should be alive";
        assert GameContext.getTurnOrder().size() == 1 : "Should only be one player left";
        assert GameContext.getTurnOrder().get(0) == player : 
            "Player " + playerName + " should be the last player";
    }

    /**
     * Sets the player as eliminated.
     * @param playerName The name of the player
     */
    @When("^player \"([^\"]*)\" is eliminated$")
    public void playerIsEliminated(String playerName) {
        Player player = findPlayerByName(playerName);
        player.setAlive(false);
        GameContext.removePlayer(player);
    }

    /**
     * Sets the game as over.
     */
    @When("^the game is marked as over$")
    public void theGameIsMarkedAsOver() {
        GameContext.setGameOver(true);
    }

    /**
     * Verifies the game turn order.
     * @param expectedOrder The expected turn order
     */
    @Then("^the game turn order should be \"([^\"]*)\"$")
    public void turnOrderShouldBe(String expectedOrder) {
        List<Player> turnOrder = GameContext.getTurnOrder();
        String[] expectedPlayers = expectedOrder.split(", ");
        
        assertEquals(expectedPlayers.length, turnOrder.size(), 
            "Expected " + expectedPlayers.length + " players in turn order, but got " 
            + turnOrder.size());
            
        for (int i = 0; i < expectedPlayers.length; i++) {
            assertEquals(expectedPlayers[i], turnOrder.get(i).getName(),
                "Player at position " + i + " should be " + expectedPlayers[i] 
                + ", but was " + turnOrder.get(i).getName());
        }
    }

    /**
     * Verifies that the player is not eliminated.
     * @param playerName The name of the player
     */
    @Then("^player \"([^\"]*)\" should not be eliminated$")
    public void playerShouldNotBeEliminated(String playerName) {
        Player player = findPlayerByName(playerName);
        assertTrue(player.isAlive(), "Player " + playerName + " should not be eliminated");
    }

    /**
     * Sets the number of turns left for a player.
     * @param playerName The name of the player
     * @param turns The number of turns
     */
    @Given("^player \"([^\"]*)\" has (\\d+) turn left$")
    public void playerHasTurnLeft(String playerName, int turns) {
        Player player = findPlayerByName(playerName);
        player.setLeftTurns(turns);
    }

    /**
     * Creates a card instance based on the card type.
     * @param cardType The type of card to create
     * @return The created card instance
     */
    private Card createCardFromType(String cardType) {
        Map<String, Supplier<Card>> cardCreators = Map.of(
            "ATTACK", AttackCard::new,
            "SKIP", SkipCard::new,
            "FAVOR", FavorCard::new,
            "CATCARD", () -> new CatCard(CatType.TACOCAT),
            "DEFUSE", DefuseCard::new,
            "NOPE", NopeCard::new,
            "SHUFFLE", ShuffleCard::new,
            "SEE_THE_FUTURE", SeeTheFutureCard::new,
            "EXPLODINGKITTEN", ExplodingKittenCard::new,
            "REVERSE", ReverseCard::new
        );
        return cardCreators.getOrDefault(cardType.toUpperCase(), () -> null).get();
    }

    /**
     * Finds a player by name.
     * @param name The name of the player to find
     * @return The found player instance
     */
    private Player findPlayerByName(String name) {
        return GameContext.getTurnOrder().stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Player not found: " + name));
    }

    /**
     * Finds a card of specific type in player's hand.
     * @param player The player instance
     * @param cardType The type of card to find
     * @return The found card instance
     */
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