package explodingkittens.service;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.ExplodingKittenCard;
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
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TurnServiceTest {
    @Mock
    private Player player;
    @Mock
    private Player player2;
    @Mock
    private Deck deck;
    @Mock
    private Card card;
    @Mock
    private Card nopeCard;
    @Mock
    private ExplodingKittenCard explodingKitten;
    @Mock
    private GameView view;
    @Mock
    private CardEffectService cardEffectService;
    @Mock
    private NopeService nopeService;
    @Mock
    private TurnService turnService;

    private MockedStatic<GameContext> mockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        turnService = new TurnService(view, cardEffectService, nopeService);
        
        mockedStatic = mockStatic(GameContext.class);
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(Arrays.asList(player, player2));
        mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player);
        when(player.hasCardOfType(CardType.NOPE)).thenReturn(true);
        when(player.removeCardOfType(CardType.NOPE)).thenReturn(nopeCard);
    }


    @AfterEach
    void tearDown() {
        if (mockedStatic != null) {
            mockedStatic.close();
        }
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
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
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
        when(view.selectCardToPlay(player, hand)).thenReturn(card).thenReturn(null);
        when(view.checkForNope(player, card)).thenReturn(false);
        
        // Mock deck to return a normal card
        when(deck.drawOne()).thenReturn(card);
        
        // Mock GameContext
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(cardEffectService).applyEffect(card, player);
        verify(player).receiveCard(card);
    }
    @Test
    void testTakeTurnDrawsExplodingKitten() throws EmptyDeckException {
        // Setup
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getLeftTurns()).thenReturn(1);
        when(player.hasDefuse()).thenReturn(true);
        
        // Mock view interactions
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(view.confirmDefuse(player)).thenReturn(true);
        when(view.selectExplodingKittenPosition(deck.size())).thenReturn(0);
        
        // Mock deck behavior
        when(deck.drawOne()).thenReturn(explodingKitten);  // Draw exploding kitten
        when(deck.size()).thenReturn(10);
        doNothing().when(deck).insertAt(any(), anyInt());
        when(explodingKitten.getType()).thenReturn(CardType.EXPLODING_KITTEN);
        
        // Mock GameContext
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).useDefuse();
        verify(deck).insertAt(explodingKitten, 0);
        verify(player).setLeftTurns(1);
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player));
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(0));
    }

    @Test
    void testTakeTurnEmptyDeck() throws EmptyDeckException {
        // Setup
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getLeftTurns()).thenReturn(1);
        
        // Mock view interactions
        when(view.promptPlayerAction(player)).thenReturn("draw");
        
        // Mock deck behavior
        when(deck.size()).thenReturn(0);
        when(deck.drawOne()).thenThrow(new EmptyDeckException("Deck is empty"));
        
        // Mock GameContext
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute and verify exception
        assertThrows(EmptyDeckException.class, () -> turnService.takeTurn(player));
        
        // Verify no card was received
        verify(player, never()).receiveCard(any());
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
        // Setup
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(card);
        when(view.checkForNope(player, card)).thenReturn(false);
        
        // Execute
        turnService.playCard(player, card);
        
        // Verify
        verify(cardEffectService).applyEffect(card, player);
        verify(player).removeCard(card);
    }

    @Test
    void testPlayCardWithInvalidCard() {
        // Setup
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(view.checkForNope(player, card)).thenReturn(false);
        doThrow(new RuntimeException("Invalid card"))
            .when(cardEffectService)
            .applyEffect(card, player);
        
        // Execute and verify exception
        assertThrows(InvalidCardException.class, () -> turnService.playCard(player, card));
        
        // Verify
        verify(player, never()).removeCard(any());
    }

    @Test
    void testPlayCardWithNope() throws InvalidCardException {
        // Setup
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.isAlive()).thenReturn(true);

        // Mock NopeService to return true (card is noped)
        when(nopeService.isNegatedByPlayers(card)).thenReturn(true);

        // Mock view behavior
        doNothing().when(view).showCardPlayed(player, card);
        doNothing().when(view).showCardNoped(player, card);

        // Run
        turnService.playCard(player, card);

        // Verify
        verify(view).showCardPlayed(player, card);
        verify(view).showCardNoped(player, card);
        verify(player).removeCard(card);
        verify(cardEffectService, never()).applyEffect(any(), any());
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
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(false);
        
        turnService.drawPhase(player);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).setAlive(false);
    }

    @Test
    void testDrawCardWithExplodingKittenAndDefuseButRefuses() throws EmptyDeckException {
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(false);
        
        turnService.drawPhase(player);
        
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).setAlive(false);
    }

    @Test
    void testTakeTurnUpdatesTurnOrderWhenPlayerAlive() throws EmptyDeckException {
        // Setup
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getLeftTurns()).thenReturn(1);
        
        // Mock view to end turn
        when(view.promptPlayerAction(player)).thenReturn("end");
        
        // Mock GameContext
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player));
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(0));
    }

    @Test
    void testTakeTurnWithGameOver() {
        // Setup
        when(player.getLeftTurns()).thenReturn(1);  // Start with 1 turn
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(explodingKitten);  // Draw exploding kitten
        when(explodingKitten.getType()).thenReturn(CardType.EXPLODING_KITTEN);
        when(player.hasDefuse()).thenReturn(false);  // Player has no defuse card
        when(player.isAlive()).thenReturn(true);  // Player starts alive
        mockedStatic.when(GameContext::isGameOver)
            .thenReturn(false)  // First check: game not over
            .thenReturn(true);  // Second check: game over after exploding kitten
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(Arrays.asList(player));
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(view).showCardDrawn(player, explodingKitten);  // Card drawn
        verify(player).setAlive(false);  // Player eliminated by exploding kitten
        // Left turns should not be set when game is over
        verify(player, never()).setLeftTurns(anyInt());
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player), never());
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(anyInt()), never());
    }

    @Test
    void testTakeTurnDisplayTurnOrder() {
        // Setup
        List<Player> turnOrder = Arrays.asList(player, player2);
        when(player.getLeftTurns()).thenReturn(1);
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(card);
        when(card.getType()).thenReturn(CardType.DEFUSE);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        when(player.getName()).thenReturn("Player1");
        when(player2.getName()).thenReturn("Player2");
        when(player.isAlive()).thenReturn(true);
        when(player2.isAlive()).thenReturn(true);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player), times(1));
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(0), times(1));
    }

    @Test
    void testTakeTurnDisplayTurnOrderWithEliminatedPlayer() {
        // Setup
        List<Player> turnOrder = Arrays.asList(player, player2);
        when(player.getLeftTurns()).thenReturn(1);
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(card);
        when(card.getType()).thenReturn(CardType.DEFUSE);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        when(player.getName()).thenReturn("Player1");
        when(player2.getName()).thenReturn("Player2");
        when(player.isAlive()).thenReturn(true);
        when(player2.isAlive()).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player), times(1));
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(0), times(1));
    }

    @Test
    void testTakeTurnWithGameOverDuringLoop() {
        // Setup
        when(player.getLeftTurns()).thenReturn(2);  // Start with 2 turns
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne())
            .thenReturn(card)  // First draw: normal card
            .thenReturn(explodingKitten);  // Second draw: exploding kitten
        when(card.getType()).thenReturn(CardType.DEFUSE);
        when(explodingKitten.getType()).thenReturn(CardType.EXPLODING_KITTEN);
        when(player.hasDefuse()).thenReturn(false);  // Player has no defuse card
        when(player.isAlive()).thenReturn(true);  // Player starts alive
        mockedStatic.when(GameContext::isGameOver)
            .thenReturn(false)  // First check: game not over
            .thenReturn(true);  // Second check: game over after exploding kitten
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(Arrays.asList(player));
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(view, times(1))
            .showCardDrawn(player, card);  // First card drawn
        verify(view, times(1))
            .showCardDrawn(player, explodingKitten);  // Second card drawn
        verify(player, times(1))
            .receiveCard(card);  // First card received
        verify(player)
            .setAlive(false);  // Player eliminated by exploding kitten
        verify(player, never())
            .setLeftTurns(anyInt());  // Left turns should not be set when game is over
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player), never());
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(anyInt()), never());
    }

    @Test
    void testPlayCardsPhaseWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCardsPhase(null));
    }

    
    @Test
    void testPlayCardsPhaseWithInvalidCard() {
        // Prepare mocks
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.isAlive()).thenReturn(true);

        // Mock view behavior
        when(view.selectCardToPlay(player, hand)).thenReturn(card).thenReturn(null);
        doNothing().when(view).showCardPlayed(player, card);
        doNothing().when(view).showError(anyString());
        doNothing().when(view).displayPlayerHand(player);

        // Static context
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);

        // Mock NopeService behavior
        when(nopeService.isNegatedByPlayers(card)).thenReturn(false);

        // Card effect throws exception
        doThrow(new RuntimeException("Invalid card"))
            .when(cardEffectService)
            .applyEffect(card, player);

        // Run
        turnService.playCardsPhase(player);

        // Verify
        verify(view)
            .showCardPlayed(player, card);
        verify(view)
            .showError("Invalid card");
        verify(player, never())
            .removeCard(any());
        verify(view, atLeastOnce())
            .displayPlayerHand(player);
        verify(view, atLeastOnce())
            .selectCardToPlay(player, hand);
        verify(cardEffectService)
            .applyEffect(card, player);
    }

    @Test
    void testTwoParameterConstructor() {
        // Create a new TurnService with two parameters
        TurnService service = new TurnService(view, cardEffectService);
        
        // Verify that the service is created with a non-null NopeService
        assertNotNull(service.getNopeService());
    }

    @Test
    void testDrawPhaseWithNullGameDeck() {
        // Setup
        when(player.getName()).thenReturn("TestPlayer");
        when(player.isAlive()).thenReturn(true);

        // Mock GameContext to return null for game deck
        mockedStatic.when(GameContext::getGameDeck).thenReturn(null);

        // Execute and verify exception
        assertThrows(
            IllegalArgumentException.class,
            () -> turnService.drawPhase(player)
        );

        // Verify no card was received
        verify(view, never())
            .showCardDrawn(any(), any());
        verify(player, never())
            .receiveCard(any());
    }

} 
 