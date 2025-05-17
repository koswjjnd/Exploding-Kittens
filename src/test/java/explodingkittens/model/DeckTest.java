package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import explodingkittens.exceptions.EmptyDeckException;

/**
 * Test class for the Deck class.
 */
public class DeckTest {
    /**
     * Tests that initializing a deck with 1 player throws an IllegalArgumentException.
     */
    @Test
    public void testInitializeBaseDeckInvalidPlayerCount1() {
        Deck deck = new Deck();
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(1));
    }
    
    /**
     * Tests that initializing a deck with 2 players creates the correct number of cards.
     */
    @Test
    public void testInitializeBaseDeckPlayerCount2() {
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

    /**
     * Tests that initializing a deck with 4 players creates the correct number of cards.
     */
    @Test
    public void testInitializeBaseDeckPlayerCount4() {
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

    /**
     * Tests that initializing a deck with 5 players throws an IllegalArgumentException.
     */
    @Test
    public void testInitializeBaseDeckInvalidPlayerCount5() {
        Deck deck = new Deck();
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(5));
    }

    /**
     * Tests that an empty deck returns an empty card count map.
     */
    @Test
    public void testGetCardCountsEmptyDeck() {
        Deck deck = new Deck();
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertTrue(cardCounts.isEmpty());
    }

    /**
     * Tests that a deck with a single skip card returns the correct card count.
     */
    @Test
    public void testGetCardCountsSingleSkipCard() {
        Deck deck = new Deck();
        deck.addCard(new SkipCard());
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(1, cardCounts.size());
        assertEquals(1, cardCounts.get("SkipCard"));
    }

    /**
     * Tests that a deck with one of each card type returns the correct card counts.
     */
    @Test
    public void testGetCardCountsOneOfEachType() {
        Deck deck = new Deck();
        deck.addCards(new SkipCard(), 1);
        deck.addCards(new ShuffleCard(), 1);
        deck.addCards(new AttackCard(), 1);
        deck.addCards(new DefuseCard(), 1);
        deck.addCards(new SeeTheFutureCard(), 1);
        deck.addCards(new NopeCard(), 1);
        deck.addCards(new CatCard(CatType.TACOCAT), 1);
        deck.addCard(new CatCard(CatType.BEARD_CAT));
        deck.addCard(new CatCard(CatType.CATTERMELON));
        deck.addCard(new CatCard(CatType.RAINBOW_CAT));
        
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(10, cardCounts.size());
        assertEquals(1, cardCounts.get("SkipCard"));
        assertEquals(1, cardCounts.get("ShuffleCard"));
        assertEquals(1, cardCounts.get("AttackCard"));
        assertEquals(1, cardCounts.get("DefuseCard"));
        assertEquals(1, cardCounts.get("SeeTheFutureCard"));
        assertEquals(1, cardCounts.get("NopeCard"));
        assertEquals(1, cardCounts.get("CatCard_TACOCAT"));
        assertEquals(1, cardCounts.get("CatCard_BEARD_CAT"));
        assertEquals(1, cardCounts.get("CatCard_CATTERMELON"));
        assertEquals(1, cardCounts.get("CatCard_RAINBOW_CAT"));
    }

    /**
     * Tests that adding zero cards to a deck results in an empty deck.
     */
    @Test
    public void testAddCardsZeroCount() {
        Deck deck = new Deck();
        deck.addCards(new ShuffleCard(), 0);
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertTrue(cardCounts.isEmpty());
    }

    /**
     * Tests that adding a single card to a deck results in the correct card count.
     */
    @Test
    public void testAddCardsSingleCard() {
        Deck deck = new Deck();
        deck.addCards(new SkipCard(), 1);
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(1, cardCounts.size());
        assertEquals(1, cardCounts.get("SkipCard"));
    }

    /**
     * Tests that adding multiple cards to a deck results in the correct card count.
     */
    @Test
    public void testAddCardsMultipleCards() {
        Deck deck = new Deck();
        deck.addCards(new SeeTheFutureCard(), 2);
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(1, cardCounts.size());
        assertEquals(2, cardCounts.get("SeeTheFutureCard"));
    }

    @Test
    void testIsEmptyWithNullCards() {
        Deck deck = new Deck();
        try {
            java.lang.reflect.Field cardsField = Deck.class.getDeclaredField("cards");
            cardsField.setAccessible(true);
            cardsField.set(deck, null);
        }
        catch (Exception e) {
            fail("Failed to set cards to null", e);
        }
        assertThrows(NullPointerException.class, () -> {
            boolean result = deck.isEmpty();
            fail("Should have thrown NullPointerException, but returned: " + result);
        });
    }

    @Test
    void testIsEmptyWithEmptyList() {
        Deck deck = new Deck();
        try {
            java.lang.reflect.Field cardsField = Deck.class.getDeclaredField("cards");
            cardsField.setAccessible(true);
            cardsField.set(deck, new ArrayList<>());
        }
        catch (Exception e) {
            fail("Failed to set cards to empty list", e);
        }
        boolean result = deck.isEmpty();
        assertTrue(result, "Empty deck should return true for isEmpty()");
    }

    @Test
    void testIsEmptyWithSingleCard() {
        Deck deck = new Deck();
        deck.addCard(new SkipCard());
        boolean result = deck.isEmpty();
        assertFalse(result, "Deck with one card should return false for isEmpty()");
    }

    @Test
    void testIsEmptyWithMultipleCards() {
        Deck deck = new Deck();
        deck.addCards(new SkipCard(), 3);
        deck.addCards(new AttackCard(), 2);
        deck.addCards(new DefuseCard(), 1);
        boolean result = deck.isEmpty();
        assertFalse(result, "Deck with multiple cards should return false for isEmpty()");
        assertEquals(6, deck.getCardCounts().values().stream().mapToInt(Integer::intValue).sum(), 
            "Deck should contain exactly 6 cards");
    }

    @Test
    void testGetCardsWithEmptyDeck() {
        Deck deck = new Deck();
        List<Card> cards = deck.getCards();
        assertTrue(cards.isEmpty(), "Empty deck should return empty list");
    }

    @Test
    void testGetCardsWithCards() {
        Deck deck = new Deck();
        Card skipCard = new SkipCard();
        Card attackCard = new AttackCard();
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        List<Card> cards = deck.getCards();
        assertEquals(2, cards.size(), "Deck should contain exactly 2 cards");
        assertTrue(cards.contains(skipCard), "Deck should contain SkipCard");
        assertTrue(cards.contains(attackCard), "Deck should contain AttackCard");
    }

    /**
     * Tests that adding zero exploding kittens results in no exploding kittens in the deck.
     */
    @Test
    void testAddExplodingKittensZeroCount() {
        Deck deck = new Deck();
        deck.addExplodingKittens(0);
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertFalse(cardCounts.containsKey("ExplodingKittenCard"), 
            "Deck should not contain any exploding kittens when count is 0");
    }

    /**
     * Tests that adding negative number of exploding kittens throws IllegalArgumentException.
     */
    @Test
    void testAddExplodingKittensNegativeCount() {
        Deck deck = new Deck();
        assertThrows(IllegalArgumentException.class, () -> deck.addExplodingKittens(-1),
            "Should throw IllegalArgumentException when count is negative");
    }

    /**
     * Tests that adding multiple exploding kittens adds the correct number of cards.
     */
    @Test
    void testAddExplodingKittensMultipleCards() {
        Deck deck = new Deck();
        Deck result = deck.addExplodingKittens(2);
        assertSame(deck, result, "Should return the same deck object");
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(1, cardCounts.size(), "Should only have one type of card");
        assertEquals(2, cardCounts.get("ExplodingKittenCard"), 
            "Should have exactly 2 exploding kittens");
    }

    /**
     * Tests that removing a card from an empty deck throws EmptyDeckException.
     */
    @Test
    void testRemoveTopCard_EmptyDeck() {
        Deck deck = new Deck();
        assertThrows(EmptyDeckException.class, () -> deck.removeTopCard(),
            "Should throw EmptyDeckException when removing from empty deck");
    }
}
