package explodingkittens.service;

import explodingkittens.controller.GameContext;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CardType;
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
import java.util.Collections;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

class CardEffectServiceSelectTargetTest {
    
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
    private CatCard firstCatCard;
    @Mock
    private CatCard secondCatCard;
    @Mock
    private Card stolenCard;
    
    private CardEffectService cardEffectService;
    private List<Player> turnOrder;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardEffectService = new CardEffectService(mockGameView);
        turnOrder = Arrays.asList(player1, player2, player3);
        
        // Set up common mock behaviors
        Mockito.when(player1.getName()).thenReturn("player1");
        Mockito.when(player2.getName()).thenReturn("player2");
        Mockito.when(player3.getName()).thenReturn("player3");
    }

    @Test
    void testSelectTargetPlayerWithEmptyHand() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            CatCard thirdCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.isAlive()).thenReturn(true);
            Mockito.when(player2.isAlive()).thenReturn(true);
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList());
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(catCard, player1));
        }
    }

    @Test
    void testSelectTargetPlayerWithNoValidPlayers() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            CatCard thirdCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.isAlive()).thenReturn(true);
            Mockito.when(player2.isAlive()).thenReturn(false);
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList());
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(catCard, player1));
        }
    }

    @Test
    void testHandleCatCardEffectTargetPlayerNotFound() {
        // Setup test data
        CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
        List<Player> turnOrder = Arrays.asList(player1); // Only player1 in turn order

        // Configure mocks
        Mockito.when(effect.getTargetPlayerName()).thenReturn("Non-existent Player");
        Mockito.when(player1.getName()).thenReturn("Player 1");

        // Call the method using reflection and expect exception
        try {
            java.lang.reflect.Method method = CardEffectService.class.getDeclaredMethod(
                "handleCatCardEffect", CatCard.CatCardEffect.class, List.class);
            method.setAccessible(true);
            method.invoke(cardEffectService, effect, turnOrder);
            Assertions.fail("Expected IllegalStateException was not thrown");
        } 
        catch (InvocationTargetException e) {
            Assertions.assertTrue(e.getCause() instanceof IllegalStateException, 
                "Expected IllegalStateException but got " + e.getCause().getClass().getName());
            Assertions.assertEquals("Target player not found", e.getCause().getMessage());
        } 
        catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testTargetPlayerSelection() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            CatCard thirdCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.isAlive()).thenReturn(true);
            Mockito.when(player2.isAlive()).thenReturn(true);
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(Mockito.mock(Card.class)));
            Mockito.when(player2.getName()).thenReturn("player2");
            
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList())).thenReturn(player2);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(catCard, player1);
            
            Mockito.verify(mockGameView).selectTargetPlayer(Mockito.anyList());
        }
    }

    @Test
    void testHandleCatCardEffectWithNonExistentTargetPlayer() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("nonExistentPlayer");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getName()).thenReturn("player2");
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(catCard, player1));
        }
    }
} 