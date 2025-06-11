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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavorCardTest {
    
    private explodingkittens.model.FavorCard favorCard;
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
        favorCard = new explodingkittens.model.FavorCard();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(new ArrayList<>());
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(0);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(1); // Select second card
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(0);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(1);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(2);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(3);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(4);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(5);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
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
        when(mockTargetPlayer1.getRealHand()).thenReturn(targetHand);
        when(mockTargetPlayer2.getRealHand()).thenReturn(new ArrayList<>());
        when(mockView.promptTargetPlayer(anyList())).thenReturn(0);
        when(mockView.promptCardSelection(anyList())).thenReturn(6);
        
        // Act
        favorCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockTargetPlayer1).getRealHand();
        verify(mockCurrentPlayer).receiveCard(mockCard7);
        verify(mockView).promptTargetPlayer(anyList());
        verify(mockView).promptCardSelection(anyList());
    }

    @Test
    void testGetViewReturnsNewInstance() {
        FavorCard card = new FavorCard();
        FavorCardView view1 = card.getView();
        FavorCardView view2 = card.getView();
        
        assertNotSame(view1, view2, "getView should return a new instance each time");
    }

    @Test
    void testConstructorCreatesNewView() {
        FavorCard card = new FavorCard();
        FavorCardView view = card.getView();
        
        assertNotNull(view, "Constructor should create a new view");
    }

    @Test
    void testEffectWithNullTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> 
            favorCard.effect(null, mockDeck),
            "Should throw IllegalArgumentException when turnOrder is null"
        );
    }

    @Test
    void testEffectWithEmptyTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> 
            favorCard.effect(new ArrayList<>(), mockDeck),
            "Should throw IllegalArgumentException when turnOrder is empty"
        );
    }

    @Test
    void testProtectedConstructor() {
        FavorCardView view = new FavorCardView();
        FavorCard favorCard = new FavorCard(view) {};
        assertNotNull(favorCard, "FavorCard should be created successfully");
        assertEquals(CardType.FAVOR, favorCard.getType(), "Card type should be FAVOR");
    }

    @Test
    void testEffectWithTargetPlayerHavingCards() {
        // Create players
        Player currentPlayer = new Player("Current");
        Player targetPlayer = new Player("Target");
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);

        // Add a card to target player's hand
        SkipCard skipCard = new SkipCard();
        targetPlayer.receiveCard(skipCard);

        // Create a new view for each input
        FavorCardView targetSelectionView = new FavorCardView();
        targetSelectionView.setUserInput("0");
        FavorCardView cardSelectionView = new FavorCardView();
        cardSelectionView.setUserInput("0");

        // Create a favor card with the first view
        FavorCard favorCardWithTargetView = new FavorCard(targetSelectionView);
        
        // Execute effect
        favorCardWithTargetView.effect(turnOrder, mockDeck);

        // Verify card was transferred to current player
        List<Card> currentHand = currentPlayer.getHand();
        assertEquals(1, currentHand.size(), "Current player should have one card");
        assertTrue(currentHand.get(0) instanceof SkipCard,
                "Current player should have received the skip card");
    }

    @Test
    void testEffectWithTargetPlayerHavingNoCards() {
        // 设置目标玩家没有卡牌
        mockTargetPlayer1.getHand().clear();
        
        // 验证当目标玩家没有卡牌时抛出 IllegalStateException
        assertThrows(IllegalStateException.class, 
            () -> favorCard.effect(turnOrder, mockDeck),
            "Should throw IllegalStateException when target player has no cards"
        );
    }
} 