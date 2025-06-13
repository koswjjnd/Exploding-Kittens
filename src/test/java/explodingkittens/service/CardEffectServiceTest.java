package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.AttackCard;
import explodingkittens.model.SkipCard;
import explodingkittens.model.SeeTheFutureCard;
import explodingkittens.model.ShuffleCard;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.TacoCatCard;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.view.GameView;
import explodingkittens.view.SeeTheFutureView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

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
     * Test handleCardEffect with null card
     */
    @Test
    void testHandleCardEffectWithNullCard() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            cardEffectService.handleCardEffect(null, turnOrder, gameDeck);
            
            // No effect should be called
            Mockito.verifyNoInteractions(mockCard);
        }
    }

    /**
     * Test handleCardEffect with non-cat card effect
     */
    @Test
    void testHandleCardEffectWithNonCatCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            RuntimeException exception = new RuntimeException("Test exception");
            Mockito.doThrow(exception)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(RuntimeException.class, () -> 
                cardEffectService.handleCardEffect(mockCard, turnOrder, gameDeck));
        }
    }

    /**
     * Test handleCardEffect with cat card effect (steal)
     */
    @Test
    void testHandleCardEffectWithCatCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with a card to steal
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> player2Hand = Arrays.asList(stolenCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand())
                .thenReturn(player2Hand);
            Mockito.when(effect.getTargetCardIndex())
                .thenReturn(0);
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            cardEffectService.handleCardEffect(mockCard, turnOrder, gameDeck);
            
            // Verify that cards are removed from player1
            Mockito.verify(player1).removeCard(firstCatCard);
            Mockito.verify(player1).removeCard(secondCatCard);
            
            // Verify that card is stolen from player2 and given to player1
            Mockito.verify(player2).removeCard(stolenCard);
            Mockito.verify(player1).receiveCard(stolenCard);
            
            // Verify that the view is updated
            Mockito.verify(mockGameView)
                .displayCardStolen(
                    player1,
                    player2,
                    stolenCard
                );
        }
    }

    /**
     * Test handleCardEffect with non-existent target player
     */
    @Test
    void testHandleCardEffectWithNonExistentTargetPlayer() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("nonExistentPlayer");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.handleCardEffect(
                    mockCard,
                    turnOrder,
                    gameDeck
                )
            );
        }
    }

    /**
     * Test handleCardEffect with empty hand target player
     */
    @Test
    void testHandleCardEffectWithEmptyHandTargetPlayer() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Mockito.when(player2.getHand()).thenReturn(Collections.emptyList());
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand())
                .thenReturn(Collections.emptyList());
            Mockito.when(effect.getTargetCardIndex())
                .thenReturn(0);
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.handleCardEffect(
                    mockCard,
                    turnOrder,
                    gameDeck
                )
            );
            
            Assertions.assertEquals("Target player has no cards", exception.getMessage());
        }
    }

    /**
     * Test handleCardEffect with invalid target card index
     */
    @Test
    void testHandleCardEffectWithInvalidTargetCardIndex() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with a card
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> player2Hand = Arrays.asList(stolenCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand())
                .thenReturn(player2Hand);
            Mockito.when(effect.getTargetCardIndex())
                .thenReturn(1); // Invalid index (out of bounds)
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.handleCardEffect(
                    mockCard,
                    turnOrder,
                    gameDeck
                )
            );
            
            Assertions.assertEquals("Invalid target card index", exception.getMessage());
        }
    }

    /**
     * Test handleCardEffect with negative target card index
     */
    @Test
    void testHandleCardEffectWithNegativeTargetCardIndex() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with a card
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> player2Hand = Arrays.asList(stolenCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand())
                .thenReturn(player2Hand);
            Mockito.when(effect.getTargetCardIndex())
                .thenReturn(-1); // Invalid negative index
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> cardEffectService.handleCardEffect(
                    mockCard,
                    turnOrder,
                    gameDeck
                )
            );
            
            Assertions.assertEquals("Invalid target card index", exception.getMessage());
        }
    }

    /**
     * Test handleCardEffect with normal card effect execution
     */
    @Test
    void testHandleCardEffectWithNormalExecution() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up a card that executes normally without throwing exception
            Card normalCard = Mockito.mock(Card.class);
            Mockito.doNothing().when(normalCard).effect(turnOrder, gameDeck);
            
            // Execute the method
            cardEffectService.handleCardEffect(normalCard, turnOrder, gameDeck);
            
            // Verify that effect was called once
            Mockito.verify(normalCard, Mockito.times(1)).effect(turnOrder, gameDeck);
        }
    }

    /**
     * Test applyEffect with card type filtering
     */
    @Test
    void testApplyEffectWithCardTypeFiltering() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with different types of cards
            Card attackCard = Mockito.mock(AttackCard.class);
            Card skipCard = Mockito.mock(SkipCard.class);
            Card seeTheFutureCard = Mockito.mock(SeeTheFutureCard.class);
            Card shuffleCard = Mockito.mock(ShuffleCard.class);
            Card defuseCard = Mockito.mock(DefuseCard.class);
            
            Mockito.when(attackCard.getType()).thenReturn(CardType.ATTACK);
            Mockito.when(skipCard.getType()).thenReturn(CardType.SKIP);
            Mockito.when(seeTheFutureCard.getType()).thenReturn(CardType.SEE_THE_FUTURE);
            Mockito.when(shuffleCard.getType()).thenReturn(CardType.SHUFFLE);
            Mockito.when(defuseCard.getType()).thenReturn(CardType.DEFUSE);
            
            List<Card> player2Hand = Arrays.asList(
                attackCard,
                skipCard,
                seeTheFutureCard,
                shuffleCard,
                defuseCard
            );
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            // Set up cat card effect to request ATTACK type
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getTargetPlayerHand())
                .thenReturn(player2Hand);
            Mockito.when(effect.getTargetCardIndex())
                .thenReturn(0);
            Mockito.when(effect.getRequestedCardType())
                .thenReturn(CardType.ATTACK);
            
            // Mock the view behavior
            Mockito.when(mockGameView.selectTargetPlayer(
                    Mockito.anyList()
                ))
                .thenReturn(player2);
            Mockito.when(mockGameView.selectCardFromPlayer(
                    player2,
                    Arrays.asList(attackCard)
                ))
                .thenReturn(attackCard);
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            // Execute the method
            cardEffectService.applyEffect(mockCard, player1);
            
            // Verify that only the attack card was selected
            Mockito.verify(player2).removeCard(attackCard);
            Mockito.verify(player1).receiveCard(attackCard);
            
            // Verify that other cards were not selected
            Mockito.verify(player2, Mockito.never()).removeCard(skipCard);
            Mockito.verify(player2, Mockito.never()).removeCard(seeTheFutureCard);
            Mockito.verify(player2, Mockito.never()).removeCard(shuffleCard);
            Mockito.verify(player2, Mockito.never()).removeCard(defuseCard);
            
            // Verify that the appropriate messages were displayed
            Mockito.verify(mockGameView)
                .displayCatCardEffect(
                    "request",
                    player1,
                    player2
                );
            Mockito.verify(mockGameView)
                .displayCardRequested(
                    player1,
                    player2,
                    attackCard
                );
        }
    }

    /**
     * Test applyEffect with card type filtering when no card is selected
     */
    @Test
    void testApplyEffectWithCardTypeFilteringNoSelection() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with matching cards
            Card attackCard1 = Mockito.mock(AttackCard.class);
            Card attackCard2 = Mockito.mock(AttackCard.class);
            
            Mockito.when(attackCard1.getType()).thenReturn(CardType.ATTACK);
            Mockito.when(attackCard2.getType()).thenReturn(CardType.ATTACK);
            
            List<Card> player2Hand = Arrays.asList(attackCard1, attackCard2);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            // Set up cat card effect to request ATTACK type
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getTargetPlayerHand())
                .thenReturn(player2Hand);
            Mockito.when(effect.getTargetCardIndex())
                .thenReturn(0);
            Mockito.when(effect.getRequestedCardType())
                .thenReturn(CardType.ATTACK);
            
            // Mock the view to return null (no card selected)
            Mockito.when(mockGameView.selectTargetPlayer(
                    Mockito.anyList()
                ))
                .thenReturn(player2);
            Mockito.when(
                mockGameView.selectCardFromPlayer(
                    player2,
                    Arrays.asList(attackCard1, attackCard2)
                )
            )
                .thenReturn(null);
            
            Mockito.doThrow(effect)
                .when(mockCard)
                .effect(turnOrder, gameDeck);
            
            // Execute the method
            cardEffectService.applyEffect(mockCard, player1);
            
            // Verify that no cards were transferred
            Mockito.verify(player2, Mockito.never()).removeCard(Mockito.any());
            Mockito.verify(player1, Mockito.never()).receiveCard(Mockito.any());
            
            // Verify that the appropriate messages were displayed
            Mockito.verify(mockGameView)
                .displayCatCardEffect(
                    "request",
                    player1,
                    player2
                );
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
    void testApplyEffectWithSpecificCatTypeFiltering() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);

            // Set up one TacoCat and one RainbowCat in player2's hand
            CatCard tacoCat = Mockito.mock(CatCard.class);
            CatCard rainbowCat = Mockito.mock(CatCard.class);

            Mockito.when(tacoCat.getType()).thenReturn(CardType.CAT_CARD);
            Mockito.when(tacoCat.getCatType()).thenReturn(CatType.TACOCAT);

            Mockito.when(rainbowCat.getType()).thenReturn(CardType.CAT_CARD);
            Mockito.when(rainbowCat.getCatType()).thenReturn(CatType.RAINBOW_CAT);

            List<Card> player2Hand = Arrays.asList(tacoCat, rainbowCat);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);

            // Set up effect to request TACOCAT
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(player2Hand);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.CAT_CARD);
            Mockito.when(effect.getRequestedCatType()).thenReturn(CatType.TACOCAT);

            // Mock view to select TacoCat only
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList()))
                .thenReturn(player2);
            Mockito.when(mockGameView.selectCardFromPlayer(Mockito.eq(player2), Mockito.anyList()))
                .thenReturn(tacoCat);

            Mockito.doThrow(effect).when(mockCard).effect(turnOrder, gameDeck);

            // Act
            cardEffectService.applyEffect(mockCard, player1);

            // Assert
            Mockito.verify(player2).removeCard(tacoCat);
            Mockito.verify(player1).receiveCard(tacoCat);
            Mockito.verify(mockGameView).displayCardRequested(player1, player2, tacoCat);
            Mockito.verify(mockGameView, Mockito.never()).showError(Mockito.any());
        }
    }

    @Test
    void testSeeTheFutureViewInjection() {
        // Setup
        SeeTheFutureCard seeTheFutureCard = mock(SeeTheFutureCard.class);
        when(seeTheFutureCard.getType()).thenReturn(CardType.SEE_THE_FUTURE);
        
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Execute
            cardEffectService.applyEffect(seeTheFutureCard, player1);
            
            // Verify
            verify(seeTheFutureCard).setView(any(SeeTheFutureView.class));
        }
    }

    @Test
    void testDisplayCatCardEffectForSteal() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with a card to steal
            Card stolenCard = Mockito.mock(Card.class);
            List<Card> player2Hand = Arrays.asList(stolenCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(null);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(player2Hand);
            Mockito.when(effect.getTargetCardIndex()).thenReturn(0);
            
            Mockito.doThrow(effect).when(mockCard).effect(turnOrder, gameDeck);
            
            // Execute
            cardEffectService.applyEffect(mockCard, player1);
            
            // Verify
            verify(mockGameView).displayCatCardEffect("steal", player1, player2);
        }
    }

    @Test
    void testDisplayCatCardEffectForRequest() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with a card to request
            Card requestedCard = Mockito.mock(Card.class);
            List<Card> player2Hand = Arrays.asList(requestedCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            Mockito.when(requestedCard.getType()).thenReturn(CardType.ATTACK);
            
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            
            Mockito.when(mockGameView.selectTargetPlayer(any())).thenReturn(player2);
            Mockito.when(mockGameView.selectCardFromPlayer(any(), any())).thenReturn(requestedCard);
            
            Mockito.doThrow(effect).when(mockCard).effect(turnOrder, gameDeck);
            
            // Execute
            cardEffectService.applyEffect(mockCard, player1);
            
            // Verify
            verify(mockGameView).displayCatCardEffect("request", player1, player2);
        }
    }

    @Test
    void testShowErrorForNoRequestedCardType() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player states
            when(player1.isAlive()).thenReturn(true);
            when(player2.isAlive()).thenReturn(true);
            when(player1.getName()).thenReturn("player1");
            when(player2.getName()).thenReturn("player2");
            
            // Set up player2's hand with a non-matching card
            Card nonMatchingCard = mock(Card.class);
            when(nonMatchingCard.getType()).thenReturn(CardType.SKIP);
            List<Card> player2Hand = Arrays.asList(nonMatchingCard);
            when(player2.getHand()).thenReturn(player2Hand);
            
            CatCard.CatCardEffect effect = mock(CatCard.CatCardEffect.class);
            when(effect.getTargetPlayerName()).thenReturn("player2");
            when(effect.getFirstCard()).thenReturn(firstCatCard);
            when(effect.getSecondCard()).thenReturn(secondCatCard);
            when(effect.getThirdCard()).thenReturn(thirdCatCard);
            when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            
            // Mock view to return player2 as target
            when(mockGameView.selectTargetPlayer(any())).thenReturn(player2);
            
            // Mock card to throw effect
            doThrow(effect).when(mockCard).effect(turnOrder, gameDeck);
            
            // Execute
            cardEffectService.applyEffect(mockCard, player1);
            
            // Verify
            verify(mockGameView).showError("Target player does not have the requested card type.");
        }
    }
} 