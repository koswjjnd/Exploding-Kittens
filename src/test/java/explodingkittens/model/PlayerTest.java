package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class PlayerTest {
    private Player player;

    @Mock
    private Card defuseCard;

    @Mock
    private Card nonDefuseCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        player = new Player("TestPlayer");
        when(defuseCard.getType()).thenReturn(CardType.DEFUSE);
        when(nonDefuseCard.getType()).thenReturn(CardType.ATTACK);
    }

    @Test
    void testGetLeftTurnsDefault() {
        // Test Case 1: New player with default turns
        assertEquals(1, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsZero() {
        // Test Case 2: Player with 0 turns left
        player.setLeftTurns(0);
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsMultiple() {
        // Test Case 3: Player with multiple turns left
        player.setLeftTurns(3);
        assertEquals(3, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsAfterSkip() {
        // Test Case 4: Player after using skip card
        player.setLeftTurns(1);
        player.useSkipCard();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsAfterAttack() {
        // Test Case 5: Player after using attack card
        player.setLeftTurns(1);
        player.getAttacked();
        assertEquals(3, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsFromOne() {
        // Test Case 6: Player with 1 turn left
        player.decrementLeftTurns();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsFromZero() {
        // Test Case 7: Player with 0 turns left
        player.setLeftTurns(0);
        player.decrementLeftTurns();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsFromMultiple() {
        // Test Case 8: Player with multiple turns left
        player.setLeftTurns(3);
        player.decrementLeftTurns();
        assertEquals(2, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsNewPlayer() {
        // Test Case 9: New player (default 1 turn)
        player.decrementLeftTurns();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testHasDefuseEmptyHand() {
        // Test Case 10: Player with no cards
        assertFalse(player.hasDefuse());
    }

    @Test
    void testHasDefuseMultipleDefuseCards() {
        // Test Case 11: Player with multiple defuse cards
        player.receiveCard(defuseCard);
        player.receiveCard(defuseCard);
        assertTrue(player.hasDefuse());
    }

    @Test
    void testHasDefuseNonDefuseCards() {
        // Test Case 12: Player with non-defuse cards
        player.receiveCard(nonDefuseCard);
        player.receiveCard(nonDefuseCard);
        assertFalse(player.hasDefuse());
    }

    @Test
    void testHasDefuseOneDefuseCard() {
        // Test Case 13: Player with one defuse card
        player.receiveCard(defuseCard);
        assertTrue(player.hasDefuse());
    }

    @Test
    void testHasDefuseMultipleCards() {
        // Test Case 14: Player with multiple cards
        player.receiveCard(defuseCard);
        player.receiveCard(nonDefuseCard);
    }

    @Test
    void testUseDefuseNoDefuseCard() {
        // Test Case 15: Player with no defuse card
        assertFalse(player.useDefuse());
        assertEquals(0, player.getHand().size());
    }
    
    @Test
    void testUseDefuseOneDefuseCard() {
        // Test Case 16: Player with one defuse card
        player.receiveCard(defuseCard);
        assertTrue(player.useDefuse());
        assertEquals(0, player.getHand().size());
    }

    @Test
    void testUseDefuseMultipleDefuseCards() {
        // Test Case 17: Player with multiple defuse cards
        player.receiveCard(defuseCard);
        player.receiveCard(defuseCard);
        assertTrue(player.useDefuse());
        assertEquals(1, player.getHand().size());
    }

    @Test
    void testUseDefuseNonDefuseCards() {
        // Test Case 18: Player with only non-defuse cards
        player.receiveCard(nonDefuseCard);
        player.receiveCard(nonDefuseCard);
        assertFalse(player.useDefuse());
        assertEquals(2, player.getHand().size());
    }

    @Test
    void testUseDefuseMixedCards() {
        // Test Case 19: Player with mixed cards including defuse
        player.receiveCard(nonDefuseCard);
        player.receiveCard(defuseCard);
        player.receiveCard(nonDefuseCard);
        assertTrue(player.useDefuse());
        assertEquals(2, player.getHand().size());
    }

    @Test
    void testReceiveCardNull() {
        // Test Case 20: Receive null card
        assertThrows(IllegalArgumentException.class, () -> player.receiveCard(null));
    }

    @Test
    void testReceiveCardOne() {
        // Test Case 21: Receive one card
        player.receiveCard(defuseCard);
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().contains(defuseCard));
    }

    @Test
    void testReceiveCardMultiple() {
        // Test Case 22: Receive multiple cards
        player.receiveCard(defuseCard);
        player.receiveCard(nonDefuseCard);
        assertEquals(2, player.getHand().size());
        assertTrue(player.getHand().contains(defuseCard));
        assertTrue(player.getHand().contains(nonDefuseCard));
    }

    @Test
    void testReceiveCardDuplicate() {
        // Test Case 23: Receive same card twice
        player.receiveCard(defuseCard);
        player.receiveCard(defuseCard);
        assertEquals(2, player.getHand().size());
        assertEquals(2, player.getHand().stream()
                .filter(card -> card == defuseCard)
                .count());
    }

    @Test
    void testReceiveCardDifferentTypes() {
        // Test Case 24: Receive different types of cards
        player.receiveCard(defuseCard);
        player.receiveCard(nonDefuseCard);
        player.receiveCard(defuseCard);
        assertEquals(3, player.getHand().size());
        assertEquals(2, player.getHand().stream()
                .filter(card -> card.getType() == CardType.DEFUSE)
                .count());
        assertEquals(1, player.getHand().stream()
                .filter(card -> card.getType() == CardType.ATTACK)
                .count());
    }
    
} 