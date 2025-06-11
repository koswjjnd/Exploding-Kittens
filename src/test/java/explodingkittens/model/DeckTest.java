package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


/**
 * Test class for the Deck class.
 */
public class DeckTest {
    private Deck deck;
    private Card skipCard;
    private Card attackCard;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        skipCard = new SkipCard();
        attackCard = new AttackCard();
    }

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

        assertEquals(3, cardCounts.get("DefuseCard"));  // 5 - 2
        assertEquals(2, cardCounts.get("AttackCard"));
        assertEquals(2, cardCounts.get("SkipCard"));
        assertEquals(2, cardCounts.get("ShuffleCard"));
        assertEquals(2, cardCounts.get("See_the_futureCard"));
        assertEquals(4, cardCounts.get("NopeCard"));

        
        assertEquals(5, cardCounts.get("CatCard_WATERMELON_CAT"));
        assertEquals(5, cardCounts.get("CatCard_TACOCAT"));
        assertEquals(5, cardCounts.get("CatCard_BEARD_CAT"));
        assertEquals(5, cardCounts.get("CatCard_RAINBOW_CAT"));
        assertEquals(5, cardCounts.get("CatCard_HAIRY_POTATO_CAT"));


        assertEquals(1, cardCounts.get("SnatchCard"));
        assertEquals(1, cardCounts.get("Switch_deck_by_halfCard"));
        assertEquals(1, cardCounts.get("Time_rewindCard"));
        assertEquals(1, cardCounts.get("FavorCard"));
        assertEquals(2, cardCounts.get("Draw_from_bottomCard"));
        assertEquals(2, cardCounts.get("ReverseCard"));
        assertEquals(2, cardCounts.get("Super_skipCard"));
        assertEquals(2, cardCounts.get("Double_skipCard"));
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
        assertEquals(2, cardCounts.get("AttackCard"));
        assertEquals(2, cardCounts.get("SkipCard"));
        assertEquals(2, cardCounts.get("ShuffleCard"));
        assertEquals(2, cardCounts.get("See_the_futureCard"));
        assertEquals(4, cardCounts.get("NopeCard"));

        
        assertEquals(5, cardCounts.get("CatCard_WATERMELON_CAT"));
        assertEquals(5, cardCounts.get("CatCard_TACOCAT"));
        assertEquals(5, cardCounts.get("CatCard_BEARD_CAT"));
        assertEquals(5, cardCounts.get("CatCard_RAINBOW_CAT"));
        assertEquals(5, cardCounts.get("CatCard_HAIRY_POTATO_CAT"));


        assertEquals(1, cardCounts.get("SnatchCard"));
        assertEquals(1, cardCounts.get("Switch_deck_by_halfCard"));
        assertEquals(1, cardCounts.get("Time_rewindCard"));
        assertEquals(1, cardCounts.get("FavorCard"));
        assertEquals(2, cardCounts.get("Draw_from_bottomCard"));
        assertEquals(2, cardCounts.get("ReverseCard"));
        assertEquals(2, cardCounts.get("Super_skipCard"));
        assertEquals(2, cardCounts.get("Double_skipCard"));
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
        
        Map<String, Integer> cardCounts = deck.getCardCounts();
        assertEquals(6, cardCounts.size());
        assertEquals(1, cardCounts.get("SkipCard"));
        assertEquals(1, cardCounts.get("ShuffleCard"));
        assertEquals(1, cardCounts.get("AttackCard"));
        assertEquals(1, cardCounts.get("DefuseCard"));
        assertEquals(1, cardCounts.get("See_the_futureCard"));
        assertEquals(1, cardCounts.get("NopeCard"));
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
        assertEquals(2, cardCounts.get("See_the_futureCard"));
    }

    @Test
    void testDrawFromEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> 
            deck.drawOne(),
            "Should throw IllegalStateException when drawing from empty deck"
        );
    }

    @Test
    void testInsertCardAtValidPosition() {
        // Insert cards at different positions
        deck.insertAt(skipCard, 0);
        deck.insertAt(attackCard, 1);

        // Verify cards are in correct positions
        assertEquals(skipCard, deck.drawOne(), "First card should be SkipCard");
        assertEquals(attackCard, deck.drawOne(), "Second card should be AttackCard");
    }

    @Test
    void testInsertNullCard() {
        assertThrows(IllegalArgumentException.class, () -> 
            deck.insertAt(null, 0),
            "Should throw IllegalArgumentException when inserting null card"
        );
    }

    @Test
    void testInsertCardAtNegativePosition() {
        assertThrows(IllegalArgumentException.class, () -> 
            deck.insertAt(skipCard, -1),
            "Should throw IllegalArgumentException when inserting at negative position"
        );
    }

    @Test
    void testInsertCardAtPositionBeyondSize() {
        assertThrows(IllegalArgumentException.class, () -> 
            deck.insertAt(skipCard, 1),
            "Should throw IllegalArgumentException when inserting at position beyond size"
        );
    }

    @Test
    void testPeekAtEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> 
            deck.peekTop(),
            "Should throw IllegalStateException when peeking at empty deck"
        );
    }

    @Test
    void testPeekAtNonEmptyDeck() {
        deck.insertAt(skipCard, 0);
        deck.insertAt(attackCard, 1);

        // Verify peek returns first card without removing it
        assertEquals(skipCard, deck.peekTop(), "Peek should return first card");
        assertEquals(2, deck.size(), "Deck size should remain unchanged after peek");
    }

    @Test
    void testSize() {
        assertEquals(0, deck.size(), "Empty deck should have size 0");
        
        deck.insertAt(skipCard, 0);
        assertEquals(1, deck.size(), "Deck should have size 1 after inserting one card");
        
        deck.insertAt(attackCard, 1);
        assertEquals(2, deck.size(), "Deck should have size 2 after inserting two cards");
        
        deck.drawOne();
        assertEquals(1, deck.size(), "Deck should have size 1 after drawing one card");
    }
}
