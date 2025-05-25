package explodingkittens.model;

import explodingkittens.view.FavorCardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
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
    
    @Mock
    private Card mockCard1;
    
    @Mock
    private Card mockCard2;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        favorCard = new explodingkittens.model.card.FavorCard();
        favorCard.setView(mockView);
        when(mockCurrentPlayer.getName()).thenReturn("CurrentPlayer");
        when(mockTargetPlayer.getName()).thenReturn("TargetPlayer");
        when(mockCard1.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard2.getType()).thenReturn(CardType.CAT_CARD);
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

    /**
     * Test Case 2: turnOrder = 2 players, targetPlayer has 1 card
     * Expected: Card transferred to current player
     * 
     * This test verifies that when the target player has one card,
     * the card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithOneCard() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        when(mockTargetPlayer.getHand()).thenReturn(targetHand);
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(0);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard1);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 3: turnOrder = 2 players, targetPlayer has 2 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when the target player has two cards,
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithTwoCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        when(mockTargetPlayer.getHand()).thenReturn(targetHand);
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(1); // Select second card
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard2);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }
} 