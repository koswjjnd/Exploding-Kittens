package explodingkittens.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import explodingkittens.model.BasicCard;
import explodingkittens.model.CardType;
import explodingkittens.model.Player;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.view.GameView;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for {@link NopeService}.
 * This class contains unit tests to verify the correct behavior of the Nope card
 * negation mechanics in the game.
 * 
 * The tests cover various scenarios including:
 * - No Nope cards played
 * - Single Nope card played
 * - Multiple Nope cards played
 * - Invalid input cases (null values)
 * - Mixed card types
 * - Edge cases with multiple Nope cards
 */
public class NopeServiceTest {
    /** The service being tested */
    private NopeService nopeService;
    
    /** Test player used in the tests */
    @Mock
    private Player player1;
    
    /** Test Nope card used in the tests */
    @Mock
    private BasicCard nopeCard;
    
    /** Test non-Nope card used in the tests */
    @Mock
    private BasicCard skipCard;

    /** Mock view for testing */
    @Mock
    private GameView view;
    
    /**
     * Sets up the test environment before each test method.
     * Initializes:
     * - A new NopeService instance
     * - A test player
     * - A test Nope card
     * - A test Skip card
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nopeService = new NopeService(view);
        when(skipCard.getType()).thenReturn(CardType.SKIP);
        when(nopeCard.getType()).thenReturn(CardType.NOPE);
    }

    /**
     * Tests the behavior when no Nope cards are played.
     * Expected result: The effect should not be negated (return false)
     * as an even number (zero) of Nope cards means the original effect proceeds.
     */
    @Test
    void testIsNegatedWithNoNopeCards() {
        assertFalse(nopeService.isNegated(player1, new ArrayList<>()));
    }

    /**
     * Tests the behavior when a single Nope card is played.
     * Expected result: The effect should be negated (return true)
     * as an odd number of Nope cards negates the original effect.
     */
    @Test
    void testIsNegatedWithOneNopeCard() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        assertTrue(nopeService.isNegated(player1, nopeCards));
    }

    /**
     * Tests the behavior when multiple Nope cards are played.
     * Expected result: The effect should not be negated (return false)
     * as an even number of Nope cards means the original effect proceeds.
     * This test verifies that Nope cards cancel each other out in pairs.
     */
    @Test
    void testIsNegatedWithMultipleNopeCards() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        nopeCards.add(nopeCard);
        assertFalse(nopeService.isNegated(player1, nopeCards));
    }

    /**
     * Tests the behavior when three Nope cards are played.
     * Expected result: The effect should be negated (return true)
     * as an odd number of Nope cards negates the original effect.
     */
    @Test
    void testIsNegatedWithThreeNopeCards() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        nopeCards.add(nopeCard);
        nopeCards.add(nopeCard);
        assertTrue(nopeService.isNegated(player1, nopeCards));
    }

    /**
     * Tests the behavior when non-Nope cards are included in the list.
     * Expected result: The effect should be negated (return true)
     * as only the Nope cards are counted, and there is an odd number of them.
     */
    @Test
    void testIsNegatedWithMixedCardTypes() {
        List<BasicCard> cards = new ArrayList<>();
        cards.add(skipCard);
        cards.add(nopeCard);
        cards.add(skipCard);
        assertTrue(nopeService.isNegated(player1, cards));
    }

    /**
     * Tests the behavior when a null player is provided.
     * Expected result: Should throw IllegalArgumentException
     * as the player parameter cannot be null.
     */
    @Test
    void testIsNegatedWithNullPlayer() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        assertThrows(IllegalArgumentException.class, 
            () -> nopeService.isNegated(null, nopeCards));
    }

    /**
     * Tests the behavior when a null list of Nope cards is provided.
     * Expected result: Should throw IllegalArgumentException
     * as the nopeCards parameter cannot be null.
     */
    @Test
    void testIsNegatedWithNullNopeCards() {
        assertThrows(IllegalArgumentException.class, 
            () -> nopeService.isNegated(player1, null));
    }

    @Test
    void testIsNegatedByPlayersWithNoNopeCards() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player1);
        turnOrder.add(new Player("Player2"));
        
        // Mock GameContext static calls
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player1.hasCardOfType(CardType.NOPE)).thenReturn(false);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(skipCard);
            
            // Verify
            assertFalse(result);
            mockedStatic.verify(GameContext::getTurnOrder);
            mockedStatic.verify(GameContext::getCurrentPlayer);
        }
    }

    @Test
    void testIsNegatedByPlayersWithOneNopeCard() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        Player player2 = mock(Player.class);
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        // Mock GameContext static calls
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player1.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player2.isAlive()).thenReturn(true);
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(true);
            when(player2.removeCardOfType(CardType.NOPE)).thenReturn(nopeCard);
            
            // Mock view behavior
            when(view.promptPlayNope(player2, skipCard)).thenReturn(true);
            doNothing().when(view).displayPlayedNope(player2);
            doNothing().when(view).displayPlayerHand(player2);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(skipCard);
            
            // Verify
            assertTrue(result);
            verify(view).displayPlayedNope(player2);
            verify(view).displayPlayerHand(player2);
            mockedStatic.verify(GameContext::getTurnOrder);
            mockedStatic.verify(GameContext::getCurrentPlayer);
        }
    }

    @Test
    void testIsNegatedByPlayersWithEliminatedPlayer() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        Player player2 = mock(Player.class);
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        // Mock GameContext static calls
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player1.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player2.isAlive()).thenReturn(false);  // Player2 is eliminated
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(skipCard);
            
            // Verify
            assertFalse(result);
            verify(player2, never()).hasCardOfType(any());
            mockedStatic.verify(GameContext::getTurnOrder);
            mockedStatic.verify(GameContext::getCurrentPlayer);
        }
    }

    @Test
    void testIsNegatedByPlayersWithNullCard() {
        // Execute and verify exception
        assertThrows(IllegalArgumentException.class, 
            () -> nopeService.isNegatedByPlayers(null));
    }

    @Test
    void testIsNegatedByPlayersWithNullNopeCard() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        Player player2 = mock(Player.class);
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        // Mock GameContext static calls
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player1.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player2.isAlive()).thenReturn(true);
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(true);
            when(player2.removeCardOfType(CardType.NOPE)).thenReturn(null);  // Return null for Nope card
            
            // Mock view behavior
            when(view.promptPlayNope(player2, skipCard)).thenReturn(true);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(skipCard);
            
            // Verify
            assertFalse(result);  // Should not be negated since no Nope card was actually played
            verify(player2).removeCardOfType(CardType.NOPE);
            verify(view, never()).displayPlayedNope(any());
            verify(view, never()).displayPlayerHand(any());
            mockedStatic.verify(GameContext::getTurnOrder);
            mockedStatic.verify(GameContext::getCurrentPlayer);
        }
    }

    @Test
    void testIsNegatedByPlayersWithFalseHasCardOfType() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        Player player2 = mock(Player.class);
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        // Mock GameContext static calls
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player1.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player2.isAlive()).thenReturn(true);
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(false);  // Player doesn't have Nope card
            
            // Mock view behavior
            when(view.promptPlayNope(player2, skipCard)).thenReturn(true);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(skipCard);
            
            // Verify
            assertFalse(result);  // Should not be negated since player doesn't have Nope card
            verify(player2, never()).removeCardOfType(any());
            verify(view, never()).displayPlayedNope(any());
            verify(view, never()).displayPlayerHand(any());
            mockedStatic.verify(GameContext::getTurnOrder);
            mockedStatic.verify(GameContext::getCurrentPlayer);
        }
    }

    @Test
    void testIsNegatedByPlayersWithDeclinedNope() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        Player player2 = mock(Player.class);
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        // Mock GameContext static calls
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player1.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player2.isAlive()).thenReturn(true);
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(true);
            
            // Mock view behavior
            when(view.promptPlayNope(player2, skipCard)).thenReturn(false);  // Player declines to play Nope
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(skipCard);
            
            // Verify
            assertFalse(result);  // Should not be negated since player declined to play Nope
            verify(player2, never()).removeCardOfType(any());
            verify(view, never()).displayPlayedNope(any());
            verify(view, never()).displayPlayerHand(any());
            mockedStatic.verify(GameContext::getTurnOrder);
            mockedStatic.verify(GameContext::getCurrentPlayer);
        }
    }

    @Test
    void testIsNegatedWithNonBasicCard() {
        // Setup
        Card nonBasicCard = mock(Card.class);
        when(nonBasicCard.getType()).thenReturn(CardType.NOPE);
        
        List<Card> cards = new ArrayList<>();
        cards.add(nonBasicCard);
        
        // Execute
        boolean result = nopeService.isNegated(player1, cards);
        
        // Verify
        assertFalse(result);  // Should not be negated since card is not a BasicCard
    }

    @Test
    void testIsNegatedWithNonNopeBasicCard() {
        // Setup
        BasicCard nonNopeCard = mock(BasicCard.class);
        when(nonNopeCard.getType()).thenReturn(CardType.SKIP);  // Not a NOPE card
        
        List<Card> cards = new ArrayList<>();
        cards.add(nonNopeCard);
        
        // Execute
        boolean result = nopeService.isNegated(player1, cards);
        
        // Verify
        assertFalse(result);  // Should not be negated since card is not a NOPE card
    }

    @Test
    void testIsNegatedWithMixedInvalidCardTypes() {
        // Setup
        BasicCard nopeCard1 = mock(BasicCard.class);
        BasicCard nopeCard2 = mock(BasicCard.class);
        Card nonBasicCard = mock(Card.class);
        BasicCard nonNopeCard = mock(BasicCard.class);
        
        when(nopeCard1.getType()).thenReturn(CardType.NOPE);
        when(nopeCard2.getType()).thenReturn(CardType.NOPE);
        when(nonBasicCard.getType()).thenReturn(CardType.NOPE);
        when(nonNopeCard.getType()).thenReturn(CardType.SKIP);
        
        List<Card> cards = new ArrayList<>();
        cards.add(nopeCard1);
        cards.add(nonBasicCard);  // Should be ignored
        cards.add(nopeCard2);
        cards.add(nonNopeCard);   // Should be ignored
        
        // Execute
        boolean result = nopeService.isNegated(player1, cards);
        
        // Verify
        assertFalse(result);  // Should not be negated since there are 2 valid NOPE cards (even number)
    }
} 