package explodingkittens.model;

import explodingkittens.controller.GameContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;

class ReverseCardTest {
    private Player player1;
    private Player player2;
    private Player player3;
    private Deck deck;
    private List<Player> turnOrder;
    private ReverseCard card;
    private MockedStatic<GameContext> mockedGameContext;

    @BeforeEach
    void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        deck = mock(Deck.class);
        turnOrder = new ArrayList<>();
        card = new ReverseCard();
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
    void testEffectWithNullTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            card.effect(null, deck);
        });
    }

    @Test
    void testEffectWithEmptyTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            card.effect(new ArrayList<>(), deck);
        });
    }

    @Test
    void testEffectWithSinglePlayer() {
        // Setup
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player1));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1).setLeftTurns(0);
    }

    @Test
    void testEffectWithMultiplePlayers() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        when(player3.isAlive()).thenReturn(true);
        when(player3.getName()).thenReturn("Player3");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder
        ).thenReturn(List.of(player1, player2, player3));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())
        ).thenAnswer(invocation -> {
            List<Player> newOrder = invocation.getArgument(0);
            turnOrder.clear();
            turnOrder.addAll(newOrder);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(3, turnOrder.size());
        assertSame(player3, turnOrder.get(0));
        assertSame(player2, turnOrder.get(1));
        assertSame(player1, turnOrder.get(2));
        verify(player1).setLeftTurns(0);
    }

    @Test
    void testEffectWithAttackedPlayer() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(2);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player1, player2));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenAnswer(invocation -> {
            List<Player> newOrder = invocation.getArgument(0);
            turnOrder.clear();
            turnOrder.addAll(newOrder);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(2, turnOrder.size());
        assertSame(player2, turnOrder.get(0));
        assertSame(player1, turnOrder.get(1));
        verify(player1).decrementLeftTurns();
    }
} 