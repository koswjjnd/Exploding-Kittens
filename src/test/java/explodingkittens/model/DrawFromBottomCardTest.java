package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

class DrawFromBottomCardTest {
    private Deck deck;
    private Player player;
    private List<Player> turnOrder;
    private DrawFromBottomCard card;

    @BeforeEach
    void setUp() {
        deck = mock(Deck.class);
        player = mock(Player.class);
        turnOrder = new ArrayList<>();
        turnOrder.add(player);
        card = new DrawFromBottomCard();
    }

    @Test
    void testDrawFromEmptyDeckThrows() {
        when(deck.getCards()).thenReturn(new ArrayList<>());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, deck));
    }

    @Test
    void testDrawFromDeckWithOneCard() {
        Card bottom = mock(Card.class);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(bottom);
        when(deck.getCards()).thenReturn(cards);
        doNothing().when(player).receiveCard(bottom);
        card.effect(turnOrder, deck);
        assertEquals(0, cards.size());
        verify(player).receiveCard(bottom);
    }

    @Test
    void testDrawFromDeckWithMultipleCards() {
        Card c1 = mock(Card.class);
        Card c2 = mock(Card.class);
        Card c3 = mock(Card.class);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(c1); cards.add(c2); cards.add(c3);
        when(deck.getCards()).thenReturn(cards);
        doNothing().when(player).receiveCard(c3);
        card.effect(turnOrder, deck);
        assertEquals(2, cards.size());
        assertFalse(cards.contains(c3));
        verify(player).receiveCard(c3);
    }

    @Test
    void testNullTurnOrderThrows() {
        assertThrows(IllegalArgumentException.class, () -> card.effect(null, deck));
    }

    @Test
    void testEmptyTurnOrderThrows() {
        assertThrows(IllegalArgumentException.class, () -> card.effect(new ArrayList<>(), deck));
    }

    @Test
    void testNullDeckThrows() {
        assertThrows(IllegalArgumentException.class, () -> card.effect(turnOrder, null));
    }
} 