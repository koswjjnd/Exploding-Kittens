package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class DeckTest {
    @Test
    public void test_initializeBaseDeck_invalidPlayerCount1() {
        Deck deck = new Deck();
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(1));
    }
    
    @Test
    public void test_initializeBaseDeck_playerCount2() {
        Deck deck = new Deck();
        deck.initializeBaseDeck(2);
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(3, cardCounts.get("DefuseCard"));
        assertEquals(3, cardCounts.get("AttackCard"));
        assertEquals(3, cardCounts.get("SkipCard"));
        assertEquals(4, cardCounts.get("ShuffleCard"));
        assertEquals(4, cardCounts.get("SeeTheFutureCard"));
        assertEquals(4, cardCounts.get("NopeCard"));
        assertEquals(4, cardCounts.get("CatCard_TACOCAT"));
        assertEquals(4, cardCounts.get("CatCard_BEARD_CAT"));
        assertEquals(4, cardCounts.get("CatCard_CATTERMELON"));
        assertEquals(4, cardCounts.get("CatCard_RAINBOW_CAT"));
    }

    
}
