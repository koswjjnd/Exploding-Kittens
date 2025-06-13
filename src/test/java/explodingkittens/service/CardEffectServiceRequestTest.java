package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.Player;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Test class for request-related functionality in CardEffectService.
 */
class CardEffectServiceRequestTest {
    
    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private GameView mockGameView;
    @Mock
    private CatCard firstCatCard;
    @Mock
    private CatCard secondCatCard;
    @Mock
    private CatCard thirdCatCard;
    
    private CardEffectService cardEffectService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardEffectService = new CardEffectService(mockGameView);
        
        // Set up common mock behaviors
        Mockito.when(player1.getName()).thenReturn("player1");
        Mockito.when(player2.getName()).thenReturn("player2");
        Mockito.when(player1.isAlive()).thenReturn(true);
        Mockito.when(player2.isAlive()).thenReturn(true);
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

    @Test
    void testHandleRequestEffectSelectedCardIsNullShowsNoCardSelectedError() throws Exception {
        CatType sharedType = CatType.TACOCAT;
        CatCard cardInHand = new CatCard(sharedType);
        List<Card> hand = List.of(cardInHand);
    
        Mockito.when(player2.getHand()).thenReturn(hand);
        Mockito.when(mockGameView.selectCardFromPlayer(Mockito.eq(player2), Mockito.anyList()))
               .thenReturn(null);
    
        CatCard card1 = new CatCard(sharedType);
        CatCard card2 = new CatCard(sharedType);
        CatCard card3 = new CatCard(sharedType);
        CatCard.CatCardEffect effect = new CatCard.CatCardEffect(card1, card2, card3, player1.getName(), CardType.CAT_CARD);
    
        Field viewField = CardEffectService.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(cardEffectService, mockGameView);
    
        Method method = CardEffectService.class.getDeclaredMethod(
            "handleRequestEffect", Player.class, Player.class, CatCard.CatCardEffect.class);
        method.setAccessible(true);
        method.invoke(cardEffectService, player1, player2, effect);
    
        Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
        Mockito.verify(mockGameView).showError("No card was selected.");
    }

    @Test
    void testApplyEffectWithCatCardTypeFilteringNoMatchingCatType() throws Exception {
        // Set up player2's hand with different types of cat cards
        CatCard rainbowCat = Mockito.mock(CatCard.class);
        CatCard beardCat = Mockito.mock(CatCard.class);
        
        Mockito.when(rainbowCat.getType()).thenReturn(CardType.CAT_CARD);
        Mockito.when(beardCat.getType()).thenReturn(CardType.CAT_CARD);
        
        Mockito.when(rainbowCat.getCatType()).thenReturn(CatType.RAINBOW_CAT);
        Mockito.when(beardCat.getCatType()).thenReturn(CatType.BEARD_CAT);
        
        List<Card> hand = List.of(rainbowCat, beardCat);
        Mockito.when(player2.getHand()).thenReturn(hand);
        
        // Set up cat card effect to request TACOCAT type
        CatCard.CatCardEffect effect = Mockito.mock(CatCard.CatCardEffect.class);
        Mockito.when(effect.getFirstCard()).thenReturn(firstCatCard);
        Mockito.when(effect.getSecondCard()).thenReturn(secondCatCard);
        Mockito.when(effect.getThirdCard()).thenReturn(thirdCatCard);
        Mockito.when(effect.getRequestedCardType()).thenReturn(CardType.CAT_CARD);
        Mockito.when(effect.getRequestedCatType()).thenReturn(CatType.TACOCAT);
        
        // Inject mock view if not already present
        Field viewField = CardEffectService.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(cardEffectService, mockGameView);
        
        // Act: Use reflection to call the private method
        Method method = CardEffectService.class.getDeclaredMethod(
            "handleRequestEffect", Player.class, Player.class, CatCard.CatCardEffect.class);
        method.setAccessible(true);
        method.invoke(cardEffectService, player1, player2, effect);
        
        // Verify that no cards were transferred
        Mockito.verify(player2, Mockito.never()).removeCard(Mockito.any());
        Mockito.verify(player1, Mockito.never()).receiveCard(Mockito.any());
        
        // Verify that the appropriate messages were displayed
        Mockito.verify(mockGameView).displayCardRequested(player1, player2, null);
        Mockito.verify(mockGameView).showError("Target player does not have the requested card type.");
    }
} 