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

    
}
