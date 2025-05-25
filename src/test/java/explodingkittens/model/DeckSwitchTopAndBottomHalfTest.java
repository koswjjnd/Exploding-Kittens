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

    
}
