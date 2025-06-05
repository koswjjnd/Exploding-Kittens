package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import explodingkittens.controller.CatCardStealInputHandler;

/**
 * Test class for BeardCatCard.
 */
public class BeardCatCardTest {
    private BeardCatCard card;
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player player1;
    private Player player2;

    /**
     * Mock InputHandler for testing.
     */
    private static class MockInputHandler implements CatCardStealInputHandler {
        @Override
        public Player selectTargetPlayer(List<Player> availablePlayers) {
            return availablePlayers.get(0); // Always select the first available player
        }

        @Override
        public int selectCardIndex(int handSize) {
            return 0; // Always select the first card
        }

        @Override
        public void handleCardSteal(Player currentPlayer, List<Player> turnOrder, CatType catType) {
            // Mock implementation
        }
    }

    @BeforeEach
    void setUp() {
        card = new BeardCatCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    @Test
    void testConstructor() {
        assertNotNull(card);
        assertEquals(CatType.BEARD_CAT, card.getCatType());
        assertEquals(CardType.CAT_CARD, card.getType());
    }

    @Test
    void testEffectWithTwoBeardCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.receiveCard(card);
        player1.receiveCard(new BeardCatCard());
        player1.setLeftTurns(1);
        player2.receiveCard(new DefuseCard());
        card.setInputHandler(new MockInputHandler());
        assertThrows(CatCard.CatCardEffect.class, () -> card.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithDifferentCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.receiveCard(card);
        player1.receiveCard(new RainbowCatCard());
        player1.setLeftTurns(1);
        card.setInputHandler(new MockInputHandler());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithThreeBeardCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.receiveCard(card);
        player1.receiveCard(new BeardCatCard());
        player1.receiveCard(new BeardCatCard());
        player1.setLeftTurns(1);
        card.setInputHandler(new MockInputHandler());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, gameDeck));
    }
} 