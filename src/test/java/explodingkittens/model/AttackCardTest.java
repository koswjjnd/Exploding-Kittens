package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for AttackCard.
 */
class AttackCardTest {
    
    @Mock
    private Player currentPlayer;
    
    @Mock
    private Player nextPlayer;
    
    /**
     * BVA Test Case 1: turnOrder = empty list
     * Expected: IllegalArgumentException
     */
    @Test
    void testEmptyTurnOrder() {
        AttackCard attackCard = new AttackCard();
        List<Player> emptyTurnOrder = new ArrayList<>();
        Deck gameDeck = new Deck();
        
        assertThrows(IllegalArgumentException.class, () -> 
            attackCard.effect(emptyTurnOrder, gameDeck));
    }

    /**
     * BVA Test Case 2: turnOrder = multiple players, current player's left turns = 0
     * Expected: Next player's left turns = 2
     */
    @Test
    void testZeroLeftTurns() {
        MockitoAnnotations.openMocks(this);
        AttackCard attackCard = new AttackCard();
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(currentPlayer);
        turnOrder.add(nextPlayer);
        Deck gameDeck = new Deck();

        // Set up current player with 0 left turns
        when(currentPlayer.getLeftTurns()).thenReturn(0);

        // Execute effect
        attackCard.effect(turnOrder, gameDeck);

        // Verify next player's left turns is set to 2
        verify(nextPlayer).setLeftTurns(2);
    }

    /**
     * BVA Test Case 3: turnOrder = multiple players, current player's left turns = 1
     * Expected: Next player's left turns = 3
     */
    @Test
    void testOneLeftTurn() {
        MockitoAnnotations.openMocks(this);
        AttackCard attackCard = new AttackCard();
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(currentPlayer);
        turnOrder.add(nextPlayer);
        Deck gameDeck = new Deck();

        // Set up current player with 1 left turn
        when(currentPlayer.getLeftTurns()).thenReturn(1);

        // Execute effect
        attackCard.effect(turnOrder, gameDeck);

        // Verify next player's left turns is set to 3
        verify(nextPlayer).setLeftTurns(3);
    }
} 