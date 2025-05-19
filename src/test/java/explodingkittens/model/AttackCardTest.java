package explodingkittens.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for AttackCard.
 */
class AttackCardTest {
    
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
} 