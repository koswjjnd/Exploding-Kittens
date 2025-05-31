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

    
} 