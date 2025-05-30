package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuperSkipCardTest {
    private SuperSkipCard superSkipCard;
    private List<Player> turnOrder;
    
    @Mock
    private Deck gameDeck;
    
    @Mock
    private Player player1;
    
    @Mock
    private Player player2;
    
    @Mock
    private Player player3;

    @BeforeEach
    void setUp() {
        superSkipCard = new SuperSkipCard();
        turnOrder = new ArrayList<>();
    }

    @Test
    void testEffectWithEmptyTurnOrder() {
        // Test Case 0: Empty turnOrder
        assertThrows(IndexOutOfBoundsException.class, () -> {
            superSkipCard.effect(turnOrder, gameDeck);
        });
    }

	@Test
    void testEffectWithZeroLeftTurns() {
        // Test Case 1: leftTurn = 0
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> {
            superSkipCard.effect(turnOrder, gameDeck);
        });
        
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1, never()).setLeftTurns(anyInt());
    }

	@Test
    void testEffectWithNegativeLeftTurns() {
        // Test Case 2: leftTurn = -1
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(-1);
        
        assertThrows(IllegalStateException.class, () -> {
            superSkipCard.effect(turnOrder, gameDeck);
        });
        
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1, never()).setLeftTurns(anyInt());
    }

	@Test
    void testEffectWithSinglePlayer() {
        // Test Case 3: turnOrder size = 1, leftTurn > 0
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(1);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1).setLeftTurns(0);
    }

	@Test
    void testEffectWithTwoPlayers() {
        // Test Case 4: turnOrder size = 2, leftTurn > 0
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(1);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player1, turnOrder.get(1));
        verify(player1).setLeftTurns(0);
    }

	@Test
    void testEffectWithThreePlayers() {
        // Test Case 5: turnOrder size = 3, leftTurn > 0
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        when(player1.getLeftTurns()).thenReturn(1);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(3, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player3, turnOrder.get(1));
        assertEquals(player1, turnOrder.get(2));
        verify(player1).setLeftTurns(0);
    }

	@Test
    void testEffectWithMaxValueLeftTurns() {
        // Test Case 6: leftTurn = Integer.MAX_VALUE
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(Integer.MAX_VALUE);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player1, turnOrder.get(1));
        verify(player1).setLeftTurns(0);
    }

	@Test
    void testEffectWithLargeTurnOrder() {
        // Test Case 7: turnOrder size = 10
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Player player = mock(Player.class);
            if (i == 0) {
                when(player.getLeftTurns()).thenReturn(1);
            }
            players.add(player);
            turnOrder.add(player);
        }
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(10, turnOrder.size());
        // First player should be at the end
        assertEquals(players.get(0), turnOrder.get(9));
        // Second player should be at the front
        assertEquals(players.get(1), turnOrder.get(0));
        // Verify leftTurns was set to 0 for the first player
        verify(players.get(0)).setLeftTurns(0);
    }
	
} 