package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the Card class.
 */
public class CardTest {
    private SkipCard skipCard;

    @BeforeEach
    void setUp() {
        skipCard = new SkipCard();
    }

    @Test
    void testGetType() {
        assertEquals(CardType.SKIP, skipCard.getType());
    }

    @Test
    void testCloneSuccess() {
        Card original = new SkipCard();
        Card clone = original.clone();
        
        assertNotNull(clone);
        assertNotSame(original, clone);
        assertTrue(clone instanceof SkipCard);
        assertEquals(original.getType(), clone.getType());
    }

    @Test
    void testCloneFailure() {
        // create an anonymous subclass to simulate clone failure
        SkipCard original = new SkipCard() {
            @Override
            public Card clone() {
                try {
                    throw new CloneNotSupportedException("Test exception");
                }
                catch (CloneNotSupportedException e) {
                    throw new AssertionError("Card cloning failed", e);
                }
            }
        };
        
        AssertionError error = assertThrows(AssertionError.class, 
            () -> original.clone());
        assertEquals("Card cloning failed", error.getMessage());
        assertTrue(error.getCause() instanceof CloneNotSupportedException);
    }

    @Test
    void testEquals() {
        SkipCard card1 = new SkipCard();
        SkipCard card2 = new SkipCard();
        SkipCard card3 = new SkipCard();
        
        assertTrue(card1.equals(card1));
        
        assertTrue(card1.equals(card2));
        assertTrue(card2.equals(card1));
        
        assertTrue(card1.equals(card2));
        assertTrue(card2.equals(card3));
        assertTrue(card1.equals(card3));
        
        assertFalse(card1.equals(null));
        
        assertFalse(card1.equals(new AttackCard()));
    }

    @Test
    void testHashCode() {
        SkipCard card1 = new SkipCard();
        SkipCard card2 = new SkipCard();
        
        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), new AttackCard().hashCode());
    }
} 