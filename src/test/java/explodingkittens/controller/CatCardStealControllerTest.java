package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.SkipCard;

class CatCardStealControllerTest {
    private List<Player> turnOrder;
    private Player currentPlayer;
    private Player targetPlayer;
    private CatCardStealController controller;
    private CatCardStealInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        turnOrder = new ArrayList<>();
        currentPlayer = new Player("Player1");
        targetPlayer = new Player("Player2");
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);
        inputHandler = new MockCatCardStealInputHandler();
        controller = new CatCardStealController(inputHandler);
    }

    private class MockCatCardStealInputHandler implements CatCardStealInputHandler {
        @Override
        public Player selectTargetPlayer(List<Player> availablePlayers) {
            return targetPlayer;
        }

        @Override
        public int selectCardIndex(int handSize) {
            return 0;
        }

        @Override
        public void handleCardSteal(Player currentPlayer, List<Player> turnOrder, CatType catType) {
            // Mock implementation
        }
    }

    @Test
    @DisplayName("Test Case 1: Successful card steal")
    void testSuccessfulCardSteal() {
        // Setup
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());

        // Execute
        controller.handleCardSteal(currentPlayer, turnOrder, CatType.TACOCAT);

        // Verify
        assertEquals(1, currentPlayer.getHand().size(), 
            "Should have one card (the stolen card)");
        assertTrue(targetPlayer.getHand().isEmpty(), 
            "Should have no cards after being stolen from");
    }

    @Test
    @DisplayName("Test Case 2: Target player has no cards")
    void testEmptyTargetHand() {
        // Setup
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));

        // Execute and Verify
        assertThrows(IllegalStateException.class, () -> {
            controller.handleCardSteal(currentPlayer, turnOrder, CatType.TACOCAT);
        }, "Should throw exception when target player has no cards");
    }

    @Test
    @DisplayName("Test Case 3: Not enough cat cards")
    void testNotEnoughCatCards() {
        // Setup
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());

        // Execute and Verify
        assertThrows(IllegalStateException.class, () -> {
            controller.handleCardSteal(currentPlayer, turnOrder, CatType.TACOCAT);
        }, "Should throw exception when player doesn't have two cat cards");
    }

    @Test
    @DisplayName("Test Case 4: Different type cat cards")
    void testDifferentTypeCatCards() {
        // Setup
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.BEARD_CAT));
        targetPlayer.receiveCard(new SkipCard());

        // Execute and Verify
        assertThrows(IllegalStateException.class, () -> {
            controller.handleCardSteal(currentPlayer, turnOrder, CatType.TACOCAT);
        }, "Should throw exception when cat cards are of different types");
    }

    @Test
    @DisplayName("Test Case 2: Handle card steal with invalid target")
    void testHandleCardStealWithInvalidTarget() {
        setupInvalidTargetHandler();
        setupPlayerCards();
        assertThrows(IllegalArgumentException.class, () -> {
            controller.handleCardSteal(currentPlayer, turnOrder, CatType.TACOCAT);
        });
    }

    private void setupInvalidTargetHandler() {
        inputHandler = new CatCardStealInputHandler() {
            @Override
            public Player selectTargetPlayer(List<Player> availablePlayers) {
                return null;
            }

            @Override
            public int selectCardIndex(int handSize) {
                return 0;
            }

            @Override
            public void handleCardSteal(Player currentPlayer, List<Player> turnOrder, 
                    CatType catType) {
                // Mock implementation
            }
        };
        controller = new CatCardStealController(inputHandler);
    }

    private void setupPlayerCards() {
        // Give target player some cards
        targetPlayer.receiveCard(new SkipCard());
        targetPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new CatCard(CatType.BEARD_CAT));

        // Give current player two TACOCAT cards for the steal
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
    }
} 