package explodingkittens.view;

import explodingkittens.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FavorCardViewTest {
    
    private FavorCardView view;
    
    @Mock
    private Player mockPlayer1;
    
    @Mock
    private Player mockPlayer2;
    
    @Mock
    private Player mockPlayer3;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        view = new FavorCardView();
        when(mockPlayer1.getName()).thenReturn("TestPlayer1");
        when(mockPlayer2.getName()).thenReturn("TestPlayer2");
        when(mockPlayer3.getName()).thenReturn("TestPlayer3");
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
} 