package explodingkittens.view;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FavorCardViewTest {
    
    private FavorCardView view;
    
    @Mock
    private Player mockPlayer1;
    
    @Mock
    private Player mockPlayer2;
    
    @Mock
    private Player mockPlayer3;
    
    @Mock
    private Card mockCard1;
    
    @Mock
    private Card mockCard2;
    
    @Mock
    private Card mockCard3;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        view = new FavorCardView();
        when(mockPlayer1.getName()).thenReturn("TestPlayer1");
        when(mockPlayer2.getName()).thenReturn("TestPlayer2");
        when(mockPlayer3.getName()).thenReturn("TestPlayer3");
        when(mockCard1.getType()).thenReturn(CardType.CAT_CARD);
        when(mockCard2.getType()).thenReturn(CardType.DEFUSE);
        when(mockCard3.getType()).thenReturn(CardType.ATTACK);
    }
    
    /**
     * Test Case 1: availablePlayers = 1 player, input = 0
     * Expected: Returns 0
     * 
     * This test verifies that when there is only one available player,
     * selecting index 0 returns the correct player index.
     */
    @Test
    void testPromptTargetPlayerWithOnePlayer() {
        // Arrange
        List<Player> availablePlayers = Arrays.asList(mockPlayer1);
        view.setUserInput("0");
        
        // Act
        int result = view.promptTargetPlayer(availablePlayers);
        
        // Assert
        assertEquals(0, result, "Should return 0 when there is only one player");
    }

    /**
     * Test Case 2: availablePlayers = 2 players, input = 1
     * Expected: Returns 1
     * 
     * This test verifies that when there are two available players,
     * selecting index 1 returns the correct player index.
     */
    @Test
    void testPromptTargetPlayerWithTwoPlayers() {
        // Arrange
        List<Player> availablePlayers = Arrays.asList(mockPlayer1, mockPlayer2);
        view.setUserInput("1");
        
        // Act
        int result = view.promptTargetPlayer(availablePlayers);
        
        // Assert
        assertEquals(1, result, "Should return 1 when selecting the second player");
    }

    /**
     * Test Case 3: availablePlayers = 3 players, input = 2
     * Expected: Returns 2
     * 
     * This test verifies that when there are three available players,
     * selecting the last player (index 2) returns the correct player index.
     */
    @Test
    void testPromptTargetPlayerWithThreePlayers() {
        // Arrange
        List<Player> availablePlayers = Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3);
        view.setUserInput("2");
        
        // Act
        int result = view.promptTargetPlayer(availablePlayers);
        
        // Assert
        assertEquals(2, result, "Should return 2 when selecting the last player");
    }

    /**
     * Test Case 4: availablePlayers = 2 players, input = 2
     * Expected: Throws IllegalArgumentException
     * 
     * This test verifies that when there are two available players,
     * selecting an index beyond the list size throws an exception.
     */
    @Test
    void testPromptTargetPlayerWithInvalidIndex() {
        // Arrange
        List<Player> availablePlayers = Arrays.asList(mockPlayer1, mockPlayer2);
        view.setUserInput("2");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            view.promptTargetPlayer(availablePlayers),
            "Should throw IllegalArgumentException when selecting index beyond list size"
        );
    }

    /**
     * Test Case 5: availablePlayers = 2 players, input = -1
     * Expected: Throws IllegalArgumentException
     * 
     * This test verifies that when there are two available players,
     * selecting a negative index throws an exception.
     */
    @Test
    void testPromptTargetPlayerWithNegativeIndex() {
        // Arrange
        List<Player> availablePlayers = Arrays.asList(mockPlayer1, mockPlayer2);
        view.setUserInput("-1");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            view.promptTargetPlayer(availablePlayers),
            "Should throw IllegalArgumentException when selecting negative index"
        );
    }

    /**
     * Test Case 6: availablePlayers = 2 players, input = "abc"
     * Expected: Throws IllegalArgumentException
     * 
     * This test verifies that when there are two available players,
     * entering non-numeric input throws an exception.
     */
    @Test
    void testPromptTargetPlayerWithNonNumericInput() {
        // Arrange
        List<Player> availablePlayers = Arrays.asList(mockPlayer1, mockPlayer2);
        view.setUserInput("abc");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            view.promptTargetPlayer(availablePlayers),
            "Should throw IllegalArgumentException when entering non-numeric input"
        );
    }

    /**
     * Test Case 1: cards = 1 card, input = 0
     * Expected: Returns 0
     * 
     * This test verifies that when there is only one card,
     * selecting index 0 returns the correct card index.
     */
    @Test
    void testPromptCardSelectionWithOneCard() {
        // Arrange
        List<Card> cards = Arrays.asList(mockCard1);
        view.setUserInput("0");
        
        // Act
        int result = view.promptCardSelection(cards);
        
        // Assert
        assertEquals(0, result, "Should return 0 when there is only one card");
    }

    /**
     * Test Case 2: cards = 2 cards, input = 1
     * Expected: Returns 1
     * 
     * This test verifies that when there are two cards,
     * selecting index 1 returns the correct card index.
     */
    @Test
    void testPromptCardSelectionWithTwoCards() {
        // Arrange
        List<Card> cards = Arrays.asList(mockCard1, mockCard2);
        view.setUserInput("1");
        
        // Act
        int result = view.promptCardSelection(cards);
        
        // Assert
        assertEquals(1, result, "Should return 1 when selecting the second card");
    }

    /**
     * Test Case 3: cards = 3 cards, input = 2
     * Expected: Returns 2
     * 
     * This test verifies that when there are three cards,
     * selecting the last card (index 2) returns the correct card index.
     */
    @Test
    void testPromptCardSelectionWithThreeCards() {
        // Arrange
        List<Card> cards = Arrays.asList(mockCard1, mockCard2, mockCard3);
        view.setUserInput("2");
        
        // Act
        int result = view.promptCardSelection(cards);
        
        // Assert
        assertEquals(2, result, "Should return 2 when selecting the last card");
    }

    /**
     * Test Case 4: cards = 2 cards, input = 2
     * Expected: Throws IllegalArgumentException
     * 
     * This test verifies that when there are two cards,
     * selecting an index beyond the list size throws an exception.
     */
    @Test
    void testPromptCardSelectionWithInvalidIndex() {
        // Arrange
        List<Card> cards = Arrays.asList(mockCard1, mockCard2);
        view.setUserInput("2");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            view.promptCardSelection(cards),
            "Should throw IllegalArgumentException when selecting index beyond list size"
        );
    }

    /**
     * Test Case 5: cards = 2 cards, input = -1
     * Expected: Throws IllegalArgumentException
     * 
     * This test verifies that when there are two cards,
     * selecting a negative index throws an exception.
     */
    @Test
    void testPromptCardSelectionWithNegativeIndex() {
        // Arrange
        List<Card> cards = Arrays.asList(mockCard1, mockCard2);
        view.setUserInput("-1");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            view.promptCardSelection(cards),
            "Should throw IllegalArgumentException when selecting negative index"
        );
    }
} 