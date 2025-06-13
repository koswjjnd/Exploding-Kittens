package explodingkittens.service;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.ExplodingKittenCard;
import explodingkittens.model.DrawFromBottomCard;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

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
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

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

        // Redirect System.out
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        if (mockedStatic != null) {
            mockedStatic.close();
        }
        System.setOut(originalOut);
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
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(deck.size()).thenReturn(10);
        doNothing().when(deck).insertAt(any(), anyInt());
        
        // Mock GameContext
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(player).useDefuse();
        verify(deck).insertAt(explodingKitten, 0);
        verify(player).setLeftTurns(1);
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
    void testPlayCardWithNope() throws InvalidCardException {
        // Setup
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.isAlive()).thenReturn(true);

        // Mock NopeService to return true (card is noped)
        when(nopeService.isNegatedByPlayers(card)).thenReturn(true);

        // Execute
        turnService.playCard(player, card);

        // Verify
        verify(nopeService).isNegatedByPlayers(card);
        verify(view).showCardNoped(player, card);
        verify(player).removeCard(card);
        verify(view, never()).showCardPlayed(any(), any());
        verify(cardEffectService, never()).applyEffect(any(), any());
    }

    @Test
    void testPlayCardWithInvalidCard() {
        // Setup
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        when(player.getHand()).thenReturn(hand);
        doThrow(new RuntimeException("Invalid card"
        )).when(cardEffectService).applyEffect(card, player);

        // Execute and verify exception
        assertThrows(InvalidCardException.class, () -> turnService.playCard(player, card));

        // Verify
        verify(cardEffectService).applyEffect(card, player);
        verify(view, never()).showCardPlayed(any(), any());
        verify(player, never()).removeCard(any());
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
        when(deck.drawOne()).thenReturn(explodingKitten);
        when(explodingKitten.getType()).thenReturn(CardType.EXPLODING_KITTEN);
        when(player.hasDefuse()).thenReturn(false);  // Player has no defuse card
        when(player.isAlive()).thenReturn(true);  // Player starts alive
        
        // Game ends after player is eliminated
        mockedStatic.when(GameContext::isGameOver)
            .thenReturn(false)
            .thenReturn(true);
        mockedStatic.when(GameContext::getTurnOrder)
            .thenReturn(Arrays.asList(player));
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(view).showCardDrawn(player, explodingKitten);
        verify(player).setAlive(false);  // Player should be eliminated
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
        
        // First check: game not over, Second check: game over after exploding kitten
        mockedStatic.when(GameContext::isGameOver)
            .thenReturn(false)
            .thenReturn(true);
        mockedStatic.when(GameContext::getTurnOrder)
            .thenReturn(Arrays.asList(player));
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(view, times(1)).showCardDrawn(player, card);  // First card drawn
        verify(view, times(1)).showCardDrawn(player, explodingKitten);  // Second card drawn
        verify(player, times(1)).receiveCard(card);  // First card received
        verify(player).setAlive(false);  // Player eliminated by exploding kitten
        // Left turns should not be set when game is over
        verify(player, never()).setLeftTurns(anyInt());
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player), never());
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(anyInt()), never());
    }

    @Test
    void testPlayCardsPhaseWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () ->
            turnService.playCardsPhase(null));
    }

    

    @Test
    void testTwoParameterConstructor() {
        // Create a new TurnService with two parameters
        TurnService service = new TurnService(view, cardEffectService);
        
        // Verify that the service is created with a new NopeService
        NopeService nopeService = service.getNopeService();
        assertNotNull(nopeService, "NopeService should be initialized");
    }

    @Test
    void testDrawPhaseWithNullGameDeck() {
        // Setup
        when(player.getName()).thenReturn("TestPlayer");
        when(player.isAlive()).thenReturn(true);

        // Mock GameContext to return null for game deck
        mockedStatic.when(GameContext::getGameDeck).thenReturn(null);

        // Execute and verify exception
        assertThrows(IllegalArgumentException.class, () -> 
            turnService.drawPhase(player));

        // Verify
        verify(view, never()).showCardDrawn(any(), any());
        verify(player, never()).receiveCard(any());
    }

    @Test
    void testPlayCardsPhaseWithDrawFromBottomCard() throws InvalidCardException {
        // Setup
        List<Card> hand = new ArrayList<>();
        DrawFromBottomCard drawFromBottomCard = mock(DrawFromBottomCard.class);
        hand.add(drawFromBottomCard);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand)).thenReturn(drawFromBottomCard);
        when(nopeService.isNegatedByPlayers(drawFromBottomCard)).thenReturn(false);
        
        // Execute
        turnService.playCardsPhase(player);
        
        // Verify
        verify(cardEffectService).applyEffect(drawFromBottomCard, player);
        verify(player).removeCard(drawFromBottomCard);
        verify(view).showCardPlayed(player, drawFromBottomCard);
        // Verify that selectCardToPlay is only called once since we break after DrawFromBottomCard
        verify(view, times(1)).selectCardToPlay(player, hand);
    }

    @Test
    void testPlayCardsPhaseWithInvalidCard() {
        // Setup
        List<Card> hand = new ArrayList<>();
        Card invalidCard = mock(Card.class);
        hand.add(invalidCard);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand))
            .thenReturn(invalidCard)  // First attempt: invalid card
            .thenReturn(null);        // Second attempt: end play phase
        when(nopeService.isNegatedByPlayers(invalidCard)).thenReturn(false);
        
        // Mock card effect service to throw runtime exception
        doThrow(new RuntimeException("Invalid card effect"))
            .when(cardEffectService).applyEffect(invalidCard, player);
        
        // Execute
        turnService.playCardsPhase(player);
        
        // Verify
        verify(view).showError("Invalid card effect");
        verify(view, times(1)).displayPlayerHand(player); // Only once after error
        verify(player, never()).removeCard(any()); // Card should not be removed
        verify(view, never()).showCardPlayed(any(), any()); // Card should not be shown as played
    }

    @Test
    void testPlayCardsPhaseWithRuntimeException() {
        // Setup
        List<Card> hand = new ArrayList<>();
        Card errorCard = mock(Card.class);
        hand.add(errorCard);
        when(player.getHand()).thenReturn(hand);
        when(view.selectCardToPlay(player, hand))
            .thenReturn(errorCard)  // First attempt: card that causes runtime error
            .thenReturn(null);      // Second attempt: end play phase
        when(nopeService.isNegatedByPlayers(errorCard)).thenReturn(false);
        
        // Mock card effect service to throw runtime exception
        doThrow(new RuntimeException("Unexpected error"))
            .when(cardEffectService).applyEffect(errorCard, player);
        
        // Execute
        turnService.playCardsPhase(player);
        
        // Verify
        verify(view).showError("Unexpected error");
        verify(view, times(1)).displayPlayerHand(player); // Only once after error
        verify(player, never()).removeCard(any()); // Card should not be removed
        verify(view, never()).showCardPlayed(any(), any()); // Card should not be shown as played
    }

    @Test
    void testTakeTurnDisplaysPlayerHand() throws EmptyDeckException {
        // Setup
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.getLeftTurns()).thenReturn(1);
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(card);
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify
        verify(view, atLeastOnce()).displayPlayerHand(player);
    }

    @Test
    void testTakeTurnDisplaysTurnOrder() throws EmptyDeckException {
        // Setup
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player2.getName()).thenReturn("TestPlayer2");
        when(player.getLeftTurns()).thenReturn(1);
        when(player2.isAlive()).thenReturn(true);
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(card);
        
        // Mock GameContext
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        mockedStatic.when(GameContext::getTurnOrder)
            .thenReturn(Arrays.asList(player, player2))
            .thenReturn(Arrays.asList(player2, player)); // After movePlayerToEnd
        mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player2);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify turn order is displayed
        String output = outContent.toString(StandardCharsets.UTF_8);
        System.out.println("Actual output: " + output); // Debug output
        
        assertTrue(output.contains("Current turn order:"), "Should display current turn order");
        assertTrue(output.contains("1. TestPlayer"), "Should display first player");
        assertTrue(output.contains("2. TestPlayer2"), "Should display second player");
        assertTrue(output.contains("Turn order after player TestPlayer's turn:"), "Should display turn order after move");
        assertTrue(output.contains("Next player will be: TestPlayer2"), "Should display next player");
    }

    @Test
    void testTakeTurnDisplaysTurnOrderWithFormatting() throws EmptyDeckException {
        // Setup
        when(player.isAlive()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");
        when(player2.getName()).thenReturn("TestPlayer2");
        when(player.getLeftTurns()).thenReturn(1);
        when(player2.isAlive()).thenReturn(false); // player2 is eliminated
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(card);
        
        // Mock GameContext
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        mockedStatic.when(GameContext::getTurnOrder)
            .thenReturn(Arrays.asList(player, player2))
            .thenReturn(Arrays.asList(player2, player)); // After movePlayerToEnd
        mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player2);
        
        // Execute
        turnService.takeTurn(player);
        
        // Verify turn order is displayed with correct formatting
        String output = outContent.toString(StandardCharsets.UTF_8);
        
        // Verify current turn order display
        assertTrue(output.contains("Current turn order:"), "Should display current turn order header");
        assertTrue(output.contains("1. TestPlayer"), "Should display first player without eliminated status");
        assertTrue(output.contains("2. TestPlayer2 (Eliminated)"), "Should display second player with eliminated status");
        
        // Verify turn order after move
        assertTrue(output.contains("Turn order after player TestPlayer's turn:"), "Should display turn order after move header");
        assertTrue(output.contains("1. TestPlayer2 (Eliminated)"), "Should display eliminated player first");
        assertTrue(output.contains("2. TestPlayer"), "Should display active player second");
        
        // Verify next player announcement
        assertTrue(output.contains("Next player will be: TestPlayer2"), "Should display next player");
        
        // Verify newlines
        assertTrue(output.contains("\n\n"), "Should have double newlines between sections");
    }

    @Test
    void testHandleExplodingKittenDisplaysDefuseSuccess() {
        // Setup
        ExplodingKittenCard ek = mock(ExplodingKittenCard.class);
        when(player.hasDefuse()).thenReturn(true);
        when(view.confirmDefuse(player)).thenReturn(true);
        when(view.selectExplodingKittenPosition(anyInt())).thenReturn(0);
        when(deck.size()).thenReturn(10);
        
        // Execute
        turnService.handleExplodingKitten(player, ek);
        
        // Verify
        verify(view).displayDefuseSuccess(player, 0);
        verify(deck).insertAt(ek, 0);
        verify(player).useDefuse();
    }

    @Test
    void testDrawPhaseDisplaysPlayerOrder() throws EmptyDeckException {
        // Setup
        when(player.getName()).thenReturn("TestPlayer");
        when(player2.getName()).thenReturn("TestPlayer2");
        when(player.isAlive()).thenReturn(true);
        when(player2.isAlive()).thenReturn(true);
        when(deck.drawOne()).thenReturn(card);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(Arrays.asList(player, player2));
        
        // Execute
        turnService.drawPhase(player);
        
        // Verify player order is displayed
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Current player order after drawing:"));
        assertTrue(output.contains("1. TestPlayer (Current)"));
        assertTrue(output.contains("2. TestPlayer2"));
    }

    @Test
    void testHandleExplodingKittenDisplaysPlayerOrder() {
        // Setup
        ExplodingKittenCard ek = mock(ExplodingKittenCard.class);
        when(player.getName()).thenReturn("TestPlayer");
        when(player2.getName()).thenReturn("TestPlayer2");
        when(player.isAlive()).thenReturn(true);
        when(player2.isAlive()).thenReturn(true);
        when(player.hasDefuse()).thenReturn(false);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(Arrays.asList(player, player2));
        
        // Execute
        turnService.handleExplodingKitten(player, ek);
        
        // Verify player order is displayed
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Current player order after handling exploding kitten:"));
        assertTrue(output.contains("1. TestPlayer (Current)"));
        assertTrue(output.contains("2. TestPlayer2"));
    }

    @Test
    void testHandleExplodingKittenDisplaysPlayerStatusAndNewlines() {
        // Setup
        ExplodingKittenCard ek = mock(ExplodingKittenCard.class);
        when(player.getName()).thenReturn("TestPlayer");
        when(player2.getName()).thenReturn("TestPlayer2");
        when(player.isAlive()).thenReturn(true);
        when(player2.isAlive()).thenReturn(false); // player2 is eliminated
        when(player.hasDefuse()).thenReturn(false);
        
        // Mock GameContext with multiple players
        List<Player> turnOrder = Arrays.asList(player, player2);
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        
        // Execute
        turnService.handleExplodingKitten(player, ek);
        
        // Verify player order is displayed with correct formatting
        String output = outContent.toString(StandardCharsets.UTF_8);
        String[] lines = output.split("\n");
        // Verify the exact format of the output
        // Verify header
        assertTrue(lines[0].isEmpty(), "Should start with a newline");
        assertEquals("Current player order after handling exploding kitten:", lines[1].trim(),
            "Should have correct header");
        
        // Verify player list
        assertEquals("1. TestPlayer (Current)", lines[2].trim(),
            "Should display current player correctly");
        assertEquals("2. TestPlayer2 (Eliminated)", lines[3].trim(),
            "Should display eliminated player correctly");
        
        // 更健壮的结尾换行判断
        assertTrue(output.endsWith("\n"), "Raw output should end with a newline");
        
        // Verify player elimination
        verify(player).setAlive(false);
        verify(view).displayPlayerEliminated(player);
    }

    @Test
    void testPlayCardsPhaseRefreshesHandDisplay() throws InvalidCardException {
        // Setup
        List<Card> hand = new ArrayList<>();
        Card playableCard = mock(Card.class);
        hand.add(playableCard);
        when(player.getHand()).thenReturn(hand);
        
        // Mock view to play one card and then end
        when(view.selectCardToPlay(player, hand))
            .thenReturn(playableCard)  // First call: play a card
            .thenReturn(null);         // Second call: end play phase
        when(nopeService.isNegatedByPlayers(playableCard)).thenReturn(false);
        
        // Execute
        turnService.playCardsPhase(player);
        
        // Verify
        // Should display hand before playing card
        verify(view, times(2)).selectCardToPlay(player, hand);  // Called twice: once for playing, once for ending
        // Should display hand after playing card
        verify(view, times(1)).displayPlayerHand(player);
        // Should apply card effect
        verify(cardEffectService).applyEffect(playableCard, player);
        // Should remove the played card
        verify(player).removeCard(playableCard);
        // Should show the played card
        verify(view).showCardPlayed(player, playableCard);
    }

    @Test
    void testPrintTurnOrderFormattingAndStatus() {
        // Setup
        when(player.getName()).thenReturn("Alice");
        when(player2.getName()).thenReturn("Bob");
        when(player.isAlive()).thenReturn(true);
        when(player2.isAlive()).thenReturn(false); // Bob淘汰
        when(player.getLeftTurns()).thenReturn(1);
        when(player.hasDefuse()).thenReturn(false);
        
        // Mock view interactions
        when(view.promptPlayerAction(player)).thenReturn("draw");
        when(deck.drawOne()).thenReturn(card);
        
        // Mock GameContext with multiple players to ensure loop executes
        List<Player> turnOrder = Arrays.asList(player, player2, player, player2); // 4 players to ensure loop
        mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedStatic.when(GameContext::getGameDeck).thenReturn(deck);
        mockedStatic.when(GameContext::isGameOver).thenReturn(false);
        mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player2);
        
        // Execute
        turnService.takeTurn(player);
        
        String output = outContent.toString(StandardCharsets.UTF_8);
        String[] lines = output.split("\n");
        
        // 验证循环执行了正确的次数
        int playerCount = 0;
        for (String line : lines) {
            if (line.trim().matches("\\d+\\. .*")) { // 匹配 "数字. 玩家名" 格式
                playerCount++;
            }
        }
        // 由于takeTurn会打印三次玩家顺序，所以总数应该是12（4个玩家 * 3次打印）
        assertEquals(12, playerCount, "应该打印12个玩家的信息（4个玩家 * 3次打印）");
        
        // 验证第一次打印（当前顺序）
        assertTrue(output.contains("Current turn order:"), "应该包含当前顺序标题");
        assertTrue(output.contains("1. Alice") && !output.contains("1. Alice (Eliminated)"), "存活玩家不应有(Eliminated)");
        assertTrue(output.contains("2. Bob (Eliminated)"), "淘汰玩家应有(Eliminated)");
        assertTrue(output.contains("3. Alice") && !output.contains("3. Alice (Eliminated)"), "存活玩家不应有(Eliminated)");
        assertTrue(output.contains("4. Bob (Eliminated)"), "淘汰玩家应有(Eliminated)");
        
        // 验证第二次打印（抽牌后顺序）
        assertTrue(output.contains("Current player order after drawing:"), "应该包含抽牌后顺序标题");
        
        // 验证第三次打印（移动后的顺序）
        assertTrue(output.contains("Turn order after player Alice's turn:"), "应该包含移动后顺序标题");
        assertTrue(output.contains("Next player will be: Bob"), "应该显示下一个玩家");
        
        // 验证换行符
        // 1. 验证当前顺序后的换行
        int currentOrderIndex = output.indexOf("Current turn order:");
        assertTrue(currentOrderIndex >= 0, "找不到当前顺序标题");
        String currentOrderSection = output.substring(currentOrderIndex);
        int currentOrderNewlineIndex = currentOrderSection.indexOf("\n", currentOrderSection.indexOf("Current turn order:") + "Current turn order:".length());
        assertTrue(currentOrderNewlineIndex > 0, "当前顺序标题后应该有换行");
        
        // 2. 验证抽牌后顺序后的换行
        int afterDrawingIndex = output.indexOf("Current player order after drawing:");
        assertTrue(afterDrawingIndex >= 0, "找不到抽牌后顺序标题");
        String afterDrawingSection = output.substring(afterDrawingIndex);
        int afterDrawingNewlineIndex = afterDrawingSection.indexOf("\n", afterDrawingSection.indexOf("Current player order after drawing:") + "Current player order after drawing:".length());
        assertTrue(afterDrawingNewlineIndex > 0, "抽牌后顺序标题后应该有换行");
        
        // 3. 验证移动后顺序后的换行
        int afterMoveIndex = output.indexOf("Turn order after player Alice's turn:");
        assertTrue(afterMoveIndex >= 0, "找不到移动后顺序标题");
        String afterMoveSection = output.substring(afterMoveIndex);
        int afterMoveNewlineIndex = afterMoveSection.indexOf("\n", afterMoveSection.indexOf("Turn order after player Alice's turn:") + "Turn order after player Alice's turn:".length());
        assertTrue(afterMoveNewlineIndex > 0, "移动后顺序标题后应该有换行");
        
        // 4. 验证下一个玩家提示后的换行
        int nextPlayerIndex = output.indexOf("Next player will be: Bob");
        assertTrue(nextPlayerIndex >= 0, "找不到下一个玩家提示");
        String nextPlayerSection = output.substring(nextPlayerIndex);
        int nextPlayerNewlineIndex = nextPlayerSection.indexOf("\n", nextPlayerSection.indexOf("Next player will be: Bob") + "Next player will be: Bob".length());
        assertTrue(nextPlayerNewlineIndex > 0, "下一个玩家提示后应该有换行");
        
        // 验证其他必要的调用
        verify(player).setLeftTurns(1);
        mockedStatic.verify(() -> GameContext.movePlayerToEnd(player));
        mockedStatic.verify(() -> GameContext.setCurrentPlayerIndex(0));
    }
} 
 