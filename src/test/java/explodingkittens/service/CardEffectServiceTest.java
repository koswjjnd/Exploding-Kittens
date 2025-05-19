package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.AttackCard;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for CardEffectService.
 */
class CardEffectServiceTest {
    
    @Mock
    private AttackService attackService;
    
    @Mock
    private Player player1;
    
    @Mock
    private Player player2;
    
    private CardEffectService cardEffectService;
    private List<Player> turnOrder;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardEffectService = new CardEffectService(attackService);
        turnOrder = Arrays.asList(player1, player2);
    }
    
    /**
     * BVA Test Case 1: card = null, ctx = null
     * Expected: IllegalArgumentException
     */
    @Test
    void testHandleCardEffectWithNullCardAndNullContext() {
        assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.handleCardEffect(null, null));
    }
    
    @Test
    void testHandleCardEffectWithNullCard() {
        assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.handleCardEffect(null, GameContext.class));
    }
    
   @Test
    void handleCardEffect_nullCtx_throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardEffectService.handleCardEffect(new AttackCard(), null);
        });
    }
} 