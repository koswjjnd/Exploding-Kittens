package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuperSkipCardTest {
    private SuperSkipCard superSkipCard;
    private List<Player> turnOrder;
    
    @Mock
    private Deck gameDeck;
    
    @Mock
    private Player player1;
    
    @Mock
    private Player player2;
    
    @Mock
    private Player player3;

    @BeforeEach
    void setUp() {
        superSkipCard = new SuperSkipCard();
        turnOrder = new ArrayList<>();
    }

    @Test
    void testEffectWithEmptyTurnOrder() {
        // Test Case 0: Empty turnOrder
        assertThrows(IndexOutOfBoundsException.class, () -> {
            superSkipCard.effect(turnOrder, gameDeck);
        });
    }


} 