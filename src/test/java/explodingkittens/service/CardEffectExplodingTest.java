package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
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
 * Test class for exploding kitten card effects in CardEffectService.
 */
class CardEffectExplodingTest {
    
    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private Deck gameDeck;
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

    @Test
    void testExplodingKittenCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Card explodingKittenCard = Mockito.mock(Card.class);
            
            Mockito.when(explodingKittenCard.getType()).thenReturn(CardType.EXPLODING_KITTEN);
            Mockito.when(player1.hasDefuse()).thenReturn(false);
            
            // Mock the card's effect method to set player as not alive
            Mockito.doAnswer(invocation -> {
                player1.setAlive(false);
                return null;
            }).when(explodingKittenCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(explodingKittenCard, player1);
            
            Mockito.verify(explodingKittenCard).effect(turnOrder, gameDeck);
        }
    }

    @Test
    void testDefuseCardEffect() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Card defuseCard = Mockito.mock(Card.class);
            
            Mockito.when(defuseCard.getType()).thenReturn(CardType.DEFUSE);
            
            // Mock the card's effect method (empty as per implementation)
            Mockito.doNothing().when(defuseCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(defuseCard, player1);
            
            Mockito.verify(defuseCard).effect(turnOrder, gameDeck);
        }
    }

    @Test
    void testExplodingKittenCardEffectWithDefuse() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Card explodingKittenCard = Mockito.mock(Card.class);
            
            Mockito.when(explodingKittenCard.getType()).thenReturn(CardType.EXPLODING_KITTEN);
            Mockito.when(player1.hasDefuse()).thenReturn(true);
            
            // Mock the card's effect method (no effect as player has defuse)
            Mockito.doNothing().when(explodingKittenCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(explodingKittenCard, player1);
            
            Mockito.verify(explodingKittenCard).effect(turnOrder, gameDeck);
        }
    }

    @Test
    void testExplodingKittenCardEffectWithoutDefuse() {
        try (MockedStatic<GameContext> mockedStatic = Mockito.mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getGameDeck).thenReturn(gameDeck);
            
            Card explodingKittenCard = Mockito.mock(Card.class);
            
            Mockito.when(explodingKittenCard.getType()).thenReturn(CardType.EXPLODING_KITTEN);
            Mockito.when(player1.hasDefuse()).thenReturn(false);
            
            // Mock the card's effect method to set player as not alive
            Mockito.doAnswer(invocation -> {
                player1.setAlive(false);
                return null;
            }).when(explodingKittenCard).effect(turnOrder, gameDeck);
            
            cardEffectService.applyEffect(explodingKittenCard, player1);
            
            Mockito.verify(explodingKittenCard).effect(turnOrder, gameDeck);
        }
    }
} 