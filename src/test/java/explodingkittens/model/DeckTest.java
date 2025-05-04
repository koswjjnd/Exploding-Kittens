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

    @Test
    public void test_initializeBaseDeck_playerCount4() {
        Deck deck = new Deck();
        deck.initializeBaseDeck(4);
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(1, cardCounts.get("DefuseCard"));
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

    @Test
    public void test_initializeBaseDeck_invalidPlayerCount5() {
        Deck deck = new Deck();
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(5));
    }

    @Test
    public void test_getCardCounts_emptyDeck() {
        Deck deck = new Deck();
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertTrue(cardCounts.isEmpty());
    }

    
}
