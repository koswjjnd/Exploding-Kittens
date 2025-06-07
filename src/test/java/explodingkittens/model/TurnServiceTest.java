package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.service.CardEffectService;
import explodingkittens.service.TurnService;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
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

    @Mock
    private Card nopeCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        turnService = new TurnService(view, cardEffectService);

        mockedGameContext = mockStatic(GameContext.class);
        mockedGameContext.when(GameContext::getGameDeck).thenReturn(deck);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));   // ★
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);        // ★
        when(player.hasCardOfType(CardType.NOPE)).thenReturn(true);
        when(player.removeCardOfType(CardType.NOPE)).thenReturn(nopeCard);
    }


    @AfterEach
    void tearDown() {
        mockedGameContext.close();
    }

    @Test
    void testTakeTurnWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> 
            turnService.takeTurn(null));
    }

    @Test
    void testTakeTurnWithEmptyHand() throws EmptyDeckException {
        // Setup
        List<Card> emptyHand = new ArrayList<>();
        when(player.getHand()).thenReturn(emptyHand);
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getLeftTurns()).thenReturn(1);
        
        // Mock view to skip card playing phase
        when(view.promptPlayerAction(player)).thenReturn("draw");
        
        // Mock deck to return a normal card
        when(deck.drawOne()).thenReturn(card);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getGameDeck).thenReturn(deck);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(player).receiveCard(card);
        verify(view, never()).selectCardToPlay(any(), any());
    }

    @Test
    void testTakeTurnWithCards() throws EmptyDeckException, InvalidCardException {
        // Setup player with cards
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getLeftTurns()).thenReturn(1);
        
        // Mock view to play a card and then end turn
        when(view.promptPlayerAction(player)).thenReturn("play");
        when(view.selectCardToPlay(player, hand)).thenReturn(card, null);
        when(view.checkForNope(player, card)).thenReturn(false);
        
        // Mock deck to return a normal card
        when(deck.drawOne()).thenReturn(card);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getGameDeck).thenReturn(deck);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(cardEffectService).applyEffect(card, player);
        verify(player).receiveCard(card);
    }

    @Test
    void testTakeTurnDrawsExplodingKitten() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(true);
        when(view.selectExplodingKittenPosition(deck.size())).thenReturn(0);
        
        turnService.takeTurn(player);
        
        verify(player).useDefuse();
        verify(deck).insertAt(explodingKitten, 0);
    }

    @Test
    void testTakeTurnEmptyDeck() throws EmptyDeckException {
        when(deck.drawOne()).thenThrow(new EmptyDeckException());
        
        assertThrows(EmptyDeckException.class, () -> 
            turnService.takeTurn(player));
    }

    @Test
    void testTakeTurnPlayerEndsTurn() throws EmptyDeckException {
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(null);
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player);
        
        verify(cardEffectService, never()).applyEffect(any(), any());
        verify(player).receiveCard(card);
    }

    @Test
    void testPlayCardWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(null, card));
    }

    @Test
    void testPlayCardWithNullCard() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCard(player, null));
    }

    @Test
    void testPlayCardWithValidCard() throws InvalidCardException {
        List<Card> hand = spy(new ArrayList<>());
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(false);

        turnService.playCard(player, card);

        verify(view).showCardPlayed(player, card);
        verify(cardEffectService).applyEffect(card, player);
        verify(hand).remove(card);
    }

    @Test
    void testPlayCardWithInvalidCard() {
        List<Card> hand = spy(new ArrayList<>());
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(false);
        doThrow(new RuntimeException("Invalid card")).when(cardEffectService)
            .applyEffect(card, player);

        assertThrows(RuntimeException.class, () ->
            turnService.playCard(player, card));
    }

    @Test
    void testDrawCardWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.drawPhase(null));
    }


    @Test
    void testDrawCardWithNormalCard() throws EmptyDeckException {
        when(deck.drawOne()).thenReturn(card);
        
        turnService.drawPhase(player);
        
        verify(view).showCardDrawn(player, card);
        verify(player).receiveCard(card);
    }

    @Test
    void testDrawCardWithEmptyDeck() throws EmptyDeckException {
        when(deck.drawOne()).thenThrow(new EmptyDeckException());
        
        assertThrows(EmptyDeckException.class, () ->
            turnService.drawPhase(player));
    }

    @Test
    void testDrawCardWithExplodingKittenAndDefuse() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(true);
        when(view.selectExplodingKittenPosition(deck.size())).thenReturn(0);
        
        turnService.drawPhase(player);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).useDefuse();
        verify(deck).insertAt(explodingKitten, 0);
    }

    @Test
    void testDrawCardWithExplodingKittenWithoutDefuse() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(false);
        
        turnService.drawPhase(player);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).setAlive(false);
    }

    @Test
    void testDrawCardWithExplodingKittenAndDefuseButRefuses() throws EmptyDeckException {
        ExplodingKittenCard explodingKitten = mock(ExplodingKittenCard.class);
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(false);
        
        turnService.drawPhase(player);
        
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
        turnService.takeTurn(player);
        assertEquals(1, turnOrder.size());
        assertEquals(player, turnOrder.get(0));
    }

} 
