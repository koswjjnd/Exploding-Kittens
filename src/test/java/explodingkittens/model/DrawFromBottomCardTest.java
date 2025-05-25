package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
} 