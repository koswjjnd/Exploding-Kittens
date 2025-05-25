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

    
}
