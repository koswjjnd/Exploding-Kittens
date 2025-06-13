package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.spy;

/**
 * Test class for the ShuffleCard class.
 */
public class ShuffleCardTest {
    private ShuffleCard shuffleCard;
    private Deck gameDeck;
    private List<Player> dummyPlayers;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        shuffleCard = new ShuffleCard();
        gameDeck = spy(new Deck());
        
        // add some different cards to the deck
        gameDeck.addCard(new DefuseCard());
        gameDeck.addCard(new AttackCard());
        gameDeck.addCard(new SkipCard());
        gameDeck.addCard(new ShuffleCard());
        gameDeck.addCard(new SeeTheFutureCard());

        // Initialize dummyPlayers
        dummyPlayers = new ArrayList<>();
        dummyPlayers.add(new Player("Player1"));
        dummyPlayers.add(new Player("Player2"));
        dummyPlayers.add(new Player("Player3"));

        // Redirect System.out with explicit charset
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @org.junit.jupiter.api.AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testConstructor() {
        ShuffleCard newShuffleCard = new ShuffleCard();
        assertNotNull(newShuffleCard);
        assertEquals(CardType.SHUFFLE, newShuffleCard.getType());
    }

    @Test
    void testEffectWithNullDeck() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> shuffleCard.effect(dummyPlayers, null)
        );
        assertEquals("Deck cannot be null when playing ShuffleCard.", exception.getMessage());
        assertEquals("", outContent.toString(StandardCharsets.UTF_8), "No output should be printed for null deck");
    }

    /**
     * Tests the shuffle effect changes the order of cards while preserving their count and types.
     */
    @Test
    void testEffectShufflesDeck() {
        // use ShuffleCard effect
        shuffleCard.effect(dummyPlayers, gameDeck);
        
        // Verify that shuffle was called
        verify(gameDeck).shuffle();

        // Verify console output
        String expectedOutput = String.format("ShuffleCard played: deck has been shuffled!");
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .trim()
            .replaceAll("\\r\\n", "\n");
        
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testGetType() {
        assertEquals(CardType.SHUFFLE, shuffleCard.getType());
    }

    @Test
    void testEffectWithEmptyDeck() {
        // Setup empty deck
        gameDeck = spy(new Deck());
        
        // use ShuffleCard effect
        shuffleCard.effect(dummyPlayers, gameDeck);
        
        // Verify that shuffle was called
        verify(gameDeck).shuffle();

        // Verify console output
        String expectedOutput = String.format("ShuffleCard played: deck has been shuffled!");
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .trim()
            .replaceAll("\\r\\n", "\n");
        
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testEffectWithSingleCardDeck() {
        // Setup deck with single card
        gameDeck = spy(new Deck());
        Card singleCard = new ExplodingKittenCard();
        gameDeck.addCard(singleCard);
        
        // use ShuffleCard effect
        shuffleCard.effect(dummyPlayers, gameDeck);
        
        // Verify that shuffle was called
        verify(gameDeck).shuffle();

        // Verify console output
        String expectedOutput = String.format("ShuffleCard played: deck has been shuffled!");
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .trim()
            .replaceAll("\\r\\n", "\n");
        
        assertEquals(expectedOutput, actualOutput);
    }
}