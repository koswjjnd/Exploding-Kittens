package explodingkittens.view;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameSetupViewTest {
    /**
     * Tests that initializing a deck with 1 player.
     */
    @Test
    public void testPromptPlayerCount1() {
        InputStream originalIn = System.in;
        String simulatedInput = "1\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        try {
            GameSetupView view = new GameSetupView();
            int result = view.promptPlayerCount();
            assertEquals(1, result);
        }
        finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests that initializing a deck with "X" players, then 2 players.
     */
    @Test
    public void testPromptPlayerCountX() {
        InputStream originalIn = System.in;
        String simulatedInput = "X\n2\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        try {
            GameSetupView view = new GameSetupView();
            int result = view.promptPlayerCount();
            assertEquals(2, result);
        }
        finally {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests that setting player 1's name to "Alice".
     */
    @Test
    public void testPromptNickname1Alice() {
        InputStream originalIn = System.in;
        String simulatedInput = "Alice\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        GameSetupView view = new GameSetupView();
        String result = view.promptNickname(1);
        assertEquals("Alice", result);
        System.setIn(originalIn);
    }
}