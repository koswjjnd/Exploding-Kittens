package explodingkittens.view;

import explodingkittens.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FavorCardViewTest {
    
    private FavorCardView view;
    
    @Mock
    private Player mockPlayer;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        view = new FavorCardView();
        when(mockPlayer.getName()).thenReturn("TestPlayer");
    }
    
    /**
     * Test Case 1: availablePlayers = 1 player, input = 0
     * Expected: Returns 0
     */
    @Test
    void testPromptTargetPlayerWithOnePlayer() {
        // Arrange
        List<Player> availablePlayers = Collections.singletonList(mockPlayer);
        view.setUserInput("0");
        
        // Act
        int result = view.promptTargetPlayer(availablePlayers);
        
        // Assert
        assertEquals(0, result, "Should return 0 when there is only one player");
    }

    /**
     * Test Case 2: availablePlayers = 1 player, input = 1
     * Expected: Throws IllegalArgumentException
     */
    @Test
    void testPromptTargetPlayerWithOnePlayerInvalidInput() {
        // Arrange
        List<Player> availablePlayers = Collections.singletonList(mockPlayer);
        view.setUserInput("1");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            view.promptTargetPlayer(availablePlayers),
            "Should throw IllegalArgumentException for invalid input"
        );
    }
} 