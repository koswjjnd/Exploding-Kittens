package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.model.Card;
import explodingkittens.model.DrawFromBottomCard;
import explodingkittens.service.CardEffectService;
import explodingkittens.service.TurnService;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DrawFromBottomCardTest {
    private Player player;
    private Deck deck;
    private List<Player> turnOrder;
    private DrawFromBottomCard card;
    private MockedStatic<GameContext> mockedGameContext;
    private GameView mockView;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        deck = mock(Deck.class);
        turnOrder = new ArrayList<>();
        turnOrder.add(player);
        mockView = mock(GameView.class);
        card = new DrawFromBottomCard(mockView);
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
        mockedGameContext = Mockito.mockStatic(GameContext.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
    }

    @Test
    void testDrawFromEmptyDeckThrows() {
        when(deck.getCards()).thenReturn(new ArrayList<>());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, deck));
    }

    @Test
    void testDrawFromDeckWithOneCard() {
        // Setup
        Card bottom = mock(Card.class);
        when(bottom.getType()).thenReturn(CardType.SKIP);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(bottom);
        when(deck.getCards()).thenReturn(cards);
        when(deck.removeBottomCard()).thenReturn(bottom);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        verify(deck).removeBottomCard();
        verify(player).receiveCard(bottom);
        verify(mockView).displayCardDrawnFromBottom(bottom);
        verify(player).setLeftTurns(0);
    }

    @Test
    void testDrawFromDeckWithMultipleCards() {
        // Setup
        Card c1 = mock(Card.class);
        Card c2 = mock(Card.class);
        Card c3 = mock(Card.class);
        when(c3.getType()).thenReturn(CardType.ATTACK);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        when(deck.getCards()).thenReturn(cards);
        when(deck.removeBottomCard()).thenReturn(c3);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        verify(deck).removeBottomCard();
        verify(player).receiveCard(c3);
        verify(mockView).displayCardDrawnFromBottom(c3);
        verify(player).setLeftTurns(0);
    }

    @Test
    void testDrawExplodingKitten() {
        // Setup
        ExplodingKittenCard ek = mock(ExplodingKittenCard.class);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(ek);
        when(deck.getCards()).thenReturn(cards);
        when(deck.removeBottomCard()).thenReturn(ek);
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        verify(deck).removeBottomCard();
        verify(mockView).displayCardDrawnFromBottom(ek);
        verify(player).setLeftTurns(0);
    }

    @Test
    void testNullTurnOrderThrows() {
        assertThrows(IllegalArgumentException.class, () -> card.effect(null, deck));
    }

    @Test
    void testEmptyTurnOrderThrows() {
        assertThrows(IllegalArgumentException.class, () -> card.effect(new ArrayList<>(), deck));
    }

    @Test
    void testNullDeckThrows() {
        assertThrows(IllegalArgumentException.class, () -> card.effect(turnOrder, null));
    }

    @Test
    void testDrawExplodingKittenHandling() {
        // Setup
        ExplodingKittenCard ek = mock(ExplodingKittenCard.class);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(ek);
        when(deck.getCards()).thenReturn(cards);
        when(deck.removeBottomCard()).thenReturn(ek);
        when(player.hasDefuse()).thenReturn(false);
        when(player.getName()).thenReturn("TestPlayer");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(player);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(List.of(player));
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        verify(deck).removeBottomCard();
        verify(mockView).displayCardDrawnFromBottom(ek);
        verify(player).hasDefuse();
        verify(player).setAlive(false);
        verify(player, Mockito.times(2)).getName();
        verify(player).isAlive();
        verify(player).setLeftTurns(0);
    }
} 