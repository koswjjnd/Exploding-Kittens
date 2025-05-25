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

    
}
