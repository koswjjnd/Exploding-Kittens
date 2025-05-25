package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for SwitchDeckByHalfCard.
 * Tests the functionality of switching the top and bottom halves of a deck.
 */
public class SwitchDeckByHalfCardTest {

    private Deck deck;
    private SwitchDeckByHalfCard switchDeckByHalfCard;

    /**
     * Sets up the test environment before each test.
     * Initializes a new deck and a SwitchDeckByHalfCard instance.
     */
    @BeforeEach
    public void setUp() {
        deck = new Deck();
        switchDeckByHalfCard = new SwitchDeckByHalfCard();
    }

    /**
     * Tests that an IllegalArgumentException is thrown when a null deck is provided.
     */
    @Test
    public void testEffectNullDeck() {
        assertThrows(IllegalArgumentException.class, () -> {
            switchDeckByHalfCard.effect(new ArrayList<>(), null);
        }, "Should throw IllegalArgumentException for null deck");
    }

    /**
     * Tests that an empty deck remains unchanged after the effect.
     */
    @Test
    public void testEffectEmptyDeck() {
        switchDeckByHalfCard.effect(new ArrayList<>(), deck);
        assertTrue(deck.getCards().isEmpty(), "Empty deck should remain unchanged");
    }

    /**
     * Tests that a deck with one card remains unchanged after the effect.
     */
    @Test
    public void testEffectOneCard() {
        Card card = new AttackCard();
        deck.addCard(card);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        assertEquals(List.of(card), deck.getCards(), "One card deck should remain unchanged");
    }

    /**
     * Tests that a deck with two cards swaps their positions after the effect.
     */
    @Test
    public void testEffectTwoCards() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        deck.addCard(card1);
        deck.addCard(card2);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        List<Card> cards = deck.getCards();
        assertEquals(card2, cards.get(0));
        assertEquals(card1, cards.get(1));
    }

    /**
     * Tests that a deck with even number of cards correctly swaps its halves.
     * Initial deck: [A, B, C, D]
     * Expected result: [C, D, A, B]
     */
    @Test
    public void testEffectEvenSizeDeck() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        Card card3 = new DefuseCard();
        Card card4 = new ShuffleCard();
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        List<Card> cards = deck.getCards();
        assertEquals(List.of(card3, card4, card1, card2), cards, 
            "Even size deck should swap top/bottom half");
    }

    /**
     * Tests that a deck with odd number of cards correctly swaps its halves
     * while keeping the middle card in place.
     * Initial deck: [A, B, C, D, E]
     * Expected result: [D, E, C, A, B]
     */
    @Test
    public void testEffectOddSizeDeck() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        Card card3 = new DefuseCard();
        Card card4 = new ShuffleCard();
        Card card5 = new SeeTheFutureCard();
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);
        deck.addCard(card5);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        List<Card> cards = deck.getCards();
        assertEquals(card4, cards.get(0));
        assertEquals(card5, cards.get(1));
        assertEquals(card3, cards.get(2), "Middle card stays in place");
        assertEquals(card1, cards.get(3));
        assertEquals(card2, cards.get(4));
    }
}
