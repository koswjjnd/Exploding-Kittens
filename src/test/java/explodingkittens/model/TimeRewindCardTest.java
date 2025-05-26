package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeRewindCardTest {
    private TimeRewindCard card;
    private List<Player> turnOrder;
    private Deck deck;
    private List<Card> deckCards;

    @BeforeEach
    void setUp() {
        card = new TimeRewindCard();
        turnOrder = new ArrayList<>();
        deck = mock(Deck.class);
        deckCards = new ArrayList<>();
        when(deck.getCards()).thenReturn(deckCards);
    }

    @Test
    void testNullDeckThrows() {
        assertThrows(IllegalArgumentException.class, () -> 
            card.effect(turnOrder, null));
    }

    @Test
    void testEmptyDeckThrows() {
        assertThrows(IllegalStateException.class, () -> 
            card.effect(turnOrder, deck));
    }

    
} 
