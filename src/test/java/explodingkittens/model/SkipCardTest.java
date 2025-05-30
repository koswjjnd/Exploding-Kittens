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
class SkipCardTest {
    private SkipCard skipCard;
    private List<Player> turnOrder;
    
    @Mock
    private Deck gameDeck;
    
    @Mock
    private Player player1;
    
    @Mock
    private Player player2;

    @BeforeEach
    void setUp() {
        skipCard = new SkipCard();
        turnOrder = new ArrayList<>();
        turnOrder.add(player1);
        turnOrder.add(player2);
    }

    @Test
    void testEffectWithLeftTurnsZero() {
        // Test Case 1: leftTurns = 0 (no turns left)
        when(player1.getLeftTurns()).thenReturn(0);
        
        skipCard.effect(turnOrder, gameDeck);
        
        verify(player1, never()).setLeftTurns(anyInt());
        assertEquals(player1, turnOrder.get(0));
        assertEquals(player2, turnOrder.get(1));
    }

	@Test
    void testEffectWithLeftTurnsOne() {
        // Test Case 2: leftTurns = 1 (will become 0 and skip)
        when(player1.getLeftTurns()).thenReturn(1);
        doAnswer(invocation -> {
            when(player1.getLeftTurns()).thenReturn(0);
            return null;
        }).when(player1).setLeftTurns(0);
        
        skipCard.effect(turnOrder, gameDeck);
        
        verify(player1).setLeftTurns(0);
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player1, turnOrder.get(1));
    }

	@Test
    void testEffectWithLeftTurnsTwo() {
        // Test Case 3: leftTurns = 2 (will become 1, no skip)
        when(player1.getLeftTurns()).thenReturn(2);
        doAnswer(invocation -> {
            when(player1.getLeftTurns()).thenReturn(1);
            return null;
        }).when(player1).setLeftTurns(1);
        
        skipCard.effect(turnOrder, gameDeck);
        
        verify(player1).setLeftTurns(1);
        assertEquals(player1, turnOrder.get(0));
        assertEquals(player2, turnOrder.get(1));
    }

}
