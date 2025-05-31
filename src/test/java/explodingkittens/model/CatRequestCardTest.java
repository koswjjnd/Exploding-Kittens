package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import explodingkittens.controller.CardRequestController;
import explodingkittens.controller.CardStealInputHandler;

class CatRequestCardTest {
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player currentPlayer;
    private Player targetPlayer;
    private CatRequestCard catRequestCard;
    private CardRequestController controller;
    private CardStealInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        currentPlayer = new Player("Player1");
        targetPlayer = new Player("Player2");
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);
        catRequestCard = new CatRequestCard(CatType.TACOCAT);
        inputHandler = new MockCardStealInputHandler();
        controller = new CardRequestController(inputHandler);
        CatRequestCard.setController(controller);
    }

    private class MockCardStealInputHandler implements CardStealInputHandler {
        @Override
        public Player selectTargetPlayer(List<Player> availablePlayers) {
            return targetPlayer;
        }

        @Override
        public int selectCardIndex(int handSize) {
            return 0;
        }
    }

    @Test
    @DisplayName("Test Case 1: Controller not set")
    void testNoController() {
        CatRequestCard.setController(null);
        currentPlayer.receiveCard(catRequestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        assertThrows(IllegalStateException.class, () -> {
            catRequestCard.effect(turnOrder, gameDeck);
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
        
        assertEquals(1, currentPlayer.getHand().size(), "Should have one card (the requested card)");
        assertTrue(targetPlayer.getHand().isEmpty(), "Should have no cards after giving one away");
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
        
        assertEquals(2, currentPlayer.getHand().size(), "Should have two cards (one remaining cat card and the requested card)");
        assertTrue(targetPlayer.getHand().isEmpty(), "Should have no cards after giving one away");
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
} 