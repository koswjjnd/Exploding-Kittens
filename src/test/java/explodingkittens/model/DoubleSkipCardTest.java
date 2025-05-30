package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoubleSkipCardTest {
    private DoubleSkipCard doubleSkipCard;
    private List<Player> turnOrder;
    
    @Mock
    private Deck gameDeck;
    
    @Mock
    private Player player1;
    
    @Mock
    private Player player2;

    @BeforeEach
    void setUp() {
        doubleSkipCard = new DoubleSkipCard();
        turnOrder = new ArrayList<>();
    }

    @Test
    void testEffectWithZeroLeftTurns() {
        // Test Case 1: leftTurn = 0
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> {
            doubleSkipCard.effect(turnOrder, gameDeck);
        });
        
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1, never()).setLeftTurns(anyInt());
    }

    @Test
    void testEffectWithOneLeftTurn() {
        // Test Case 2: leftTurn = 1
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(1);
        
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player1, turnOrder.get(1));
        verify(player1).setLeftTurns(0);
    }

    @Test
    void testEffectWithTwoLeftTurns() {
        // Test Case 3: leftTurn = 2
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(2);
        
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player1, turnOrder.get(1));
        verify(player1).setLeftTurns(0);
    }
    
    @Test
    void testEffectWithThreeLeftTurns() {
        // Test Case 4: leftTurn = 3
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(3);
        
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        assertEquals(player2, turnOrder.get(1));
        verify(player1).setLeftTurns(1);
    }

    @Test
    void testEffectWithNegativeLeftTurns() {
        // Test Case 5: leftTurn = -1
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(-1);
        
        doubleSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1).setLeftTurns(0);
    }
    
} 