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
    void testEffectWithSinglePlayer() {
        // Test Case 1: turnOrder size = 1
        turnOrder.add(player1);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
    }

	@Test
    void testEffectWithTwoPlayers() {
        // Test Case 2: turnOrder size = 2
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(2, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player1, turnOrder.get(1));
    }

	@Test
    void testEffectWithThreePlayers() {
        // Test Case 3: turnOrder size = 3
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        
        superSkipCard.effect(turnOrder, gameDeck);
        
        assertEquals(3, turnOrder.size());
        assertEquals(player2, turnOrder.get(0));
        assertEquals(player3, turnOrder.get(1));
        assertEquals(player1, turnOrder.get(2));
    }

	@Test
    void testEffectWithEmptyTurnOrder() {
        // Test Case 4: turnOrder size = 0
        assertThrows(IndexOutOfBoundsException.class, () -> {
            superSkipCard.effect(turnOrder, gameDeck);
        });
    }
	
} 