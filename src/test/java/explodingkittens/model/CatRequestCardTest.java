package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.List;
import explodingkittens.controller.CatCardRequestController;
import explodingkittens.controller.CatCardRequestInputHandler;
import explodingkittens.model.Card;
import explodingkittens.model.SkipCard;

class CatRequestCardTest {
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player currentPlayer;
    private Player targetPlayer;
    private CatRequestCard catRequestCard;
    private CatCardRequestController controller;
    private CatCardRequestInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        currentPlayer = new Player("Player1");
        targetPlayer = new Player("Player2");
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);
        catRequestCard = new CatRequestCard(CatType.TACOCAT);
        inputHandler = new MockCardRequestInputHandler();
        controller = new CatCardRequestController(inputHandler);
        CatRequestCard.setController(controller);
    }

    private class MockCardRequestInputHandler implements CatCardRequestInputHandler {
        @Override
        public Player selectTargetPlayer(List<Player> availablePlayers) {
            return targetPlayer;
        }

        @Override
        public Card selectCard(Player targetPlayer) {
            return targetPlayer.getHand().get(0);
        }
    }

    @Test
    @DisplayName("Test Case 1: Controller not set")
    void testNoController() {
        // First verify that setting null controller throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            CatRequestCard.setController(null);
        }, "Should throw exception when setting controller to null");
        
        // Clear the controller by creating a new instance without setting it
        CatRequestCard newCard = new CatRequestCard(CatType.TACOCAT);
        
        currentPlayer.receiveCard(newCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        assertThrows(IllegalStateException.class, () -> {
            newCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when controller is not set");
    }

    @Test
    @DisplayName("Test Case 2: Player has no turns left")
    void testNoTurnsLeft() {
        currentPlayer.setLeftTurns(0);
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        assertThrows(IllegalStateException.class, () -> {
            catRequestCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no turns left");
    }

	@Test
    @DisplayName("Test Case 3: Player has less than three cat cards of EXACT SAME TYPE")
    void testLessThanThreeCatCards() {
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.BEARD_CAT)); // Different type
        
        assertThrows(IllegalStateException.class, () -> {
            catRequestCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has less than three cat cards of EXACT SAME TYPE");
    }

    @Test
    @DisplayName("Test Case 4: Player has exactly three cat cards of EXACT SAME TYPE")
    void testExactlyThreeCatCards() {
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());
        
        catRequestCard.effect(turnOrder, gameDeck);
        
        assertEquals(1, currentPlayer.getHand().size(), 
            "Should have one card (the requested card)");
        assertTrue(targetPlayer.getHand().isEmpty(), 
            "Should have no cards after giving one away");
    }

    @Test
    @DisplayName("Test Case 5: Player has more than three cat cards of EXACT SAME TYPE")
    void testMoreThanThreeCatCards() {
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());
        
        catRequestCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, currentPlayer.getHand().size(), 
            "Should have two cards (one remaining cat card and the requested card)");
        assertTrue(targetPlayer.getHand().isEmpty(), 
            "Should have no cards after giving one away");
    }

    @Test
    @DisplayName("Test Case 6: No other players available")
    void testNoOtherPlayers() {
        turnOrder.remove(targetPlayer);
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        assertThrows(IllegalStateException.class, () -> {
            catRequestCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when no other players are available");
    }

    @Test
    @DisplayName("Test Case 7: Target player is dead")
    void testDeadTargetPlayer() {
        targetPlayer.setAlive(false);
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        assertThrows(IllegalStateException.class, () -> {
            catRequestCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player is dead");
    }

    @Test
    @DisplayName("Test Case 8: Target player has no cards")
    void testEmptyTargetHand() {
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        assertThrows(IllegalStateException.class, () -> {
            catRequestCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player has no cards");
    }

    @Test
    @DisplayName("Test Case 9: Target player has the requested card")
    void testTargetHasRequestedCard() {
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());
        
        catRequestCard.effect(turnOrder, gameDeck);
        
        assertEquals(1, 
            currentPlayer.getHand().size(), 
            "Should have one card (the requested card)");
        assertTrue(targetPlayer.getHand().isEmpty(), 
            "Should have no cards after giving one away");
    }

    @Test
    @DisplayName("Test Case 10: Target player doesn't have the requested card")
    void testTargetDoesntHaveRequestedCard() {
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());
        
        catRequestCard.effect(turnOrder, gameDeck);
        
        assertEquals(1, 
            currentPlayer.getHand().size(), 
            "Should have one card (the requested card)");
        assertTrue(targetPlayer.getHand().isEmpty(), 
            "Should have no cards after giving one away");
    }

    @Test
    @DisplayName("Test Case 2: Handle card request with invalid target")
    void testHandleCardRequestWithInvalidTarget() {
        // Create a mock input handler that returns an invalid target
        inputHandler = new CatCardRequestInputHandler() {
            @Override
            public Player selectTargetPlayer(List<Player> availablePlayers) {
                return null;
            }

            @Override
            public Card selectCard(Player targetPlayer) {
                return targetPlayer.getHand().get(0);
            }
        };
        controller = new CatCardRequestController(inputHandler);
        CatRequestCard.setController(controller);

        // Give target player some cards
        targetPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new CatCard(CatType.BEARD_CAT));

        // Give current player three TACOCAT cards for the request
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));

        assertThrows(IllegalArgumentException.class, 
            () -> catRequestCard.effect(turnOrder, gameDeck));
    }

    @Test
    @DisplayName("Test Case 3: Handle card request with valid target")
    void testHandleCardRequestWithValidTarget() {
        // Give target player some cards
        targetPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new CatCard(CatType.BEARD_CAT));

        // Give current player three TACOCAT cards for the request
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));

        assertDoesNotThrow(() -> catRequestCard.effect(turnOrder, gameDeck));
    }

    @Test
    @DisplayName("Test Case: getController throws exception when controller is not set")
    void testGetControllerWithNoController() {
        // Reset the controller by setting it to null using reflection
        try {
            java.lang.reflect.Field inputHandlerField = 
                CatRequestCard.class.getDeclaredClasses()[0].getDeclaredField("inputHandler");
            inputHandlerField.setAccessible(true);
            inputHandlerField.set(null, null);
            
            // Verify that getController throws IllegalStateException
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> CatRequestCard.getController(),
                "Should throw IllegalStateException when controller is not set"
            );
            assertEquals("Controller not set", exception.getMessage());
        }
        catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    void testEffectWithNoController() {
        CatRequestCard card = new CatRequestCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        Player player = new Player("Player1");
        players.add(player);
        
        for (int i = 0; i < 3; i++) {
            player.receiveCard(new CatRequestCard(CatType.TACOCAT));
        }
        
        // check: should throw IllegalStateException when controller is not set
        assertThrows(IllegalStateException.class, () -> {
            card.effect(players, new Deck());
        });
    }

    @Test
    void testEffectWithNullTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> 
            catRequestCard.effect(null, gameDeck),
            "Should throw IllegalArgumentException when turn order is null"
        );
    }

    @Test
    void testEffectWithNullCurrentPlayer() {
        // Create a list with null as the first element
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(null);
        
        assertThrows(IllegalArgumentException.class, () -> 
            catRequestCard.effect(turnOrder, gameDeck),
            "Should throw IllegalArgumentException when current player is null"
        );
    }

    @Test
    void testEffectWithNoTurnsLeft() {
        // Create a player with no turns left
        Player currentPlayer = new Player("Current");
        currentPlayer.setLeftTurns(0);
        turnOrder.add(currentPlayer);
        
        assertThrows(IllegalStateException.class, () -> 
            catRequestCard.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when player has no turns left"
        );
    }

    @Test
    void testEffectWithInsufficientCatCards() {
        // Create a player with only two cat cards of the same type
        Player currentPlayer = new Player("Current");
        currentPlayer.receiveCard(new CatRequestCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatRequestCard(CatType.TACOCAT));
        turnOrder.add(currentPlayer);
        
        assertThrows(IllegalStateException.class, () -> 
            catRequestCard.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when player has insufficient cat cards"
        );
    }

    @Test
    void testEffectWithNoAvailablePlayers() {
        // Create a player with three cat cards
        Player currentPlayer = new Player("Current");
        currentPlayer.receiveCard(new CatRequestCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatRequestCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatRequestCard(CatType.TACOCAT));
        turnOrder.add(currentPlayer);
        
        assertThrows(IllegalStateException.class, () -> 
            catRequestCard.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when no other players are available"
        );
    }
} 