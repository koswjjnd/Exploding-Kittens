package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.controller.GameContext;
import explodingkittens.model.AttackCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CardEffectServiceTest {
    
    private final CardEffectService cardEffectService = new CardEffectService();
    
    @Test
    void testHandleCardEffectWithNullInputs() {
        // Test Case 1: card = null, ctx = null
        assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.handleCardEffect(null, null));
            
        // Test Case 2: card = null, ctx = valid GameContext
        assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.handleCardEffect(null, GameContext.class));
            
        // Test Case 3: card = ATTACK, ctx = null
        Card attackCard = new AttackCard();
        assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.handleCardEffect(attackCard, null));
    }
} 