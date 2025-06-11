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
import java.util.Collections;

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
} 