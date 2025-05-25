package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

class ShuffleCardTest {
    
    private ShuffleCard shuffleCard;
    private List<Player> turnOrder;
    private Deck mockDeck;
    
    @Mock
    private Player mockPlayer1;
    
    @Mock
    private Player mockPlayer2;
    
    @Mock
    private Player mockPlayer3;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shuffleCard = new ShuffleCard();
        mockDeck = new Deck();
        turnOrder = Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3);
    }
    
    /**
     * Test Case 1: Null deck
     * Expected: IllegalArgumentException
     * 
     * This test verifies that when the deck is null,
     * an IllegalArgumentException is thrown.
     */
    @Test
    void testEffectWithNullDeck() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> shuffleCard.effect(turnOrder, null),
            "Should throw IllegalArgumentException when deck is null"
        );
    }
    
    /**
     * Test Case 2: Valid deck
     * Expected: Deck is shuffled
     * 
     * This test verifies that when a valid deck is provided,
     * the deck is shuffled.
     */
    @Test
    void testEffectWithValidDeck() {
        // Act
        shuffleCard.effect(turnOrder, mockDeck);
        
        // Assert
        verify(mockDeck).shuffle();
    }
}
