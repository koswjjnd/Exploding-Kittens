package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

class ConsoleCardStealInputHandlerTest {
    private List<Player> availablePlayers;
    private ConsoleCardStealInputHandler inputHandler;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        availablePlayers = new ArrayList<>();
        availablePlayers.add(new Player("Player1"));
        availablePlayers.add(new Player("Player2"));
        availablePlayers.add(new Player("Player3"));
    }

    private void setupInputHandler(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        scanner = new Scanner(System.in);
        inputHandler = new ConsoleCardStealInputHandler(scanner);
    }

    // Constructor test
    @Test
    @DisplayName("Test constructor with null scanner")
    void testConstructorWithNullScanner() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConsoleCardStealInputHandler(null);
        }, "Should throw exception when scanner is null");
    }

    // selectTargetPlayer method tests
    @Test
    @DisplayName("Test Case 1: Select first player")
    void testSelectFirstPlayer() {
        setupInputHandler("1\n");
        Player selected = inputHandler.selectTargetPlayer(availablePlayers);
        assertEquals(availablePlayers.get(0), selected);
    }

    @Test
    @DisplayName("Test Case 2: Select last player")
    void testSelectLastPlayer() {
        setupInputHandler("3\n");
        Player selected = inputHandler.selectTargetPlayer(availablePlayers);
        assertEquals(availablePlayers.get(2), selected);
    }

    @Test
    @DisplayName("Test Case 3: Select middle player")
    void testSelectMiddlePlayer() {
        setupInputHandler("2\n");
        Player selected = inputHandler.selectTargetPlayer(availablePlayers);
        assertEquals(availablePlayers.get(1), selected);
    }

    @Test
    @DisplayName("Test Case 4: Invalid player selection")
    void testInvalidPlayerSelection() {
        setupInputHandler("4\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectTargetPlayer(availablePlayers);
        });
    }

    @Test
    @DisplayName("Test Case 5: Negative player selection")
    void testNegativePlayerSelection() {
        setupInputHandler("-1\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectTargetPlayer(availablePlayers);
        });
    }

    @Test
    @DisplayName("Test Case 6: Empty player list")
    void testEmptyPlayerList() {
        setupInputHandler("1\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectTargetPlayer(new ArrayList<>());
        });
    }

    @Test
    @DisplayName("Test Case 7: Null player list")
    void testNullPlayerList() {
        setupInputHandler("1\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectTargetPlayer(null);
        });
    }

} 