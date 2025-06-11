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
 * Test class for CatCard steal effects in CardEffectService.
 */
class CardEffectCatCardStealTest {
    
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
    private Card stolenCard;
    
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
    void testHandleCatCardEffectWithSteal() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(Arrays.asList(stolenCard));
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.handleCardEffect(firstCatCard, turnOrder, gameDeck);
            
            Mockito.verify(player1)
                .removeCard(firstCatCard);
            Mockito.verify(player1)
                .removeCard(secondCatCard);
            Mockito.verify(player2)
                .removeCard(stolenCard);
            Mockito.verify(player1)
                .receiveCard(stolenCard);
            Mockito.verify(mockGameView)
                .displayCardStolen(
                    player1,
                    player2,
                    stolenCard
                );
        }
    }

    @Test
    void testApplyEffectWithCatCardStealTargetNotFound() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("nonexistent");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.applyEffect(firstCatCard, player1)
            );
        }
    }

    @Test
    void testApplyEffectWithCatCardStealSuccess() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Mock the view to select player2
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList()))
                .thenReturn(player2);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(Arrays.asList(stolenCard));
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(firstCatCard, player1);
            
            // Verify that the effect was applied correctly
            Mockito.verify(player1)
                .removeCard(firstCatCard);
            Mockito.verify(player1)
                .removeCard(secondCatCard);
            Mockito.verify(player2)
                .removeCard(stolenCard);
            Mockito.verify(player1)
                .receiveCard(stolenCard);
            Mockito.verify(mockGameView)
                .displayCardStolen(
                    player1,
                    player2,
                    stolenCard
                );
        }
    }

    @Test
    void testApplyEffectWithDeadTargetPlayer() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2 as dead
            Mockito.when(player2.isAlive()).thenReturn(false);
            Mockito.when(player2.getHand()).thenReturn(Collections.emptyList());
            
            // Set up the effect
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(Arrays.asList(stolenCard));
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            // Set up the card to throw the effect
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            // Verify the exception
            IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.handleCardEffect(
                    firstCatCard,
                    turnOrder,
                    gameDeck
                )
            );
            
            // Verify the error message
            Assertions.assertEquals("Target player is not alive", exception.getMessage());
        }
    }

    @Test
    void testApplyEffectWithEmptyHandTargetPlayer() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2 with empty hand
            Mockito.when(player2.isAlive()).thenReturn(true);
            Mockito.when(player2.getHand()).thenReturn(Collections.emptyList());
            
            // Set up the effect
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(Collections.emptyList());
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            // Set up the card to throw the effect
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            // Verify the exception
            IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.handleCardEffect(
                    firstCatCard,
                    turnOrder,
                    gameDeck
                )
            );
            
            // Verify the error message
            Assertions.assertEquals(
                "Target player has no cards",
                exception.getMessage()
            );
        }
    }

    @Test
    void testApplyEffectWithMultipleValidTargets() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up all players with cards
            Mockito.when(player2.getHand()).thenReturn(Arrays.asList(stolenCard));
            Mockito.when(player3.getHand()).thenReturn(Arrays.asList(stolenCard));
            Mockito.when(player4.getHand()).thenReturn(Arrays.asList(stolenCard));
            
            // Mock view to select player3
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList()))
                .thenReturn(player3);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player3");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(Arrays.asList(stolenCard));
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            Mockito.doThrow(effect).when(firstCatCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(firstCatCard, player1);
            
            // Verify that the effect was applied to player3
            Mockito.verify(player1)
                .removeCard(firstCatCard);
            Mockito.verify(player1)
                .removeCard(secondCatCard);
            Mockito.verify(player3)
                .removeCard(stolenCard);
            Mockito.verify(player1)
                .receiveCard(stolenCard);
            Mockito.verify(mockGameView)
                .displayCardStolen(
                    player1,
                    player3,
                    stolenCard
                );
        }
    }
} 