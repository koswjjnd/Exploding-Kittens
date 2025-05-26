package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import explodingkittens.view.FavorCardView;

class SnatchCardTest {
    
    private SnatchCard snatchCard;
    private List<Player> turnOrder;
    private Deck mockDeck;
    
    @Mock
    private Player mockCurrentPlayer;
    
    @Mock
    private Player mockTargetPlayer;
    
    @Mock
    private Card mockCard1;
    
    @Mock
    private Card mockCard2;
    
    @Mock
    private Card mockCard3;
    
    @Mock
    private FavorCardView mockFavorCardView;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDeck = new Deck();
        turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer);
        snatchCard = new SnatchCard();
        
        // Inject mock FavorCardView using reflection
        try {
            java.lang.reflect.Field viewField = 
                snatchCard.getClass().getDeclaredField("favorCardView");
            viewField.setAccessible(true);
            viewField.set(snatchCard, mockFavorCardView);
        } 
        catch (Exception e) {
            throw new RuntimeException("Failed to set mock view", e);
        }
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
        when(mockFavorCardView.promptTargetPlayer(anyList())).thenReturn(0);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            snatchCard.effect(turnOrder, mockDeck),
            "Should throw IllegalStateException when target player has empty hand"
        );
    }

    /**
     * Test Case 2: targetPlayer has multiple cards
     * Expected: Random card transferred to current player
     * 
     * This test verifies that when the target player has multiple cards,
     * a random card is transferred to the current player.
     */
    @Test
    void testEffectWithCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        when(mockTargetPlayer.getHand()).thenReturn(targetHand);
        when(mockFavorCardView.promptTargetPlayer(anyList())).thenReturn(0);
        
        // Act
        snatchCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer).getHand();
        verify(mockCurrentPlayer).receiveCard(any(Card.class));
        assert targetHand.size() == 2 : 
            "Target player should have 2 cards after snatch";
    }
} 