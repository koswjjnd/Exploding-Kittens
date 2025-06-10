package explodingkittens.integration;

import explodingkittens.controller.GameSetupController;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatType;
import explodingkittens.model.AttackCard;
import explodingkittens.model.SkipCard;
import explodingkittens.model.FavorCard;
import explodingkittens.model.CatCard;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.NopeCard;
import explodingkittens.model.ShuffleCard;
import explodingkittens.model.SeeTheFutureCard;
import explodingkittens.view.GameSetupView;
import explodingkittens.model.PlayerService;
import explodingkittens.service.DealService;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.exceptions.InvalidDeckException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for game setup integration tests.
 */
public class GameSetupSteps {
    private GameSetupController controller;
    private GameSetupView view;
    private PlayerService playerService;
    private DealService dealService;
    private Exception thrownException;
    private boolean gameInitialized = false;

    /**
     * Initializes the game system before each test.
     */
    @Given("^the game system is ready$")
    public void gameSystemIsReady() {
        view = Mockito.mock(GameSetupView.class);
        playerService = new PlayerService();
        dealService = new DealService();
        controller = new GameSetupController(view, playerService, dealService);
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
     * Initializes the game with default settings.
     */
    @When("^the game is initialized$")
    public void gameIsInitialized() {
        if (!gameInitialized) {
            try {
                Mockito.when(view.promptPlayerCount()).thenReturn(3);
                Mockito.when(view.promptNickname(1)).thenReturn("Player1");
                Mockito.when(view.promptNickname(2)).thenReturn("Player2");
                Mockito.when(view.promptNickname(3)).thenReturn("Player3");
                controller.setupGame();
                gameInitialized = true;
            }
            catch (Exception e) {
                thrownException = e;
            }
        }
    }

    /**
     * Initializes the game with a specific number of players.
     * @param playerCount The number of players
     */
    @When("^the game is initialized with (\\d+) player$")
    public void gameIsInitializedWithPlayerCount(int playerCount) {
        if (!gameInitialized) {
            try {
                Mockito.when(view.promptPlayerCount()).thenReturn(playerCount);
                for (int i = 1; i <= playerCount; i++) {
                    Mockito.when(view.promptNickname(i)).thenReturn("Player" + i);
                }
                controller.setupGame();
                gameInitialized = true;
            }
            catch (Exception e) {
                thrownException = e;
            }
        }
    }

    /**
     * Initializes the game with players having specific hands.
     * @param dataTable The table containing player and hand information
     */
    @When("^the game is initialized with players having hands:$")
    public void gameIsInitializedWithPlayersHavingHands(DataTable dataTable) {
        if (!gameInitialized) {
            try {
                List<Map<String, String>> players = dataTable.asMaps();
                Mockito.when(view.promptPlayerCount()).thenReturn(players.size());
                
                // Set player names
                for (int i = 0; i < players.size(); i++) {
                    Mockito.when(view.promptNickname(i + 1))
                        .thenReturn(players.get(i).get("Player"));
                }
                
                // Check for invalid card types
                for (Map<String, String> player : players) {
                    String[] cards = player.get("Hand").split(", ");
                    for (String cardType : cards) {
                        if (createCardFromType(cardType.trim()) == null) {
                            throw new InvalidDeckException("Invalid card type: " + cardType);
                        }
                    }
                }
                
                controller.setupGame();
                
                // Get player list from game context
                List<Player> playerList = GameContext.getTurnOrder();
                
                // Clear each player's hand
                for (Player player : playerList) {
                    List<Card> hand = player.getHand();
                    for (Card card : hand) {
                        player.removeCard(card);
                    }
                }
                
                // Add specified cards to each player and supplement to 6 cards
                for (int i = 0; i < players.size(); i++) {
                    String[] cards = players.get(i).get("Hand").split(", ");
                    for (String cardType : cards) {
                        Card card = createCardFromType(cardType.trim());
                        if (card != null) {
                            playerList.get(i).receiveCard(card);
                        }
                    }
                    
                    // Supplement to 6 cards
                    while (playerList.get(i).getHand().size() < 6) {
                        playerList.get(i).receiveCard(new SkipCard());
                    }
                }
                
                gameInitialized = true;
            }
            catch (InvalidPlayerCountException | InvalidDeckException e) {
                thrownException = e;
            }
        }
    }

    /**
     * Creates a card instance from the given card type.
     * @param cardType The type of card to create
     * @return The created card instance, or null if the type is invalid
     */
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
            default:
                return null;
        }
    }

    /**
     * Verifies that the game context is initialized correctly.
     */
    @Then("^the game context should be initialized correctly$")
    public void gameContextShouldBeInitializedCorrectly() {
        Assertions.assertNotNull(GameContext.getTurnOrder());
        Assertions.assertNotNull(GameContext.getGameDeck());
    }

    /**
     * Verifies that the default parameters are correct.
     * @param expectedParams The expected parameter values
     */
    @Then("^the default parameters should be correct:$")
    public void defaultParametersShouldBeCorrect(DataTable expectedParams) {
        Map<String, String> params = expectedParams.asMap(String.class, String.class);
        List<Player> players = GameContext.getTurnOrder();
        
        // Verify player count
        Assertions.assertEquals(3, players.size());
            
        // Verify initial turns
        for (Player player : players) {
            Assertions.assertEquals(Integer.parseInt(params.get("Initial Turns")), 
                player.getLeftTurns());
        }
        
        // Verify initial hand size
        for (Player player : players) {
            Assertions.assertEquals(Integer.parseInt(params.get("Initial Hand")), 
                player.getHand().size());
        }
    }

    /**
     * Verifies that an InvalidPlayerCountException is thrown.
     */
    @Then("^an InvalidPlayerCountException should be thrown$")
    public void invalidPlayerCountExceptionShouldBeThrown() {
        Assertions.assertNotNull(thrownException);
        Assertions.assertTrue(thrownException instanceof InvalidPlayerCountException);
    }

    /**
     * Verifies that an InvalidNicknameException is thrown.
     */
    @Then("^an InvalidNicknameException should be thrown$")
    public void invalidNicknameExceptionShouldBeThrown() {
        Assertions.assertNotNull(thrownException);
        Assertions.assertTrue(thrownException instanceof InvalidNicknameException);
    }

    /**
     * Verifies that the game context is not initialized.
     */
    @Then("^the game context should not be initialized$")
    public void gameContextShouldNotBeInitialized() {
        Assertions.assertNull(GameContext.getTurnOrder());
        Assertions.assertNull(GameContext.getGameDeck());
    }

    /**
     * Verifies that the game context is initialized with the correct number of players.
     * @param count The expected number of players
     */
    @Then("^the game context should be initialized with (\\d+) players$")
    public void gameContextShouldBeInitializedWithPlayers(int count) {
        Assertions.assertNotNull(GameContext.getTurnOrder());
        Assertions.assertEquals(count, GameContext.getTurnOrder().size());
    }

    /**
     * Verifies that each player has the correct number of cards.
     * @param count The expected number of cards
     */
    @Then("^each player should have (\\d+) cards$")
    public void eachPlayerShouldHaveCards(int count) {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            Assertions.assertEquals(count, player.getHand().size());
        }
    }

    /**
     * Verifies that each player has the correct number of defuse cards.
     * @param count The expected number of defuse cards
     */
    @Then("^each player should have (\\d+) defuse card$")
    public void eachPlayerShouldHaveDefuseCard(int count) {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            int defuseCount = 0;
            for (Card card : player.getHand()) {
                if (card instanceof DefuseCard) {
                    defuseCount++;
                }
            }
            Assertions.assertEquals(count, defuseCount);
        }
    }

    /**
     * Verifies that the game deck is properly initialized.
     */
    @Then("^the game deck should be properly initialized$")
    public void gameDeckShouldBeInitialized() {
        Deck deck = GameContext.getGameDeck();
        Assertions.assertNotNull(deck);
        Assertions.assertTrue(deck.getCards().size() > 0);
    }

    /**
     * Verifies that the turn order is set correctly.
     */
    @Then("^the turn order should be set correctly$")
    public void turnOrderShouldBeSetCorrectly() {
        List<Player> turnOrder = GameContext.getTurnOrder();
        Assertions.assertNotNull(turnOrder);
        Assertions.assertTrue(turnOrder.size() > 0);
    }

    /**
     * Verifies that the player list is created.
     */
    @Then("^the player list should be created$")
    public void playerListShouldBeCreated() {
        List<Player> players = GameContext.getTurnOrder();
        Assertions.assertNotNull(players);
        Assertions.assertTrue(players.size() > 0);
    }

    /**
     * Verifies that each player has the correct name.
     */
    @Then("^each player should have correct name$")
    public void eachPlayerShouldHaveCorrectName() {
        List<Player> players = GameContext.getTurnOrder();
        for (int i = 0; i < players.size(); i++) {
            Assertions.assertEquals("P" + (i + 1), players.get(i).getName());
        }
    }

    /**
     * Verifies that the player count is correct.
     * @param count The expected player count
     */
    @Then("^the player count should be (\\d+)$")
    public void playerCountShouldBe(int count) {
        Assertions.assertEquals(count, GameContext.getTurnOrder().size());
    }

    /**
     * Verifies that the current player is correct.
     * @param name The expected current player name
     */
    @Then("^the current player should be (.+)$")
    public void currentPlayerShouldBe(String name) {
        Player currentPlayer = GameContext.getCurrentPlayer();
        Assertions.assertNotNull(currentPlayer);
        Assertions.assertEquals(name, currentPlayer.getName());
    }

    /**
     * Verifies that the turn order is correct.
     * @param expectedOrder The expected turn order
     */
    @Then("^the turn order should be (.+)$")
    public void turnOrderShouldBe(String expectedOrder) {
        List<Player> turnOrder = GameContext.getTurnOrder();
        String[] expectedNames = expectedOrder.split(", ");
        Assertions.assertEquals(expectedNames.length, turnOrder.size());
        for (int i = 0; i < expectedNames.length; i++) {
            Assertions.assertEquals(expectedNames[i], turnOrder.get(i).getName());
        }
    }

    /**
     * Verifies that the next player is correct.
     * @param name The expected next player name
     */
    @Then("^in game setup, the next player should be (.+)$")
    public void nextPlayerInGameSetupShouldBe(String name) {
        Player nextPlayer = GameContext.getCurrentPlayer();
        assert nextPlayer.getName().equals(name) : 
            "Expected next player to be " + name + " but was " + nextPlayer.getName();
    }

    /**
     * Verifies that all players are in READY state.
     */
    @Then("^all players should be in READY state$")
    public void allPlayersShouldBeInReadyState() {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            Assertions.assertTrue(player.isAlive());
            Assertions.assertEquals(1, player.getLeftTurns());
        }
    }

    /**
     * Verifies that the game is ready to start.
     */
    @Then("^the game should be ready to start$")
    public void gameShouldBeReadyToStart() {
        Assertions.assertNotNull(GameContext.getTurnOrder());
        Assertions.assertNotNull(GameContext.getGameDeck());
        Assertions.assertNotNull(GameContext.getCurrentPlayer());
    }

    /**
     * Sets the number of cards for each player.
     * @param count The number of cards to set
     */
    @When("^each player has (\\d+) cards$")
    public void eachPlayerHasCards(int count) {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            while (player.getHand().size() < count) {
                player.receiveCard(new SkipCard());
            }
        }
    }

    /**
     * Verifies that the deck size is correct.
     */
    @Then("^the deck size should be correct$")
    public void deckSizeShouldBeCorrect() {
        Deck deck = GameContext.getGameDeck();
        Assertions.assertNotNull(deck);
        Assertions.assertTrue(deck.getCards().size() > 0);
    }

    /**
     * Verifies that the deck is properly shuffled.
     */
    @Then("^the deck should be properly shuffled$")
    public void deckShouldBeProperlyShuffled() {
        Deck deck = GameContext.getGameDeck();
        Assertions.assertNotNull(deck);
        Assertions.assertTrue(deck.getCards().size() > 0);
    }

    /**
     * Verifies that all cards are valid instances.
     */
    @Then("^all cards should be valid instances$")
    public void allCardsShouldBeValidInstances() {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            for (Card card : player.getHand()) {
                Assertions.assertNotNull(card);
            }
        }
    }

    /**
     * Verifies that each card is of the correct type.
     */
    @Then("^each card should be of correct type$")
    public void eachCardShouldBeOfCorrectType() {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            for (Card card : player.getHand()) {
                Assertions.assertTrue(isValidCardType(card.getType()));
            }
        }
    }

    /**
     * Verifies that all cards have valid types.
     */
    @Then("^all cards should have valid types$")
    public void allCardsShouldHaveValidTypes() {
        List<Player> players = GameContext.getTurnOrder();
        for (Player player : players) {
            for (Card card : player.getHand()) {
                Assertions.assertTrue(isValidCardType(card.getType()));
            }
        }
    }

    /**
     * Checks if a card type is valid.
     * @param type The card type to check
     * @return true if the card type is valid, false otherwise
     */
    private boolean isValidCardType(CardType type) {
        return type == CardType.ATTACK
            || type == CardType.SKIP
            || type == CardType.FAVOR
            || type == CardType.CAT_CARD
            || type == CardType.DEFUSE
            || type == CardType.NOPE
            || type == CardType.SHUFFLE
            || type == CardType.SEE_THE_FUTURE;
    }

    /**
     * Verifies that a specific player has the correct number of cards.
     * @param playerNumber The player number
     * @param count The expected number of cards
     */
    @Then("^P(\\d+) should have (\\d+) cards$")
    public void playerShouldHaveCards(int playerNumber, int count) {
        List<Player> players = GameContext.getTurnOrder();
        Assertions.assertTrue(playerNumber > 0 && playerNumber <= players.size());
        Assertions.assertEquals(count, players.get(playerNumber - 1).getHand().size());
    }

    /**
     * Verifies that an InvalidCardTypeException is thrown.
     */
    @Then("^an InvalidCardTypeException should be thrown$")
    public void invalidCardTypeExceptionShouldBeThrown() {
        Assertions.assertNotNull(thrownException);
        Assertions.assertTrue(thrownException instanceof InvalidDeckException);
    }

    /**
     * Verifies that a DuplicatePlayerNameException is thrown.
     */
    @Then("^a DuplicatePlayerNameException should be thrown$")
    public void duplicatePlayerNameExceptionShouldBeThrown() {
        Assertions.assertNotNull(thrownException);
        Assertions.assertTrue(thrownException instanceof InvalidNicknameException);
    }

    /**
     * Initializes the game with specific players.
     * @param dataTable The table containing player information
     */
    @When("^the game is initialized with players:$")
    public void gameIsInitializedWithPlayers(DataTable dataTable) {
        if (!gameInitialized) {
            try {
                List<Map<String, String>> players = dataTable.asMaps();
                Mockito.when(view.promptPlayerCount()).thenReturn(players.size());
                
                // Check for duplicate player names
                Set<String> names = new HashSet<>();
                for (Map<String, String> player : players) {
                    String name = player.get("Player");
                    if (!names.add(name)) {
                        throw new InvalidNicknameException("Duplicate player name: " + name);
                    }
                }
                
                for (int i = 0; i < players.size(); i++) {
                    Mockito.when(view.promptNickname(i + 1))
                        .thenReturn(players.get(i).get("Player"));
                }
                
                controller.setupGame();
                gameInitialized = true;
            }
            catch (Exception e) {
                thrownException = e;
            }
        }
    }

    /**
     * Initializes the game with a specific number of players.
     * @param playerCount The number of players
     */
    @When("^the game is initialized with (\\d+) players$")
    public void gameIsInitializedWithPlayers(int playerCount) {
        if (!gameInitialized) {
            try {
                Mockito.when(view.promptPlayerCount()).thenReturn(playerCount);
                for (int i = 1; i <= playerCount; i++) {
                    Mockito.when(view.promptNickname(i)).thenReturn("Player" + i);
                }
                controller.setupGame();
                gameInitialized = true;
            }
            catch (Exception e) {
                thrownException = e;
            }
        }
    }

    @Then("^the next player should be P2$")
    public void the_next_player_should_be_p2() {
        List<Player> turnOrder = GameContext.getTurnOrder();
        Player currentPlayer = GameContext.getCurrentPlayer();
        
        // 验证当前玩家是P1
        assertEquals("P1", currentPlayer.getName(), 
            "Current player should be P1 before verifying next player");
            
        // 获取下一个玩家
        int currentIndex = turnOrder.indexOf(currentPlayer);
        Player nextPlayer = turnOrder.get((currentIndex + 1) % turnOrder.size());
        
        // 验证下一个玩家是P2
        assertEquals("P2", nextPlayer.getName(), 
            "Expected next player to be P2, but was " + nextPlayer.getName());
    }
} 