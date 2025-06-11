package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatCard;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * Test class for basic CatCard effects in CardEffectService.
 */
class CardEffectCatCardTest {
    
    @Mock
    private GameView mockGameView;
    @Mock
    private Deck gameDeck;
    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private Player player3;
    @Mock
    private Player player4;
    @Mock
    private CatCard firstCatCard;
    @Mock
    private CatCard secondCatCard;
    @Mock
    private CatCard thirdCatCard;
    
    private CardEffectService cardEffectService;
    private List<Player> turnOrder;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardEffectService = new CardEffectService(mockGameView);
        turnOrder = Arrays.asList(player1, player2, player3, player4);
        
        // Set up common mock behaviors
        Mockito.when(player1.getName()).thenReturn("player1");
        Mockito.when(player2.getName()).thenReturn("player2");
        Mockito.when(player3.getName()).thenReturn("player3");
        Mockito.when(player4.getName()).thenReturn("player4");
        Mockito.when(player1.isAlive()).thenReturn(true);
        Mockito.when(player2.isAlive()).thenReturn(true);
        Mockito.when(player3.isAlive()).thenReturn(true);
        Mockito.when(player4.isAlive()).thenReturn(true);
    }

    @Test
    void testHandleCardEffectWithNullCard() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            cardEffectService.handleCardEffect(null, turnOrder, gameDeck);
            
            // No effect should be called
            Mockito.verifyNoInteractions(firstCatCard);
        }
    }

    @Test
    void testApplyEffectWithNullCard() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.applyEffect(null, player1));
    }

    @Test
    void testApplyEffectWithNullPlayer() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            cardEffectService.applyEffect(firstCatCard, null));
    }

    @Test
    void testApplyEffectWithUninitializedGameContext() {
        Assertions.assertThrows(IllegalStateException.class, () -> 
            cardEffectService.applyEffect(firstCatCard, player1));
    }

    @Test
    void testHandleCardEffectWithNonCatCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            RuntimeException exception = new RuntimeException("Test exception");
            Mockito.doThrow(exception).when(firstCatCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(RuntimeException.class, () -> 
                cardEffectService.handleCardEffect(firstCatCard, turnOrder, gameDeck));
        }
    }

    @Test
    void testHandleCardEffectWithOtherException() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.handleCardEffect(firstCatCard, turnOrder, gameDeck);
            
            Mockito.verify(player1).removeCard(firstCatCard);
            Mockito.verify(player1).removeCard(secondCatCard);
            Mockito.verify(player1).removeCard(thirdCatCard);
        }
    }
} 