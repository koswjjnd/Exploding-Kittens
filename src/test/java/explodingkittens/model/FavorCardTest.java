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
    private Player mockTargetPlayer1;
    
    @Mock
    private Player mockTargetPlayer2;
    
    @Mock
    private Deck mockDeck;
    
    @Mock
    private FavorCardView mockView;
    
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
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        favorCard = new explodingkittens.model.card.FavorCard();
        favorCard.setView(mockView);
        when(mockCurrentPlayer.getName()).thenReturn("CurrentPlayer");
        when(mockTargetPlayer1.getName()).thenReturn("TargetPlayer1");
        when(mockTargetPlayer2.getName()).thenReturn("TargetPlayer2");
        when(mockCard1.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard2.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard3.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard4.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard5.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard6.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard7.getType()).thenReturn(CardType.CAT_CARD);
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
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1);
        when(mockTargetPlayer1.getHand()).thenReturn(List.of());
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
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1);
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
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithTwoCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1);
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
     * the card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayers() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
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
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndTwoCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
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
     * Test Case 6: turnOrder = 3 players, targetPlayer has 3 cards
     * Expected: Card transferred to current player
     * 
     * This test verifies that when there are three players and the target player has three cards,
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndThreeCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
        when(mockView.promptCardSelection(anyList())).thenReturn(2); // Select third card
        
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
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndFourCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
        when(mockView.promptCardSelection(anyList())).thenReturn(3); // Select fourth card
        
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
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndFiveCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        targetHand.add(mockCard5);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
        when(mockView.promptCardSelection(anyList())).thenReturn(4); // Select fifth card
        
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
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndSixCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
        List<Card> targetHand = new ArrayList<>();
        targetHand.add(mockCard1);
        targetHand.add(mockCard2);
        targetHand.add(mockCard3);
        targetHand.add(mockCard4);
        targetHand.add(mockCard5);
        targetHand.add(mockCard6);
        when(mockTargetPlayer1.getHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
        when(mockView.promptCardSelection(anyList())).thenReturn(5); // Select sixth card
        
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
     * the selected card is successfully transferred to the current player.
     */
    @Test
    void testEffectWithThreePlayersAndSevenCards() {
        // Arrange
        List<Player> turnOrder = Arrays.asList(mockCurrentPlayer, mockTargetPlayer1, mockTargetPlayer2);
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
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0); // Select first target player
        when(mockView.promptCardSelection(anyList())).thenReturn(6); // Select seventh card
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getHand();
        verify(mockCurrentPlayer).receiveCard(mockCard7);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }
} 