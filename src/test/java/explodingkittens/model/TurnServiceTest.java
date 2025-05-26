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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
    void testTakeTurnWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> 
            turnService.takeTurn(null, gameContext));
    }

    @Test
    void testTakeTurnWithEmptyHand() throws EmptyDeckException {
        when(player.getHand()).thenReturn(new ArrayList<>());
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(player).receiveCard(card);
        verify(view, never()).selectCardToPlay(any(), any());
    }

    @Test
    void testTakeTurnWithCards() throws EmptyDeckException, InvalidCardException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(card, (Card) null);
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(cardEffectService).executeCardEffect(card, player, gameContext);
        verify(player).receiveCard(card);
    }

    @Test
    void testTakeTurnDrawsExplodingKitten() throws EmptyDeckException {
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
    void testTakeTurnCardNoped() throws EmptyDeckException, InvalidCardException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(card, (Card) null);
        when(view.checkForNope(player, card)).thenReturn(true);
        
        turnService.takeTurn(player, gameContext);
        
        verify(cardEffectService, never()).executeCardEffect(any(), any(), any());
        verify(view).showCardNoped(player, card);
    }

    @Test
    void testTakeTurnEmptyDeck() throws EmptyDeckException {
        when(deck.drawOne()).thenThrow(new EmptyDeckException());
        
        assertThrows(EmptyDeckException.class, () -> 
            turnService.takeTurn(player, gameContext));
    }

    @Test
    void testTakeTurnPlayerEndsTurn() throws EmptyDeckException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(null);
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(cardEffectService, never()).executeCardEffect(any(), any(), any());
        verify(player).receiveCard(card);
    }

    @Test
    void testPlayCardWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(null, card, gameContext));
    }

    @Test
    void testPlayCardWithNullCard() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(player, null, gameContext));
    }

    @Test
    void testPlayCardWithNullContext() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(player, card, null));
    }

    @Test
    void testPlayCardWithValidCard() throws InvalidCardException {
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
    void testPlayCardWithNopedCard() throws InvalidCardException {
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
    void testPlayCardWithInvalidCard() {
        List<Card> hand = spy(new ArrayList<>());
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(false);
        doThrow(new RuntimeException("Invalid card")).when(cardEffectService)
            .executeCardEffect(card, player, gameContext);

        assertThrows(RuntimeException.class, () ->
            turnService.playCard(player, card, gameContext));
    }

    @Test
    void testDrawCardWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.drawCard(null, gameContext));
    }

    @Test
    void testDrawCardWithNullContext() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.drawCard(player, null));
    }

    @Test
    void testDrawCardWithNormalCard() throws EmptyDeckException {
        when(deck.drawOne()).thenReturn(card);
        
        turnService.drawCard(player, gameContext);
        
        verify(view).showCardDrawn(player, card);
        verify(player).receiveCard(card);
    }

    @Test
    void testDrawCardWithEmptyDeck() throws EmptyDeckException {
        when(deck.drawOne()).thenThrow(new EmptyDeckException());
        
        assertThrows(EmptyDeckException.class, () ->
            turnService.drawCard(player, gameContext));
    }

    @Test
    void testDrawCardWithExplodingKittenAndDefuse() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(true);
        when(view.selectExplodingKittenPosition()).thenReturn(0);
        
        turnService.drawCard(player, gameContext);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).useDefuse();
        verify(deck).insertAt(explodingKitten, 0);
    }

    @Test
    void testDrawCardWithExplodingKittenWithoutDefuse() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(false);
        
        turnService.drawCard(player, gameContext);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).setAlive(false);
    }

    @Test
    void testDrawCardWithExplodingKittenAndDefuseButRefuses() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(false);
        
        turnService.drawCard(player, gameContext);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).setAlive(false);
    }
    @Test
    void testTakeTurnUpdatesTurnOrderWhenPlayerAlive() throws EmptyDeckException {
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player);
        when(gameContext.getTurnOrder()).thenReturn(turnOrder);
        when(player.isAlive()).thenReturn(true);
        when(player.getHand()).thenReturn(new ArrayList<>());
        when(deck.drawOne()).thenReturn(card);
        turnService.takeTurn(player, gameContext);
        assertEquals(1, turnOrder.size());
        assertEquals(player, turnOrder.get(0));
    }

    @Test
    void testTakeTurnUpdatesTurnOrderWhenPlayerDead() throws EmptyDeckException {
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player);
        when(gameContext.getTurnOrder()).thenReturn(turnOrder);
        when(player.isAlive()).thenReturn(false);
        when(player.getHand()).thenReturn(new ArrayList<>());
        when(deck.drawOne()).thenReturn(card);
        turnService.takeTurn(player, gameContext);
        assertTrue(turnOrder.isEmpty());
    }
} 
