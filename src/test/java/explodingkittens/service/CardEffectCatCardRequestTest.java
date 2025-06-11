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
 * Test class for CatCard request effects in CardEffectService.
 */
class CardEffectCatCardRequestTest {
    
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
    @Mock
    private Card requestedCard;
    @Mock
    private Card otherCard;
    
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
    void testHandleCatCardEffectWithRequest() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(requestedCard));
            Mockito.when(requestedCard.getType()).thenReturn(CardType.ATTACK);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.handleCardEffect(firstCatCard, turnOrder, gameDeck);
            
            Mockito.verify(player1).removeCard(firstCatCard);
            Mockito.verify(player1).removeCard(secondCatCard);
            Mockito.verify(player1).removeCard(thirdCatCard);
            Mockito.verify(player2).removeCard(requestedCard);
            Mockito.verify(player1).receiveCard(requestedCard);
            Mockito.verify(mockGameView)
                .displayCardRequested(
                    player1,
                    player2,
                    requestedCard
                );
        }
    }

    @Test
    void testHandleCatCardEffectWithRequestNoMatchingCard() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(otherCard));
            Mockito.when(otherCard.getType()).thenReturn(CardType.SHUFFLE);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.handleCardEffect(firstCatCard, turnOrder, gameDeck);
            
            Mockito.verify(player1).removeCard(firstCatCard);
            Mockito.verify(player1).removeCard(secondCatCard);
            Mockito.verify(player1).removeCard(thirdCatCard);
            Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
        }
    }

    @Test
    void testApplyEffectWithCatCardRequestNoCardSelected() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(requestedCard));
            Mockito.when(requestedCard.getType()).thenReturn(CardType.ATTACK);
            Mockito.when(mockGameView.selectCardFromPlayer(
                    player2, 
                    Arrays.asList(requestedCard)
            ))
                .thenReturn(null);
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList()))
                .thenReturn(player2);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(firstCatCard, player1);
            
            Mockito.verify(player1).removeCard(firstCatCard);
            Mockito.verify(player1).removeCard(secondCatCard);
            Mockito.verify(player1).removeCard(thirdCatCard);
            Mockito.verify(mockGameView)
                .displayCardRequested(
                    player1,
                    player2,
                    null
                );
            Mockito.verify(mockGameView).showError("No card was selected.");
        }
    }

    @Test
    void testApplyEffectWithCatCardRequestMultipleMatchingCards() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Card attackCard1 = Mockito.mock(Card.class);
            Card attackCard2 = Mockito.mock(Card.class);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(attackCard1, attackCard2));
            Mockito.when(attackCard1.getType()).thenReturn(CardType.ATTACK);
            Mockito.when(attackCard2.getType()).thenReturn(CardType.ATTACK);
            Mockito.when(mockGameView.selectTargetPlayer(
                    Mockito.anyList()
            )).thenReturn(player2);
            Mockito.when(mockGameView.selectCardFromPlayer(
                    player2, 
                    Arrays.asList(attackCard1, attackCard2)
            )).thenReturn(attackCard1);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(firstCatCard, player1);
            
            Mockito.verify(player1).removeCard(firstCatCard);
            Mockito.verify(player1).removeCard(secondCatCard);
            Mockito.verify(player1).removeCard(thirdCatCard);
            Mockito.verify(player2).removeCard(attackCard1);
            Mockito.verify(player1).receiveCard(attackCard1);
        }
    }
} 