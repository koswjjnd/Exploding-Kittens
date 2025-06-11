package explodingkittens.view;

import explodingkittens.model.Card;
import explodingkittens.model.AttackCard;
import explodingkittens.model.SkipCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for SeeTheFutureView.
 */
public class SeeTheFutureViewTest {

    private SeeTheFutureView view;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        view = new SeeTheFutureView();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    /**
     * Tests displaying an empty list of cards.
     */
    @Test
    public void testDisplayEmptyList() {
        view.display(List.of());
        String output = outContent.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("You see the future cards"));

        assertTrue(!output.contains("- "));
    }

    /**
     * Tests displaying a list with one card.
     */
    @Test
    public void testDisplayOneCard() {
        List<Card> cards = List.of(new AttackCard());
        view.display(cards);

        String output = outContent.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("You see the future cards"));
        assertTrue(output.contains("ATTACK"));
    }

    /**
     * Tests displaying a list with two cards.
     */
    @Test
    public void testDisplayTwoCards() {
        List<Card> cards = List.of(new AttackCard(), new SkipCard());
        view.display(cards);

        String output = outContent.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("You see the future cards"));
        assertTrue(output.contains("ATTACK"));
        assertTrue(output.contains("SKIP"));
    }  
}
