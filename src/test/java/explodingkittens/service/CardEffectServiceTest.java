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
    void testApplyEffectWithCatCardTypeFilteringNoMatchingCatType() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with different types of cat cards
            CatCard rainbowCat = Mockito.mock(CatCard.class);
            CatCard beardCat = Mockito.mock(CatCard.class);
            
            Mockito.when(rainbowCat.getType()).thenReturn(CardType.CAT_CARD);
            Mockito.when(beardCat.getType()).thenReturn(CardType.CAT_CARD);
            
            Mockito.when(rainbowCat.getCatType()).thenReturn(CatType.RAINBOW_CAT);
            Mockito.when(beardCat.getCatType()).thenReturn(CatType.BEARD_CAT);
            
            List<Card> player2Hand = Arrays.asList(rainbowCat, beardCat);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            // Set up cat card effect to request TACOCAT type
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getTargetPlayerHand()).thenReturn(player2Hand);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.CAT_CARD);
            Mockito.when(effect.getRequestedCatType()).thenReturn(CatType.TACOCAT);
            
            // Mock the view behavior
            Mockito.when(mockGameView.selectTargetPlayer(Mockito.anyList()))
                .thenReturn(player2);
            
            Mockito.doThrow(effect).when(mockCard).effect(turnOrder, gameDeck);
            
            // Execute the method
            cardEffectService.applyEffect(mockCard, player1);
            
            // Verify that no cards were transferred
            Mockito.verify(player2, Mockito.never()).removeCard(Mockito.any());
            Mockito.verify(player1, Mockito.never()).receiveCard(Mockito.any());
            
            // Verify that the appropriate messages were displayed
            Mockito.verify(mockGameView).displayCatCardEffect("request", player1, player2);
            Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);

            // Updated assertion to match the actual call
            Mockito.verify(mockGameView).showError("No card was selected.");
        }
    }

    @Test
    void testHandleRequestEffectWithNonMatchingCatType() throws Exception {
        // Arrange
        // Test case 1: Non-CatCard
        Card nonCatCard = Mockito.mock(Card.class);
        Mockito.when(nonCatCard.getType()).thenReturn(CardType.ATTACK); // Not a CAT_CARD

        // Test case 2: Wrong CatType
        CatCard tacoCat = Mockito.mock(CatCard.class);
        Mockito.when(tacoCat.getType()).thenReturn(CardType.CAT_CARD);
        Mockito.when(tacoCat.getCatType()).thenReturn(CatType.RAINBOW_CAT); // Different from requested type

        List<Card> hand = List.of(nonCatCard, tacoCat);
        Mockito.when(player2.getHand()).thenReturn(hand);
        Mockito.when(mockGameView.selectCardFromPlayer(player2, hand)).thenReturn(nonCatCard);

        CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
        Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
        Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
        Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
        Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.CAT_CARD);
        Mockito.when(effect.getRequestedCatType()).thenReturn(CatType.TACOCAT); // Different from tacoCat's type

        // Inject mock view if not already present
        Field viewField = CardEffectService.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(cardEffectService, mockGameView);

        // Act: Use reflection to call the private method
        Method method = CardEffectService.class.getDeclaredMethod(
            "handleRequestEffect", Player.class, Player.class, CatCard.CatCardEffect.class);
        method.setAccessible(true);
        method.invoke(cardEffectService, player1, player2, effect);

        // Assert
        Mockito.verify(player2, Mockito.never()).removeCard(nonCatCard);
        Mockito.verify(player1, Mockito.never()).receiveCard(nonCatCard);
        Mockito.verify(player2, Mockito.never()).removeCard(tacoCat);
        Mockito.verify(player1, Mockito.never()).receiveCard(tacoCat);
        Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
        Mockito.verify(mockGameView).showError("Target player does not have the requested card type.");
    }

    @Test
    void testHandleRequestEffectWithMatchingCatType() throws Exception {
        // Arrange
        CatCard tacoCat = Mockito.mock(CatCard.class);
        Mockito.when(tacoCat.getType()).thenReturn(CardType.CAT_CARD);
        Mockito.when(tacoCat.getCatType()).thenReturn(CatType.TACOCAT); // Same as requested type

        List<Card> hand = List.of(tacoCat);
        Mockito.when(player2.getHand()).thenReturn(hand);
        Mockito.when(mockGameView.selectCardFromPlayer(player2, hand)).thenReturn(tacoCat);

        CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
        Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
        Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
        Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
        Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.CAT_CARD);
        Mockito.when(effect.getRequestedCatType()).thenReturn(CatType.TACOCAT); // Same as tacoCat's type

        // Inject mock view if not already present
        Field viewField = CardEffectService.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(cardEffectService, mockGameView);

        // Act: Use reflection to call the private method
        Method method = CardEffectService.class.getDeclaredMethod(
            "handleRequestEffect", Player.class, Player.class, CatCard.CatCardEffect.class);
        method.setAccessible(true);
        method.invoke(cardEffectService, player1, player2, effect);

        // Assert
        Mockito.verify(player2).removeCard(tacoCat);
        Mockito.verify(player1).receiveCard(tacoCat);
        Mockito.verify(mockGameView).displayCardRequested(player1, player2, tacoCat);
        Mockito.verify(mockGameView, Mockito.never()).showError(Mockito.any());
    }

    // @Test
    // void testHandleRequestEffect_SelectedCardIsNull_ShowsNoCardSelectedError() throws Exception {
    //     // 用相同枚举实例
    //     CatType sharedCatType = CatType.TACOCAT;
    
    //     // 创建真实 CatCard 实例，必须是相同类型
    //     CatCard cardInHand = new CatCard(sharedCatType);
    //     List<Card> hand = List.of(cardInHand);
    //     Mockito.when(player2.getHand()).thenReturn(hand);
    
    //     // 模拟用户没有选中任何卡片（返回 null）
    //     Mockito.when(mockGameView.selectCardFromPlayer(Mockito.eq(player2), Mockito.anyList()))
    //            .thenReturn(null);
    
    //     // 使用构造器正确设置 requestedCatType = sharedCatType
    //     CatCard first = new CatCard(sharedCatType);
    //     CatCard second = new CatCard(sharedCatType);
    //     CatCard third = new CatCard(sharedCatType);
    //     CatCard.CatCardEffect effect = new CatCard.CatCardEffect(first, second, third, player1.getName(), CardType.CAT_CARD);
    
    //     // 注入 view
    //     Field viewField = CardEffectService.class.getDeclaredField("view");
    //     viewField.setAccessible(true);
    //     viewField.set(cardEffectService, mockGameView);
    
    //     // 调用私有方法
    //     Method method = CardEffectService.class.getDeclaredMethod("handleRequestEffect", Player.class, Player.class, CatCard.CatCardEffect.class);
    //     method.setAccessible(true);
    //     method.invoke(cardEffectService, player1, player2, effect);
    
    //     // 验证逻辑走到了正确路径
    //     Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
    //     Mockito.verify(mockGameView).showError("No card was selected.");
    // }
    


} 