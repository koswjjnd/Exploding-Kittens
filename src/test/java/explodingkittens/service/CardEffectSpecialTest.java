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

/**
 * Test class for special card effects in CardEffectService.
 */
class CardEffectSpecialTest {
    
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
     * Test SeeTheFutureView injection
     */
    @Test
    void testSeeTheFutureViewInjection() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Create a real SeeTheFutureCard instance
            SeeTheFutureCard seeTheFutureCard = new SeeTheFutureCard();
            
            // Apply the effect
            cardEffectService.applyEffect(seeTheFutureCard, player1);
            
            // Verify that the effect was called
            Mockito.verify(mockGameView, Mockito.never())
                .displayHandForSelection(
                    Mockito.any(),
                    Mockito.any()
                );
        }
    }

    /**
     * Test cat card request effect with matching cards
     */
    @Test
    void testCatCardRequestEffectWithMatchingCards() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with matching card
            Card matchingCard = Mockito.mock(Card.class);
            Mockito.when(matchingCard.getType()).thenReturn(CardType.ATTACK);
            List<Card> player2Hand = Arrays.asList(matchingCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            // Set up the cat card effect
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCatCard = Mockito.mock(CatCard.class);
            CatCard secondCatCard = Mockito.mock(CatCard.class);
            CatCard thirdCatCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            
            // Mock the card to throw the effect
            Card catCard = Mockito.mock(Card.class);
            Mockito.doThrow(effect)
                .when(catCard)
                .effect(turnOrder, gameDeck);
            
            // Mock view behavior
            Mockito.when(mockGameView.selectCardFromPlayer(
                    player2,
                    Arrays.asList(matchingCard)
                ))
                .thenReturn(matchingCard);
            Mockito.when(mockGameView.selectTargetPlayer(
                    Mockito.anyList()
                ))
                .thenReturn(player2);
            
            // Apply the effect
            cardEffectService.applyEffect(catCard, player1);
            
            // Verify that the correct card was selected and transferred
            Mockito.verify(player2).removeCard(matchingCard);
            Mockito.verify(player1).receiveCard(matchingCard);
            Mockito.verify(mockGameView).displayCardRequested(
                    player1, 
                    player2, 
                    matchingCard
            );
        }
    }

    /**
     * Test cat card request effect with no matching cards
     */
    @Test
    void testCatCardRequestEffectWithNoMatchingCards() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            // Set up player2's hand with non-matching card
            Card nonMatchingCard = Mockito.mock(Card.class);
            Mockito.when(nonMatchingCard.getType()).thenReturn(CardType.SKIP);
            List<Card> player2Hand = Arrays.asList(nonMatchingCard);
            Mockito.when(player2.getHand()).thenReturn(player2Hand);
            
            // Set up the cat card effect
            CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
            CatCard firstCatCard = Mockito.mock(CatCard.class);
            CatCard secondCatCard = Mockito.mock(CatCard.class);
            CatCard thirdCatCard = Mockito.mock(CatCard.class);
            
            Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
            Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
            Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
            Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.ATTACK);
            Mockito.when(effect.getTargetPlayerName()).thenReturn("player2");
            
            // Mock the card to throw the effect
            Card catCard = Mockito.mock(Card.class);
            Mockito.doThrow(effect)
                .when(catCard)
                .effect(turnOrder, gameDeck);
            
            // Mock the view to return the target player
            Mockito.when(mockGameView.selectTargetPlayer(
                    Mockito.anyList()
                ))
                .thenReturn(player2);
            
            // Apply the effect
            cardEffectService.applyEffect(catCard, player1);
            
            // Verify that no cards were transferred
            Mockito.verify(player2, Mockito.never()).removeCard(Mockito.any());
            Mockito.verify(player1, Mockito.never()).receiveCard(Mockito.any());
            Mockito.verify(mockGameView).displayCardRequested(
                    player1, 
                    player2, 
                    null
            );
        }
    }
} 