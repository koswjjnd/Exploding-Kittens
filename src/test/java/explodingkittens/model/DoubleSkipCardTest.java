package explodingkittens.model;

import explodingkittens.controller.GameContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyInt;

class DoubleSkipCardTest {
    private DoubleSkipCard doubleSkipCard;
    private List<Player> turnOrder;
    private MockedStatic<GameContext> mockedGameContext;
    
    @Mock
    private Deck gameDeck;
    
    @Mock
    private Player player1;
    
    @Mock
    private Player player2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doubleSkipCard = new DoubleSkipCard();
        turnOrder = new ArrayList<>();
        
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
    void testEffectWithZeroLeftTurns() {
        // Setup
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(0);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute and Verify
        assertThrows(IllegalStateException.class, () -> {
            doubleSkipCard.effect(turnOrder, gameDeck);
        });
        
        assertEquals(1, turnOrder.size());
        assertSame(player1, turnOrder.get(0));
        verify(player1, never()).setLeftTurns(anyInt());
    }

    @Test
    void testEffectWithOneLeftTurn() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.movePlayerToEnd(player1)
        ).thenAnswer(invocation -> null);
        
        // Execute
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(player1).setLeftTurns(0);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(player1));
    }

    @Test
    void testEffectWithTwoLeftTurns() {
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
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.movePlayerToEnd(player1)
        ).thenAnswer(invocation -> null);
        
        // Execute
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(player1).setLeftTurns(0);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(player1));
    }
    
    @Test
    void testEffectWithThreeLeftTurns() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(3);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(player1).setLeftTurns(1);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(player1), never());
    }

    @Test
    void testEffectWithNegativeLeftTurns() {
        // Setup
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(-1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute and Verify
        assertThrows(IllegalStateException.class, () -> {
            doubleSkipCard.effect(turnOrder, gameDeck);
        });
        
        assertEquals(1, turnOrder.size());
        assertSame(player1, turnOrder.get(0));
        verify(player1, never()).setLeftTurns(anyInt());
    }

    @Test
    void testEffectWithMaxValueLeftTurns() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(Integer.MAX_VALUE);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player1);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(player1).setLeftTurns(Integer.MAX_VALUE - 2);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(player1), never());
    }
} 