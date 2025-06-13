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
import static org.mockito.Mockito.mock;

class SkipCardTest {
    private Player player;
    private Deck deck;
    private List<Player> turnOrder;
    private SkipCard card;
    private MockedStatic<GameContext> mockedGameContext;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        deck = mock(Deck.class);
        turnOrder = new ArrayList<>();
        turnOrder.add(player);
        card = new SkipCard();
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
    void testEffectWithOneLeftTurn() {
        // Setup
        when(player.getLeftTurns()).thenReturn(1);
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.movePlayerToEnd(player)
        ).thenAnswer(invocation -> null);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        verify(player).setLeftTurns(0);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(player));
    }

    @Test
    void testEffectWithTwoLeftTurns() {
        // Test Case 3: leftTurn = 2
        turnOrder.add(player);
        when(player.getLeftTurns()).thenReturn(2);
        
        card.effect(turnOrder, deck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player, turnOrder.get(0));
        verify(player).setLeftTurns(1);
    }

    @Test
    void testEffectWithThreeLeftTurns() {
        // Test Case 4: leftTurn = 3
        turnOrder.add(player);
        when(player.getLeftTurns()).thenReturn(3);
        
        card.effect(turnOrder, deck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player, turnOrder.get(0));
        verify(player).setLeftTurns(2);
    }

    @Test
    void testEffectWithNegativeLeftTurns() {
        // Test Case 5: leftTurn = -1
        turnOrder.add(player);
        when(player.getLeftTurns()).thenReturn(-1);
        
        assertThrows(IllegalStateException.class, () -> {
            card.effect(turnOrder, deck);
        });
        
        assertEquals(2, turnOrder.size());
        assertEquals(player, turnOrder.get(0));
        verify(player, never()).setLeftTurns(anyInt());
    }

    @Test
    void testEffectWithMaxValueLeftTurns() {
        // Test Case 6: leftTurn = Integer.MAX_VALUE
        turnOrder.add(player);
        when(player.getLeftTurns()).thenReturn(Integer.MAX_VALUE);
        
        card.effect(turnOrder, deck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player, turnOrder.get(0));
        verify(player).setLeftTurns(Integer.MAX_VALUE - 1);
    }

    @Test
    void testEffectWithZeroLeftTurns() {
        // Test Case 7: leftTurn = 0 (boundary case)
        turnOrder.add(player);
        when(player.getLeftTurns()).thenReturn(0);
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> card.effect(turnOrder, deck),
            "Should throw IllegalStateException when player has exactly 0 turns left"
        );
        
        assertEquals(
            "Cannot use Skip card when you have no turns left. You must draw a card first.",
            exception.getMessage()
        );
        
        verify(player, never()).setLeftTurns(anyInt());
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(player), never());
    }
}
