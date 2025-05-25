package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.service.CardEffectService;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurnServiceTest {

    private TurnService turnService;
    private MockedStatic<GameContext> mockedGameContext;

    @Mock
    private Player player;

    @Mock
    private GameContext gameContext;

    @Mock
    private Deck deck;

    @Mock
    private Card card;

    @Mock
    private GameView view;

    @Mock
    private CardEffectService cardEffectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        turnService = new TurnService(view, cardEffectService);
        mockedGameContext = mockStatic(GameContext.class);
        mockedGameContext.when(GameContext::getGameDeck).thenReturn(deck);
    }

    @AfterEach
    void tearDown() {
        mockedGameContext.close();
    }

    @Test
    void takeTurn_NullPlayer_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            turnService.takeTurn(null, gameContext));
    }

    @Test
    void takeTurn_EmptyHand_OnlyDrawsCard() throws EmptyDeckException {
        when(player.getHand()).thenReturn(new ArrayList<>());
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(player).receiveCard(card);
        verify(view, never()).selectCardToPlay(any(), any());
    }

    @Test
    void takeTurn_WithCards_PlaysAndDraws() throws EmptyDeckException, InvalidCardException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(card, null);
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(cardEffectService).executeCardEffect(card, player, gameContext);
        verify(player).receiveCard(card);
    }

    @Test
    void takeTurn_DrawsExplodingKitten_HandlesExplosion() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(true);
        when(view.selectExplodingKittenPosition()).thenReturn(0);
        
        turnService.takeTurn(player, gameContext);
        
        verify(player).useDefuse();
        verify(deck).insertAt(explodingKitten, 0);
    }

    @Test
    void takeTurn_CardNoped_EffectNotExecuted() throws EmptyDeckException, InvalidCardException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(card, null);
        when(view.checkForNope(player, card)).thenReturn(true);
        
        turnService.takeTurn(player, gameContext);
        
        verify(cardEffectService, never()).executeCardEffect(any(), any(), any());
        verify(view).showCardNoped(player, card);
    }

    @Test
    void takeTurn_EmptyDeck_ThrowsException() throws EmptyDeckException {
        when(deck.drawOne()).thenThrow(new EmptyDeckException());
        
        assertThrows(EmptyDeckException.class, () -> 
            turnService.takeTurn(player, gameContext));
    }

    @Test
    void takeTurn_PlayerEndsTurn_NoMoreCardsPlayed() throws EmptyDeckException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(null);
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(cardEffectService, never()).executeCardEffect(any(), any(), any());
        verify(player).receiveCard(card);
    }

    // Tests for playCard method
    @Test
    void playCard_NullPlayer_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(null, card, gameContext));
    }

    @Test
    void playCard_NullCard_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(player, null, gameContext));
    }

    @Test
    void playCard_NullContext_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(player, card, null));
    }

    @Test
    void playCard_ValidCard_ExecutesEffect() throws InvalidCardException {
        List<Card> hand = spy(new ArrayList<>());
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(false);

        turnService.playCard(player, card, gameContext);

        verify(view).showCardPlayed(player, card);
        verify(cardEffectService).executeCardEffect(card, player, gameContext);
        verify(hand).remove(card);
    }

    @Test
    void playCard_CardNoped_EffectNotExecuted() throws InvalidCardException {
        List<Card> hand = spy(new ArrayList<>());
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(true);

        turnService.playCard(player, card, gameContext);

        verify(view).showCardPlayed(player, card);
        verify(view).showCardNoped(player, card);
        verify(cardEffectService, never()).executeCardEffect(any(), any(), any());
        verify(hand).remove(card);
    }

    @Test
    void playCard_InvalidCard_ThrowsException() {
        List<Card> hand = spy(new ArrayList<>());
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(false);
        doThrow(new RuntimeException("Invalid card")).when(cardEffectService)
            .executeCardEffect(card, player, gameContext);

        assertThrows(RuntimeException.class, () ->
            turnService.playCard(player, card, gameContext));
    }

    // Tests for drawCard method
    @Test
    void drawCard_NullPlayer_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.drawCard(null, gameContext));
    }
    
} 
