package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for basic functionality of CardEffectService.
 */
class CardEffectBasicTest {
    
    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private Deck gameDeck;
    @Mock
    private Card mockCard;
    @Mock
    private GameView mockGameView;
    
    private CardEffectService cardEffectService;
    private List<Player> turnOrder;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardEffectService = new CardEffectService(mockGameView);
        turnOrder = Arrays.asList(player1, player2);
        
        // Set up common mock behaviors
        Mockito.when(player1.getName()).thenReturn("player1");
        Mockito.when(player2.getName()).thenReturn("player2");
        Mockito.when(player1.isAlive()).thenReturn(true);
        Mockito.when(player2.isAlive()).thenReturn(true);
    }
    
    /**
     * BVA Test Case 1: card = null, ctx = null
     * Expected: IllegalArgumentException
     */
    @Test
    void testNullCardAndNullContext() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.applyEffect(null, null));
    }
    
    /**
     * BVA Test Case 2: card = null, ctx = valid GameContext
     * Expected: IllegalArgumentException
     */
    @Test
    void testNullCard() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.applyEffect(null, player1));
    }
    
    /**
     * BVA Test Case 3: card = valid Card, ctx = null
     * Expected: IllegalArgumentException
     */
    @Test
    void testNullContext() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cardEffectService.applyEffect(mockCard, null);
        });
    }

    /**
     * BVA Test Case 4: card = valid Card, ctx = valid GameContext
     * Expected: card.effect() is called once
     */
    @Test
    void testValidInputs() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            cardEffectService.applyEffect(mockCard, player1);
            
            Mockito.verify(mockCard, Mockito.times(1)).effect(turnOrder, gameDeck);
        }
    }

    /**
     * Test uninitialized game context
     */
    @Test
    void testUninitializedGameContext() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(null);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(mockCard, player1));
        }
    }

    /**
     * Test null game deck
     */
    @Test
    void testNullGameDeck() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(null);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(mockCard, player1));
        }
    }
} 