package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DeckSwitchTopAndBottomHalfTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testSwitchTopAndBottomHalf_emptyDeck() {
        deck.switchTopAndBottomHalf();
        assertTrue(deck.getCards().isEmpty(), "Empty deck remains empty after switch");
    }

    @Test
    public void testSwitchTopAndBottomHalf_oneCard() {
        Card card = new AttackCard();
        deck.addCard(card);

        deck.switchTopAndBottomHalf();

        assertEquals(List.of(card), deck.getCards(), "One card deck should remain unchanged");
    }

    @Test
    public void testSwitchTopAndBottomHalf_twoCards() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        deck.addCard(card1);
        deck.addCard(card2);

        deck.switchTopAndBottomHalf();

        List<Card> cards = deck.getCards();
        assertEquals(card2, cards.get(0), "Cards should be swapped");
        assertEquals(card1, cards.get(1), "Cards should be swapped");
    }

    @Test
    public void testSwitchTopAndBottomHalf_evenSizeDeck() {
        // Deck: [A, B, C, D]
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        Card card3 = new DefuseCard();
        Card card4 = new ShuffleCard();
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);

        deck.switchTopAndBottomHalf();

        // Expect: [C, D, A, B]
        List<Card> cards = deck.getCards();
        assertEquals(List.of(card3, card4, card1, card2), cards, "Even size deck should swap top/bottom half");
    }

    @Test
    public void testSwitchTopAndBottomHalf_oddSizeDeck() {
        // Deck: [A, B, C, D, E]
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

        deck.switchTopAndBottomHalf();

        // Expect: [D, E, C, A, B]
        List<Card> cards = deck.getCards();
        assertEquals(card4, cards.get(0));
        assertEquals(card5, cards.get(1));
        assertEquals(card3, cards.get(2), "Middle card stays in place");
        assertEquals(card1, cards.get(3));
        assertEquals(card2, cards.get(4));
    }
}
