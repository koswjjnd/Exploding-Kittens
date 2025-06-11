package explodingkittens.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import explodingkittens.model.BasicCard;
import explodingkittens.model.CardType;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.view.GameView;
import explodingkittens.controller.GameContext;

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
    
    /** Second test player used in the tests */
    @Mock
    private Player player2;
    
    /** Third test player used in the tests */
    @Mock
    private Player player3;
    
    /** Fourth test player used in the tests */
    @Mock
    private Player player4;
    
    /** Test Nope card used in the tests */
    @Mock
    private BasicCard nopeCard;
    
    /** Test non-Nope card used in the tests */
    @Mock
    private BasicCard targetCard;

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
        when(nopeCard.getType()).thenReturn(CardType.NOPE);
        when(targetCard.getType()).thenReturn(CardType.SKIP);
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
        cards.add(targetCard);
        cards.add(nopeCard);
        cards.add(targetCard);
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
        turnOrder.add(player2);
        turnOrder.add(player3);
        turnOrder.add(player4);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player2.isAlive()).thenReturn(true);
            when(player3.isAlive()).thenReturn(true);
            when(player4.isAlive()).thenReturn(true);
            
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player3.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player4.hasCardOfType(CardType.NOPE)).thenReturn(false);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(targetCard);
            
            // Verify
            assertFalse(result);
            verify(view, times(4)).showCardPlayed(player1, targetCard);
            verify(view, never()).promptPlayNope(any(), any());
            verify(view, never()).displayPlayedNope(any());
            verify(view, never()).displayPlayerHand(any());
        }
    }

    @Test
    void testIsNegatedByPlayersWithOneNopeCard() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        turnOrder.add(player4);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player2.isAlive()).thenReturn(true);
            when(player3.isAlive()).thenReturn(true);
            when(player4.isAlive()).thenReturn(true);
            
            // Only player2 has Nope card and will use it
            when(player2.hasCardOfType(CardType.NOPE))
                .thenReturn(true)  // First call
                .thenReturn(false); // After using the card
            when(player3.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player4.hasCardOfType(CardType.NOPE)).thenReturn(false);
            
            when(player2.removeCardOfType(CardType.NOPE)).thenReturn(nopeCard);
            when(view.promptPlayNope(player2, targetCard)).thenReturn(true);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(targetCard);
            
            // Verify
            assertTrue(result);
            verify(view, times(4)).showCardPlayed(player1, targetCard);
            verify(view, times(1)).promptPlayNope(player2, targetCard);
            verify(view, times(1)).displayPlayedNope(player2);
            verify(view, times(1)).displayPlayerHand(player2);
        }
    }

    @Test
    void testIsNegatedByPlayersWithNopeChain() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        turnOrder.add(player4);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player2.isAlive()).thenReturn(true);
            when(player3.isAlive()).thenReturn(true);
            when(player4.isAlive()).thenReturn(true);
            
            // Mock hasCardOfType to return true only once for each player
            when(player2.hasCardOfType(CardType.NOPE))
                .thenReturn(true)  // First call
                .thenReturn(false); // Subsequent calls
            when(player3.hasCardOfType(CardType.NOPE))
                .thenReturn(true)  // First call
                .thenReturn(false); // Subsequent calls
            when(player4.hasCardOfType(CardType.NOPE)).thenReturn(false);
            
            when(player2.removeCardOfType(CardType.NOPE)).thenReturn(nopeCard);
            when(player3.removeCardOfType(CardType.NOPE)).thenReturn(nopeCard);
            
            // Mock promptPlayNope to return true only once for each player
            when(view.promptPlayNope(player2, targetCard))
                .thenReturn(true)  // First call
                .thenReturn(false); // Subsequent calls
            when(view.promptPlayNope(player3, targetCard))
                .thenReturn(true)  // First call
                .thenReturn(false); // Subsequent calls
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(targetCard);
            
            // Verify
            assertFalse(result); // Even number of Nope cards
            verify(view, times(4)).showCardPlayed(player1, targetCard);
            verify(view, times(1)).promptPlayNope(player2, targetCard);
            verify(view, times(1)).promptPlayNope(player3, targetCard);
            verify(view, times(2)).displayPlayedNope(any());
            verify(view, times(2)).displayPlayerHand(any());
        }
    }

    @Test
    void testIsNegatedByPlayersWithNullCard() {
        assertThrows(IllegalArgumentException.class, 
            () -> nopeService.isNegatedByPlayers(null));
    }

    @Test
    void testIsNegatedByPlayersWithCurrentPlayerNotFound() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player2);
        turnOrder.add(player3);
        turnOrder.add(player4);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Execute and verify
            assertThrows(IllegalStateException.class, 
                () -> nopeService.isNegatedByPlayers(targetCard));
        }
    }

    @Test
    void testIsNegatedWithOddNumberOfCards() {
        List<Card> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        assertTrue(nopeService.isNegated(player1, nopeCards));
    }

    @Test
    void testIsNegatedWithEvenNumberOfCards() {
        List<Card> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        nopeCards.add(nopeCard);
        assertFalse(nopeService.isNegated(player1, nopeCards));
    }

    @Test
    void testIsNegatedByPlayersWithDeclinedNope() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        turnOrder.add(player4);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player2.isAlive()).thenReturn(true);
            when(player3.isAlive()).thenReturn(true);
            when(player4.isAlive()).thenReturn(true);
            
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(true);
            when(player3.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player4.hasCardOfType(CardType.NOPE)).thenReturn(false);
            
            when(view.promptPlayNope(player2, targetCard)).thenReturn(false);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(targetCard);
            
            // Verify
            assertFalse(result);
            verify(view, times(4)).showCardPlayed(player1, targetCard);
            verify(view).promptPlayNope(player2, targetCard);
            verify(view, never()).displayPlayedNope(any());
            verify(view, never()).displayPlayerHand(any());
        }
    }

    @Test
    void testIsNegatedByPlayersWithNullNopeCard() {
        // Setup
        List<Player> turnOrder = new ArrayList<>();
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        turnOrder.add(player4);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(GameContext::getTurnOrder).thenReturn(turnOrder);
            mockedStatic.when(GameContext::getCurrentPlayer).thenReturn(player1);
            
            // Mock player behavior
            when(player1.isAlive()).thenReturn(true);
            when(player2.isAlive()).thenReturn(true);
            when(player3.isAlive()).thenReturn(true);
            when(player4.isAlive()).thenReturn(true);
            
            when(player2.hasCardOfType(CardType.NOPE)).thenReturn(true);
            when(player3.hasCardOfType(CardType.NOPE)).thenReturn(false);
            when(player4.hasCardOfType(CardType.NOPE)).thenReturn(false);
            
            when(player2.removeCardOfType(CardType.NOPE)).thenReturn(null);
            when(view.promptPlayNope(player2, targetCard)).thenReturn(true);
            
            // Execute
            boolean result = nopeService.isNegatedByPlayers(targetCard);
            
            // Verify
            assertFalse(result);
            verify(view, times(4)).showCardPlayed(player1, targetCard);
            verify(view).promptPlayNope(player2, targetCard);
            verify(view, never()).displayPlayedNope(any());
            verify(view, never()).displayPlayerHand(any());
        }
    }
} 