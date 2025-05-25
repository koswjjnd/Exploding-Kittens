package explodingkittens.view;

import explodingkittens.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        
        // Act
        int result = view.promptTargetPlayer(availablePlayers);
        
        // Assert
        assertEquals(0, result, "Should return 0 when there is only one player");
    }
} 