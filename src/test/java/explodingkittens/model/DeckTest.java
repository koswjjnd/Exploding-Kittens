package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import explodingkittens.exceptions.EmptyDeckException;

/**
 * Test class for the Deck class.
 */
public class DeckTest {
    private Deck deck;
    private Card skipCard;
    private Card attackCard;
    private Card seeTheFutureCard;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        skipCard = new SkipCard();
        attackCard = new AttackCard();
        seeTheFutureCard = new SeeTheFutureCard();
    }

    @Test
    void testConstructor() {
        assertNotNull(deck);
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    @Test
    void testCopyConstructor() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        Deck copyDeck = new Deck(deck);
        
        assertNotSame(deck, copyDeck);
        assertEquals(deck.size(), copyDeck.size());
        assertNotSame(deck.getCards().get(0), copyDeck.getCards().get(0));
    }

    @Test
    void testInitializeBaseDeckInvalidPlayerCount() {
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(1));
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(5));
    }

    @Test
    void testInitializeBaseDeckValidPlayerCount() {
        deck.initializeBaseDeck(2);
        Map<String, Integer> counts = deck.getCardCounts();
        
        // Debug output
        System.out.println("Actual counts:");
        counts.forEach((key, value) -> System.out.println(key + ": " + value));
        
        // Debug card types
        System.out.println("\nCard types in deck:");
        for (Card card : deck.getCards()) {
            System.out.println("Card class: " + card.getClass().getSimpleName());
            System.out.println("Card type: " + card.getType().name());
            if (card instanceof CatCard) {
                System.out.println("Cat type: " + ((CatCard) card).getCatType().name());
            }
            System.out.println("---");
        }
        
        // Check all expected cards are present
        assertEquals(3, counts.get("DefuseCard")); // 5-2
        assertEquals(2, counts.get("AttackCard"));
        assertEquals(2, counts.get("SkipCard"));
        assertEquals(2, counts.get("ShuffleCard"));
        assertEquals(2, counts.get("See_the_futureCard"));
        assertEquals(4, counts.get("NopeCard"));
        assertEquals(5, counts.get("CatCard_WATERMELON_CAT"));
        assertEquals(5, counts.get("CatCard_BEARD_CAT"));
        assertEquals(5, counts.get("CatCard_HAIRY_POTATO_CAT"));
        assertEquals(5, counts.get("CatCard_RAINBOW_CAT"));
        assertEquals(5, counts.get("CatCard_TACOCAT"));
        assertEquals(1, counts.get("SnatchCard"));
        assertEquals(1, counts.get("Switch_deck_by_halfCard"));
        assertEquals(1, counts.get("Time_rewindCard"));
        assertEquals(1, counts.get("FavorCard"));
        assertEquals(2, counts.get("Draw_from_bottomCard"));
        assertEquals(2, counts.get("ReverseCard"));
        assertEquals(2, counts.get("Super_skipCard"));
        assertEquals(2, counts.get("Double_skipCard"));
    }

    @Test
    void testAddCards() {
        deck.addCards(skipCard, 3);
        assertEquals(3, deck.size());
        
        Map<String, Integer> counts = deck.getCardCounts();
        assertEquals(3, counts.get("SkipCard"));
    }

    @Test
    void testAddCardsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addCards(null, 1));
        assertThrows(IllegalArgumentException.class, () -> deck.addCards(skipCard, -1));
    }

    @Test
    void testAddCard() {
        deck.addCard(skipCard);
        assertEquals(1, deck.size());
        assertEquals(skipCard, deck.peekTop());
    }

    @Test
    void testAddCardInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addCard(null));
    }

    @Test
    void testDrawOne() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        
        Card drawn = deck.drawOne();
        assertEquals(skipCard, drawn);
        assertEquals(1, deck.size());
    }

    @Test
    void testDrawOneEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> deck.drawOne());
    }

    @Test
    void testInsertAt() {
        deck.addCard(skipCard);
        deck.insertAt(attackCard, 0);
        
        assertEquals(attackCard, deck.drawOne());
        assertEquals(skipCard, deck.drawOne());
    }

    @Test
    void testInsertAtInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(null, 0));
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(skipCard, -1));
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(skipCard, 1));
    }

    @Test
    void testPeekTop() {
        deck.addCard(skipCard);
        assertEquals(skipCard, deck.peekTop());
        assertEquals(1, deck.size());
    }

    @Test
    void testPeekTopEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> deck.peekTop());
    }

    @Test
    void testShuffle() {
        deck.addCards(skipCard, 5);
        deck.addCards(attackCard, 5);
        List<Card> beforeShuffle = new ArrayList<>(deck.getCards());
        
        deck.shuffle();
        List<Card> afterShuffle = deck.getCards();
        
        assertEquals(beforeShuffle.size(), afterShuffle.size());
        assertNotEquals(beforeShuffle, afterShuffle);
    }

    @Test
    void testShuffleWithRandom() {
        deck.addCards(skipCard, 5);
        deck.addCards(attackCard, 5);
        List<Card> beforeShuffle = new ArrayList<>(deck.getCards());
        
        deck.shuffle(new Random(42)); // Fixed seed for deterministic testing
        List<Card> afterShuffle = deck.getCards();
        
        assertEquals(beforeShuffle.size(), afterShuffle.size());
        assertNotEquals(beforeShuffle, afterShuffle);
    }

    @Test
    void testShuffleWithNullRandom() {
        deck.addCards(skipCard, 5);
        assertDoesNotThrow(() -> deck.shuffle(null));
    }

    @Test
    void testGetCards() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        
        List<Card> cards = deck.getCards();
        assertEquals(2, cards.size());
        assertNotSame(deck.getCards(), cards); // Should return a copy
    }

    @Test
    void testGetRealCards() {
        deck.addCard(skipCard);
        List<Card> realCards = deck.getRealCards();
        assertEquals(1, realCards.size());
        assertSame(deck.getRealCards(), realCards); // Should return the actual list
    }

    @Test
    void testGetUnmodifiableCards() {
        deck.addCard(skipCard);
        List<Card> unmodifiableCards = deck.getUnmodifiableCards();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCards.add(attackCard));
    }

    @Test
    void testRemoveTopCard() {
        deck.addCard(skipCard);
        Card removed = deck.removeTopCard();
        assertEquals(skipCard, removed);
        assertTrue(deck.isEmpty());
    }

    @Test
    void testRemoveTopCardEmptyDeck() {
        assertThrows(EmptyDeckException.class, () -> deck.removeTopCard());
    }

    @Test
    void testRemoveBottomCard() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        Card removed = deck.removeBottomCard();
        assertEquals(attackCard, removed);
        assertEquals(1, deck.size());
    }

    @Test
    void testRemoveBottomCardEmptyDeck() {
        assertThrows(EmptyDeckException.class, () -> deck.removeBottomCard());
    }

    @Test
    void testSwitchTopAndBottomHalf() {
        deck.addCards(skipCard, 3);
        deck.addCards(attackCard, 3);
        List<Card> beforeSwitch = new ArrayList<>(deck.getCards());
        
        deck.switchTopAndBottomHalf();
        List<Card> afterSwitch = deck.getCards();
        
        assertEquals(beforeSwitch.size(), afterSwitch.size());
        assertNotEquals(beforeSwitch, afterSwitch);
    }

    @Test
    void testSwitchTopAndBottomHalfOddSize() {
        deck.addCards(skipCard, 3);
        deck.addCards(attackCard, 2);
        List<Card> beforeSwitch = new ArrayList<>(deck.getCards());
        
        deck.switchTopAndBottomHalf();
        List<Card> afterSwitch = deck.getCards();
        
        assertEquals(beforeSwitch.size(), afterSwitch.size());
        assertNotEquals(beforeSwitch, afterSwitch);
    }

    @Test
    void testSwitchTopAndBottomHalfSmallDeck() {
        deck.addCard(skipCard);
        deck.switchTopAndBottomHalf();
        assertEquals(1, deck.size());
    }

    @Test
    void testClear() {
        deck.addCards(skipCard, 3);
        deck.clear();
        assertTrue(deck.isEmpty());
    }

    @Test
    void testClearUninitialized() {
        deck = new Deck();
        assertDoesNotThrow(() -> deck.clear());
    }

    @Test
    void testGetCardCounts() {
        deck.addCards(skipCard, 2);
        deck.addCards(attackCard, 3);
        deck.addCards(seeTheFutureCard, 1);
        
        Map<String, Integer> counts = deck.getCardCounts();
        // Debug output
        System.out.println("Actual counts:");
        counts.forEach((key, value) -> System.out.println(key + ": " + value));
        
        // Debug card types
        System.out.println("\nCard types in deck:");
        for (Card card : deck.getCards()) {
            System.out.println("Card class: " + card.getClass().getSimpleName());
            System.out.println("Card type: " + card.getType().name());
            if (card instanceof CatCard) {
                System.out.println("Cat type: " + ((CatCard) card).getCatType().name());
            }
            System.out.println("---");
        }
        
        assertEquals(2, counts.get("SkipCard"));
        assertEquals(3, counts.get("AttackCard"));
        assertEquals(1, counts.get("See_the_futureCard"));
    }

    @Test
    void testAddExplodingKittens() {
        deck.addExplodingKittens(3);
        assertEquals(3, deck.size());
        Map<String, Integer> counts = deck.getCardCounts();
        // Debug output
        System.out.println("Actual counts:");
        counts.forEach((key, value) -> System.out.println(key + ": " + value));
        
        // Debug card types
        System.out.println("\nCard types in deck:");
        for (Card card : deck.getCards()) {
            System.out.println("Card class: " + card.getClass().getSimpleName());
            System.out.println("Card type: " + card.getType().name());
            if (card instanceof CatCard) {
                System.out.println("Cat type: " + ((CatCard) card).getCatType().name());
            }
            System.out.println("---");
        }
        
        assertEquals(3, counts.get("Exploding_kittenCard"));
    }

    @Test
    void testAddExplodingKittensInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addExplodingKittens(-1));
    }

    @Test
    void testGetCardTypeKey() {
        assertEquals("CatCard_CAT_CARD", invokeGetCardTypeKey(new CatCard(CatType.TACOCAT)));
        assertEquals("DefuseCard", invokeGetCardTypeKey(new DefuseCard()));
        assertEquals("AttackCard", invokeGetCardTypeKey(new AttackCard()));
        assertEquals("SkipCard", invokeGetCardTypeKey(new SkipCard()));
        assertEquals("ShuffleCard", invokeGetCardTypeKey(new ShuffleCard()));
        assertEquals("SeeTheFutureCard", invokeGetCardTypeKey(new SeeTheFutureCard()));
        assertEquals("NopeCard", invokeGetCardTypeKey(new NopeCard()));
        assertEquals("ExplodingKittenCard", invokeGetCardTypeKey(new ExplodingKittenCard()));
        assertEquals("UnknownCard", invokeGetCardTypeKey(new Card(CardType.FAVOR) {
            public Card clone() { 
                return this; 
            }
            public void effect(java.util.List<Player> t, Deck d) {
                // Empty effect
            }
        }));
    }

    private String invokeGetCardTypeKey(Card card) {
        try {
            java.lang.reflect.Method m = Deck.class.getDeclaredMethod("getCardTypeKey", Card.class);
            m.setAccessible(true);
            return (String) m.invoke(deck, card);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
