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

    
} 