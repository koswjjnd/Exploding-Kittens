package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import explodingkittens.model.Player;
import explodingkittens.model.CatType;
import explodingkittens.model.CatCard;
import explodingkittens.model.SkipCard;

/**
 * Tests for ConsoleCatCardStealInputHandler.
 */
class ConsoleCatCardStealInputHandlerTest {
    private ConsoleCatCardStealInputHandler inputHandler;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new Scanner(System.in);
        inputHandler = new ConsoleCatCardStealInputHandler(scanner);
    }

    @Test
    @DisplayName("Test Case 1: Constructor with null scanner")
    void testConstructorWithNullScanner() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConsoleCatCardStealInputHandler(null);
        });
    }

    @Test
    @DisplayName("Test Case 2: Select target player")
    void testSelectTargetPlayer() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));

        // Mock user input
        String input = "1\n";
        scanner = new Scanner(input);
        inputHandler = new ConsoleCatCardStealInputHandler(scanner);

        Player selected = inputHandler.selectTargetPlayer(players);
        assertEquals("Player1", selected.getName());
    }

    @Test
    @DisplayName("Test Case 3: Select card index")
    void testSelectCardIndex() {
        // Mock user input
        String input = "2\n";
        scanner = new Scanner(input);
        inputHandler = new ConsoleCatCardStealInputHandler(scanner);

        int index = inputHandler.selectCardIndex(3);
        assertEquals(1, index);
    }

    @Test
    @DisplayName("Test Case 4: Handle card steal")
    void testHandleCardSteal() {
        List<Player> players = new ArrayList<>();
        Player currentPlayer = new Player("Player1");
        Player targetPlayer = new Player("Player2");
        players.add(currentPlayer);
        players.add(targetPlayer);

        // Give target player some cards
        targetPlayer.receiveCard(new SkipCard());
        targetPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new CatCard(CatType.BEARD_CAT));

        // Give current player two TACOCAT cards for the steal
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));

        // Mock user input - provide extra input in case of retries
        String input = "2\n1\n2\n1\n";
        scanner = new Scanner(input);
        inputHandler = new ConsoleCatCardStealInputHandler(scanner);

        // Set the input handler for CatCard
        CatCard.setInputHandler(inputHandler);

        assertDoesNotThrow(() -> {
            inputHandler.handleCardSteal(currentPlayer, players, CatType.TACOCAT);
        });
    }
} 