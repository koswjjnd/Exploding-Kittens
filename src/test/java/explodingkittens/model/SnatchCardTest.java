package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SnatchCardTest {
    
    private SnatchCard snatchCard;
    private List<Player> turnOrder;
    private Deck mockDeck;
    
    @Mock
    private Player mockCurrentPlayer;
    
    @Mock
    private Player mockTargetPlayer;
    
    @Mock
    private Card mockCard;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDeck = new Deck();
        turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer);
        snatchCard = new SnatchCard();
    }
    
    /**
     * Test Case 1: targetPlayer has empty hand
     * Expected: IllegalStateException
     * 
     * This test verifies that when the target player has an empty hand,
     * an IllegalStateException is thrown.
     */
    @Test
    void testEffectWithEmptyHand() {
        // Arrange
        when(mockTargetPlayer.getHand()).thenReturn(new ArrayList<>());
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            snatchCard.effect(turnOrder, mockDeck),
            "Should throw IllegalStateException when target player has empty hand"
        );
    }
} 