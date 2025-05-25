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
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavorCardTest {
    
    private explodingkittens.model.card.FavorCard favorCard;
    private List<Player> turnOrder;
    private Deck mockDeck;
    
    @Mock
    private Player mockCurrentPlayer;
    
    @Mock
    private Player mockTargetPlayer1;
    
    @Mock
    private Player mockTargetPlayer2;
    
    @Mock
    private Card mockCard1;
    
    @Mock
    private Card mockCard2;
    
    @Mock
    private Card mockCard3;
    
    @Mock
    private Card mockCard4;
    
    @Mock
    private Card mockCard5;
    
    @Mock
    private Card mockCard6;
    
    @Mock
    private Card mockCard7;
    
    @Mock
    private FavorCardView mockView;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDeck = new Deck();
        turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        
        // Create a new FavorCard instance and inject mockView
        favorCard = new explodingkittens.model.card.FavorCard();
        // Use reflection to set the view field
        try {
            java.lang.reflect.Field viewField = favorCard.getClass().getDeclaredField("view");
            viewField.setAccessible(true);
            viewField.set(favorCard, mockView);
        } 
        catch (Exception e) {
            throw new RuntimeException("Failed to set mock view", e);
        }
    }
    
    /**
     * Test Case 1: turnOrder = 2 players, targetPlayer has empty hand
     * Expected: IllegalStateException
     * 
     * This test verifies that when the target player has an empty hand,
     * they cannot give any cards, and an IllegalStateException is thrown.
     */
    @Test
    void testEffectWithEmptyHand() {
        // Arrange
        when(mockTargetPlayer1.getHand()).thenReturn(new ArrayList<>());
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
     * they can give that card to the current player.
     */
    @Test
    void testEffectWithOneCard() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(0);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard1);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 3: turnOrder = 2 players, targetPlayer has 2 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when the target player has two cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithTwoCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(1); // Select second card
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard2);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 4: turnOrder = 3 players, targetPlayer has 1 card
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has one card,
     * they can give that card to the current player.
     */
    @Test
    void testEffectWithThreePlayers() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(0);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard1);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 5: turnOrder = 3 players, targetPlayer has 2 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has two cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndTwoCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(1);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard2);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 6: turnOrder = 3 players, targetPlayer has 3 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has three cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndThreeCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(2);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard3);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 7: turnOrder = 3 players, targetPlayer has 4 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has four cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndFourCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(3);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard4);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 8: turnOrder = 3 players, targetPlayer has 5 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has five cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndFiveCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        targetHand.add(mockCard5);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(4);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard5);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 9: turnOrder = 3 players, targetPlayer has 6 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has six cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndSixCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        targetHand.add(mockCard5);
        targetHand.add(mockCard6);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(5);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard6);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    /**
     * Test Case 10: turnOrder = 3 players, targetPlayer has 7 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has seven cards,
     * they can choose one to give to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndSevenCards() {
        // Arrange
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        targetHand.add(mockCard5);
        targetHand.add(mockCard6);
        targetHand.add(mockCard7);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(6);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard7);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }
} 