package explodingkittens.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for basic card functionality.
 */
public class BasicCardTest {
    @Test
    void testSkipCard() {
        SkipCard card = new SkipCard();
        assertEquals(CardType.SKIP, card.getType());
    }

    @Test
    void testAttackCard() {
        AttackCard card = new AttackCard();
        assertEquals(CardType.ATTACK, card.getType());
    }

    @Test
    void testFavorCard() {
        FavorCard card = new FavorCard();
        assertEquals(CardType.FAVOR, card.getType());
    }

    @Test
    void testShuffleCard() {
        ShuffleCard card = new ShuffleCard();
        assertEquals(CardType.SHUFFLE, card.getType());
    }

    @Test
    void testSeeTheFutureCard() {
        SeeTheFutureCard card = new SeeTheFutureCard();
        assertEquals(CardType.SEE_THE_FUTURE, card.getType());
    }

    @Test
    void testNopeCard() {
        NopeCard card = new NopeCard();
        assertEquals(CardType.NOPE, card.getType());
    }

    @Test
    void testGetType() {
        BasicCard card = new BasicCard(CardType.SKIP);
        assertEquals(CardType.SKIP, card.getType());
    }

    @Test
    void testEquals() {
        BasicCard card1 = new BasicCard(CardType.SKIP);
        BasicCard card2 = new BasicCard(CardType.SKIP);
        BasicCard card3 = new BasicCard(CardType.ATTACK);
        
        assertTrue(card1.equals(card1)); // same instance
        assertTrue(card1.equals(card2)); // same type
        assertFalse(card1.equals(card3)); // different type
        assertFalse(card1.equals(null)); // null check
        assertFalse(card1.equals("not a card")); // different class
    }

    @Test
    void testHashCode() {
        BasicCard card1 = new BasicCard(CardType.SKIP);
        BasicCard card2 = new BasicCard(CardType.SKIP);
        BasicCard card3 = new BasicCard(CardType.ATTACK);
        
        // Verify exact hash code calculation
        int expectedHash1 = 31 * card1.getClass().hashCode() + CardType.SKIP.hashCode();
        int expectedHash2 = 31 * card2.getClass().hashCode() + CardType.SKIP.hashCode();
        int expectedHash3 = 31 * card3.getClass().hashCode() + CardType.ATTACK.hashCode();
        
        assertEquals(expectedHash1, card1.hashCode());
        assertEquals(expectedHash2, card2.hashCode());
        assertEquals(expectedHash3, card3.hashCode());
        
        // Verify that same type cards have same hash code
        assertEquals(card1.hashCode(), card2.hashCode());
        // Verify that different type cards have different hash codes
        assertNotEquals(card1.hashCode(), card3.hashCode());
    }

    @Test
    void testToString() {
        BasicCard card = new BasicCard(CardType.SKIP);
        assertEquals("SKIP", card.toString());
    }

    @Test
    void testEffect() {
        BasicCard card = new BasicCard(CardType.SKIP);
        List<Player> turnOrder = new ArrayList<>();
        Deck gameDeck = new Deck();
        
        // Basic card's effect should do nothing
        card.effect(turnOrder, gameDeck);
        assertTrue(turnOrder.isEmpty()); 
        assertTrue(gameDeck.isEmpty()); 
    }
} 