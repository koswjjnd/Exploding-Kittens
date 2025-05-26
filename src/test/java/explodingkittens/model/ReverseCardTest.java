package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class ReverseCardTest {
    private ReverseCard card;
    private List<Player> turnOrder;
    private Player player1;
    private Player player2;
    private Player player3;
    private Deck deck;

    @BeforeEach
    void setUp() {
        card = new ReverseCard();
        turnOrder = new ArrayList<>();
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        deck = mock(Deck.class);
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
    void testSinglePlayerReversesOrder() {
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(1);
        card.effect(turnOrder, deck);
        assertEquals(player1, turnOrder.get(0));
        verify(player1).setLeftTurns(0);
    }

    @Test
    void testMultiplePlayersReversesOrder() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        when(player1.getLeftTurns()).thenReturn(1);
        card.effect(turnOrder, deck);
        assertEquals(player3, turnOrder.get(0));
        assertEquals(player2, turnOrder.get(1));
        assertEquals(player1, turnOrder.get(2));
        verify(player1, times(1)).setLeftTurns(0);
    }
} 