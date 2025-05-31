package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.SkipCard;
import explodingkittens.model.AttackCard;
import explodingkittens.model.FavorCard;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;

/**
 * Tests for ConsoleCatCardRequestInputHandler.
 */
class ConsoleCatCardRequestInputHandlerTest {
    private List<Player> availablePlayers;
    private ConsoleCatCardRequestInputHandler inputHandler;
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
        inputHandler = new ConsoleCatCardRequestInputHandler(scanner);
    }

    @Test
    @DisplayName("Test Case 1: Constructor with null scanner")
    void testConstructorWithNullScanner() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConsoleCatCardRequestInputHandler(null);
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
        inputHandler = new ConsoleCatCardRequestInputHandler(scanner);

        Player selected = inputHandler.selectTargetPlayer(players);
        assertEquals("Player1", selected.getName());
    }

    @Test
    @DisplayName("Test Case 3: Select card")
    void testSelectCard() {
        Player targetPlayer = new Player("Target");
        targetPlayer.receiveCard(new SkipCard());
        targetPlayer.receiveCard(new AttackCard());
        targetPlayer.receiveCard(new FavorCard());

        // Mock user input
        String input = "1\n";
        scanner = new Scanner(input);
        inputHandler = new ConsoleCatCardRequestInputHandler(scanner);

        Card selected = inputHandler.selectCard(targetPlayer);
        assertEquals(CardType.SKIP, selected.getType());
    }

    @Test
    @DisplayName("Test Case 4: Select card from empty hand")
    void testSelectCardFromEmptyHand() {
        Player targetPlayer = new Player("Target");
        
        // Mock user input
        String input = "1\n";
        scanner = new Scanner(input);
        inputHandler = new ConsoleCatCardRequestInputHandler(scanner);

        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(targetPlayer);
        });
    }

    @Test
    @DisplayName("Test Case 5: Select card with invalid index")
    void testSelectCardWithInvalidIndex() {
        Player targetPlayer = new Player("Target");
        targetPlayer.receiveCard(new SkipCard());
        
        // Mock user input
        String input = "2\n";
        scanner = new Scanner(input);
        inputHandler = new ConsoleCatCardRequestInputHandler(scanner);

        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(targetPlayer);
        });
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

    // selectCard method tests
    @Test
    @DisplayName("Test Case 1: Select functional card")
    void testSelectFunctionalCard() {
        Player targetPlayer = new Player("Target");
        targetPlayer.receiveCard(new SkipCard());
        targetPlayer.receiveCard(new AttackCard());
        targetPlayer.receiveCard(new FavorCard());
        
        setupInputHandler("1\n");
        Card selected = inputHandler.selectCard(targetPlayer);
        assertEquals(CardType.SKIP, selected.getType());
    }

    @Test
    @DisplayName("Test Case 2: Select card with invalid index")
    void testSelectNonFunctionalCard() {
        Player targetPlayer = new Player("Target");
        targetPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        
        setupInputHandler("2\n"); // Try to select card at index 2 when only one card exists
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(targetPlayer);
        });
    }

    @Test
    @DisplayName("Test Case 3: Empty hand")
    void testEmptyHand() {
        Player targetPlayer = new Player("Target");
        setupInputHandler("1\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(targetPlayer);
        });
    }

    @Test
    @DisplayName("Test Case 4: Null player")
    void testNullPlayer() {
        setupInputHandler("1\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(null);
        });
    }

    @Test
    @DisplayName("Test Case 5: Invalid card selection")
    void testInvalidCardSelection() {
        Player targetPlayer = new Player("Target");
        targetPlayer.receiveCard(new SkipCard());
        
        setupInputHandler("2\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(targetPlayer);
        });
    }

    @Test
    @DisplayName("Test Case 6: Negative card selection")
    void testNegativeCardSelection() {
        Player targetPlayer = new Player("Target");
        targetPlayer.receiveCard(new SkipCard());
        
        setupInputHandler("-1\n");
        assertThrows(IllegalArgumentException.class, () -> {
            inputHandler.selectCard(targetPlayer);
        });
    }
}