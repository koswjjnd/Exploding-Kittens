package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SwitchDeckByHalfCardTest {

    private Deck deck;
    private SwitchDeckByHalfCard switchDeckByHalfCard;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
        switchDeckByHalfCard = new SwitchDeckByHalfCard();
    }

    @Test
    public void testEffect_nullDeck() {
        assertThrows(IllegalArgumentException.class, () -> {
            switchDeckByHalfCard.effect(new ArrayList<>(), null);
        }, "Should throw IllegalArgumentException for null deck");
    }

    @Test
    public void testEffect_emptyDeck() {
        switchDeckByHalfCard.effect(new ArrayList<>(), deck);
        assertTrue(deck.getCards().isEmpty(), "Empty deck should remain unchanged");
    }

    @Test
    public void testEffect_oneCard() {
        Card card = new AttackCard();
        deck.addCard(card);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        assertEquals(List.of(card), deck.getCards(), "One card deck should remain unchanged");
    }

    @Test
    public void testEffect_twoCards() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        deck.addCard(card1);
        deck.addCard(card2);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        List<Card> cards = deck.getCards();
        assertEquals(card2, cards.get(0));
        assertEquals(card1, cards.get(1));
    }

    @Test
    public void testEffect_evenSizeDeck() {
        // Deck: [A, B, C, D]
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        Card card3 = new DefuseCard();
        Card card4 = new ShuffleCard();
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);

        switchDeckByHalfCard.effect(new ArrayList<>(), deck);

        // Expect: [C, D, A, B]
        List<Card> cards = deck.getCards();
        assertEquals(List.of(card3, card4, card1, card2), cards, "Even size deck should swap top/bottom half");
    }

    
}
