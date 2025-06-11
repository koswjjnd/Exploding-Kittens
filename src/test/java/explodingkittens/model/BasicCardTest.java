package explodingkittens.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
} 