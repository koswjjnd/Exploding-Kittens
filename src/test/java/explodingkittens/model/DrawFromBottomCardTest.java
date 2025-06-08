package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.model.Card;
import explodingkittens.model.DrawFromBottomCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;

class DrawFromBottomCardTest {
    private Player player;
    private Deck deck;
    private List<Player> turnOrder;
    private DrawFromBottomCard card;
    private MockedStatic<GameContext> mockedGameContext;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        deck = mock(Deck.class);
        turnOrder = new ArrayList<>();
        turnOrder.add(player);
        card = new DrawFromBottomCard();
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
        mockedGameContext = Mockito.mockStatic(GameContext.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
    }

    @Test
    void testDrawFromEmptyDeckThrows() {
        when(deck.getCards()).thenReturn(new ArrayList<>());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, deck));
    }

    @Test
    void testDrawFromDeckWithOneCard() {
        // Setup
        Card bottom = mock(Card.class);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(bottom);
        when(deck.getCards()).thenReturn(cards);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(0, cards.size());
        verify(player).receiveCard(bottom);
    }

    @Test
    void testDrawFromDeckWithMultipleCards() {
        // Setup
        Card c1 = mock(Card.class);
        Card c2 = mock(Card.class);
        Card c3 = mock(Card.class);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        when(deck.getCards()).thenReturn(cards);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
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