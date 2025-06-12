package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import explodingkittens.exceptions.EmptyDeckException;

/**
 * Test class for the Deck class.
 */
public class DeckTest {
    private Deck deck;
    private Card skipCard;
    private Card attackCard;
    private Card seeTheFutureCard;
    private Card nopeCard;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        skipCard = new SkipCard();
        attackCard = new AttackCard();
        seeTheFutureCard = new SeeTheFutureCard();
        nopeCard = new NopeCard();
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
        
        assertEquals(3, counts.get("DefuseCard")); // 5-2
        assertEquals(4, counts.get("NopeCard")); // Fixed number of Nope cards
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
    void testGetCards() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        
        List<Card> cards = deck.getCards();
        assertEquals(2, cards.size());
        assertNotSame(deck.getCards(), cards); // Should return a copy
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
        assertEquals(2, counts.get("SkipCard"));
        assertEquals(3, counts.get("AttackCard"));
    }

    @Test
    void testAddExplodingKittensInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addExplodingKittens(-1));
    }

    @Test
    void testValidateDeckWithInvalidMainCards() {
        deck.initializeBaseDeck(2);
        deck.addCards(new AttackCard(), 2); // Should be 3
        assertFalse(deck.validateDeck(2));
    }

    @Test
    void testValidateDeckWithInvalidCatCards() {
        deck.initializeBaseDeck(2);
        deck.addCards(new CatCard(CatType.TACOCAT), 3); // Should be 4
        assertFalse(deck.validateDeck(2));
    }

    @Test
    void testAddNopeCard() {
        deck.addCard(nopeCard);
        assertEquals(1, deck.size());
        assertEquals(nopeCard, deck.peekTop());
        
        Map<String, Integer> counts = deck.getCardCounts();
        assertEquals(1, counts.get("NopeCard"));
    }
}
