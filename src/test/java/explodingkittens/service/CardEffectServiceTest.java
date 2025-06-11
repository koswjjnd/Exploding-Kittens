package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.AttackCard;
import explodingkittens.model.SkipCard;
import explodingkittens.model.SeeTheFutureCard;
import explodingkittens.model.ShuffleCard;
import explodingkittens.model.CatCard;
import explodingkittens.model.DefuseCard;
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
import java.util.ArrayList;

/**
 * Test class for CardEffectService.
 */
class CardEffectServiceTest {
    
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
     * Test SeeTheFutureCard effect
     */
    @Test
    void testSeeTheFutureCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            SeeTheFutureCard seeTheFutureCard = Mockito.mock(SeeTheFutureCard.class);
            cardEffectService.applyEffect(seeTheFutureCard, player1);
            
            Mockito.verify(seeTheFutureCard).setView(Mockito.any());
            Mockito.verify(seeTheFutureCard).effect(turnOrder, gameDeck);
        }
    }

    /**
     * Test CatCard request effect
     */
    @Test
    void testCatCardRequestEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            Card requestedCard = Mockito.mock(Card.class);
            List<Card> matchingCards = Arrays.asList(requestedCard);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            CatCard thirdCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(matchingCards);
            Mockito.when(requestedCard.getType()).thenReturn(CardType.ATTACK);
            
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList())).thenReturn(player2);
            Mockito.when(mockGameView.selectCardFromPlayer(Mockito.any(), Mockito.anyList()))
                .thenReturn(requestedCard);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(catCard, player1);
            
            Mockito.verify(player1).removeCard(firstCard);
            Mockito.verify(player1).removeCard(secondCard);
            Mockito.verify(player1).removeCard(thirdCard);
            Mockito.verify(mockGameView).displayCatCardEffect("request", player1, player2);
            Mockito.verify(player2).removeCard(requestedCard);
            Mockito.verify(player1).receiveCard(requestedCard);
            Mockito.verify(mockGameView).displayCardRequested(player1, player2, requestedCard);
        }
    }

    /**
     * Test CatCard steal effect
     */
    @Test
    void testCatCardStealEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> targetHand = Arrays.asList(stolenCard);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(targetHand);
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getName()).thenReturn("player2");
            Mockito.when(player2.getHand()).thenReturn(targetHand);
            Mockito.when(player2.isAlive()).thenReturn(true);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(catCard, player1);
            
            Mockito.verify(player1).removeCard(firstCard);
            Mockito.verify(player1).removeCard(secondCard);
            Mockito.verify(player2).removeCard(stolenCard);
            Mockito.verify(player1).receiveCard(stolenCard);
            Mockito.verify(mockGameView).displayCardStolen(player1, player2, stolenCard);
        }
    }

    /**
     * Test CatCard request effect when target player has no matching card
     */
    @Test
    void testCatCardRequestEffectNoMatchingCard() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            Card nonMatchingCard = Mockito.mock(Card.class);
            List<Card> targetHand = Arrays.asList(nonMatchingCard);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            CatCard thirdCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(targetHand);
            Mockito.when(nonMatchingCard.getType()).thenReturn(CardType.SKIP);
            Mockito.when(player2.getName()).thenReturn("player2");
            Mockito.when(player2.isAlive()).thenReturn(true);
            
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList())).thenReturn(player2);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(catCard, player1);
            
            Mockito.verify(player1).removeCard(firstCard);
            Mockito.verify(player1).removeCard(secondCard);
            Mockito.verify(player1).removeCard(thirdCard);
            Mockito.verify(mockGameView).displayCatCardEffect("request", player1, player2);
            Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
            Mockito.verify(mockGameView).showError(Mockito.anyString());
        }
    }

    /**
     * Test when no valid target players are available
     */
    @Test
    void testNoValidTargetPlayers() {
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
    void testCatCardStealEffectWithNullHand() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(null);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList());
            Mockito.when(player2.getName()).thenReturn("player2");
            Mockito.when(player2.isAlive()).thenReturn(true);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(NullPointerException.class, () -> 
                cardEffectService.applyEffect(catCard, player1));
        }
    }

    @Test
    void testCatCardStealEffectWithInvalidIndex() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> targetHand = Arrays.asList(stolenCard);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(targetHand);
            Mockito.when(effect.getTargetCardIndex()).thenReturn(1); // Invalid index
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(targetHand);
            Mockito.when(player2.getName()).thenReturn("player2");
            Mockito.when(player2.isAlive()).thenReturn(true);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
                cardEffectService.applyEffect(catCard, player1));
        }
    }

    @Test
    void testCatCardRequestEffectWithNullSelection() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            Card requestedCard = Mockito.mock(Card.class);
            List<Card> matchingCards = Arrays.asList(requestedCard);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            CatCard thirdCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(matchingCards);
            Mockito.when(requestedCard.getType()).thenReturn(CardType.ATTACK);
            Mockito.when(player2.getName()).thenReturn("player2");
            Mockito.when(player2.isAlive()).thenReturn(true);
            
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList())).thenReturn(player2);
            Mockito.when(mockGameView.selectCardFromPlayer(Mockito.any(), Mockito.anyList()))
                .thenReturn(null);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(catCard, player1);
            
            Mockito.verify(player1).removeCard(firstCard);
            Mockito.verify(player1).removeCard(secondCard);
            Mockito.verify(player1).removeCard(thirdCard);
            Mockito.verify(mockGameView).displayCatCardEffect("request", player1, player2);
            Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
            Mockito.verify(mockGameView).showError(Mockito.anyString());
        }
    }

    @Test
    void testHandleCardEffect() {
        Card card = Mockito.mock(Card.class);
        cardEffectService.handleCardEffect(card, turnOrder, gameDeck);
        Mockito.verify(card).effect(turnOrder, gameDeck);
    }

    @Test
    void testHandleCardEffectWithNullCard() {
        cardEffectService.handleCardEffect(null, turnOrder, gameDeck);
        // No effect should be called
    }

    @Test
    void testHandleCardEffectWithCatCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Card card = Mockito.mock(Card.class);
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(Arrays.asList(Mockito.mock(Card.class)));
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(card));
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(Mockito.mock(Card.class)));
            
            Mockito.doThrow(effect).when(card).effect(turnOrder, gameDeck);
            
            cardEffectService.handleCardEffect(card, turnOrder, gameDeck);
            
            Mockito.verify(player1).removeCard(firstCard);
            Mockito.verify(player1).removeCard(secondCard);
        }
    }

    @Test
    void testUninitializedGameContext() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(null);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(mockCard, player1));
        }
    }

    @Test
    void testNullGameDeck() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(null);
            
            Assertions.assertThrows(IllegalStateException.class, () -> 
                cardEffectService.applyEffect(mockCard, player1));
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
    void testStealEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard catCard = Mockito.mock(CatCard.class);
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> targetHand = Arrays.asList(stolenCard);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCard = Mockito.mock(CatCard.class);
            CatCard secondCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(targetHand);
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            Mockito.when(effect.getFirstCard()).thenReturn(firstCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCard);
            
            Mockito.when(player1.getHand()).thenReturn(Arrays.asList(catCard));
            Mockito.when(player2.getHand()).thenReturn(targetHand);
            
            Mockito.doThrow(effect).when(catCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(catCard, player1);
            
            Mockito.verify(player1).removeCard(firstCard);
            Mockito.verify(player1).removeCard(secondCard);
            Mockito.verify(player2).removeCard(stolenCard);
            Mockito.verify(player1).receiveCard(stolenCard);
            Mockito.verify(mockGameView).displayCardStolen(player1, player2, stolenCard);
        }
    }
} 