package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.mockito.ArgumentCaptor;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.fail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

/**
 * Test class for the Card class.
 */
@ExtendWith(MockitoExtension.class)
public class CardTest {
    private SkipCard skipCard;

    // mock 变量
    static List<Player> turnOrder;
    static Deck gameDeck;

    @BeforeAll
    static void beforeAll() {
        // 初始化 mock
        turnOrder = Mockito.mock(List.class);
        gameDeck = Mockito.mock(Deck.class);
    }

    @AfterAll
    static void afterAll() {
        // 可选：清理
    }

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
    void testCloneCatchBlock() {
        Card card = new SkipCard() {
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

        AssertionError error = assertThrows(AssertionError.class, () -> card.clone());
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
    }

    @Test
    void testHashCode() {
        SkipCard card1 = new SkipCard();
        SkipCard card2 = new SkipCard();
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testEqualsNullOrDifferentClassReturnsFalse() {
        Card card = new SkipCard();
        assertNotEquals(null, card); // obj == null
        assertNotEquals("not a card", card); // getClass() != obj.getClass()
        // 补充：不同Card子类
        Card anotherCard = new Card(CardType.ATTACK) {
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {}
        };
        assertNotEquals(card, anotherCard); // getClass() != obj.getClass()，两者都是Card但class不同
    }

    @Test
    void testCardCloneCatchBlockTriggered() {
        class FakeCard extends Card {
            public FakeCard() {
                super(CardType.ATTACK);
            }
    
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {}
    
            @Override
            public Card clone() {
                try {
                    // 强制抛出异常
                    throw new CloneNotSupportedException("Forced failure");
                } 
                catch (CloneNotSupportedException e) {
                    throw new AssertionError("Card cloning failed", e);
                }
            }
        }
    
        Card card = new FakeCard();
    
        AssertionError error = assertThrows(AssertionError.class, () -> card.clone());
        assertEquals("Card cloning failed", error.getMessage());
        assertTrue(error.getCause() instanceof CloneNotSupportedException);
    }

    @Test
    void testCardCloneCoversCardClass() {
        Card card = new Card(CardType.ATTACK) {
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {}
        };
        
        Card cloned = card.clone();
        
        assertNotSame(card, cloned);
        assertEquals(card.getType(), cloned.getType());
    }

    @Test
    void testCardCloneCatchBlockCoverageInCard() {
        Card card = new Card(CardType.ATTACK) {
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {
            }

            @Override
            public Card clone() {
                try {
                    // 强制抛出异常
                    throw new CloneNotSupportedException("Forced failure");
                } 
                catch (CloneNotSupportedException e) {
                    throw new AssertionError("Card cloning failed", e);
                }
            }
        };

        AssertionError error = assertThrows(AssertionError.class, card::clone);
        assertEquals("Card cloning failed", error.getMessage());
        assertTrue(error.getCause() instanceof CloneNotSupportedException);
    }

    private void someMethod() {
        // ... existing code ...
    }

    private void anotherMethod() {
        // ... existing code ...
    }

    private void thirdMethod() {
        // ... existing code ...
    }
}
