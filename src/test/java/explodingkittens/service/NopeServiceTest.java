package explodingkittens.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import explodingkittens.model.BasicCard;
import explodingkittens.model.CardType;
import explodingkittens.model.Player;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.view.GameView;

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
    private Player player1;
    
    /** Test Nope card used in the tests */
    private BasicCard nopeCard;
    
    /** Test non-Nope card used in the tests */
    private BasicCard skipCard;
    
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
        GameView view = mock(ConsoleGameView.class);
        nopeService = new NopeService(view);
        player1 = new Player("Player1");
        nopeCard = new BasicCard(CardType.NOPE);
        skipCard = new BasicCard(CardType.SKIP);
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
} 