package explodingkittens.model;

import explodingkittens.view.FavorCardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FavorCardTest {
    
    private explodingkittens.model.card.FavorCard favorCard;
    
    @Mock
    private Player mockCurrentPlayer;
    
    @Mock
    private Player mockTargetPlayer;
    
    @Mock
    private Deck mockDeck;
    
    @Mock
    private FavorCardView mockView;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        favorCard = new explodingkittens.model.card.FavorCard();
        favorCard.setView(mockView);
        when(mockCurrentPlayer.getName()).thenReturn("CurrentPlayer");
        when(mockTargetPlayer.getName()).thenReturn("TargetPlayer");
    }
    
    /**
     * Test Case 1: turnOrder = 2 players, targetPlayer has empty hand
     * Expected: IllegalStateException
     * 
     * This test verifies that when the target player has an empty hand,
     * an IllegalStateException is thrown.
     */
    @Test
    void testEffectWithEmptyHand() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer);
        when(mockTargetPlayer.getHand()).thenReturn(List.of());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            favorCard.effect(turnOrder, mockDeck),
            "Should throw IllegalStateException when target player has empty hand"
        );
    }
} 