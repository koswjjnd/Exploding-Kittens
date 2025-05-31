package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.SkipCard;
import explodingkittens.model.AttackCard;
import explodingkittens.model.FavorCard;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.CatRequestCard;

/**
 * Tests for ConsoleCatCardRequestInputHandler.
 */
class ConsoleCatCardRequestInputHandlerTest {
    private List<Player> availablePlayers;
    private ConsoleCatCardRequestInputHandler inputHandler;
    private Scanner scanner;
    private Player targetPlayer;
    private Player currentPlayer;
    private CatRequestCard requestCard;
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    private CatCardRequestController controller;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    void setUp() {
        availablePlayers = new ArrayList<>();
        availablePlayers.add(new Player("Player1"));
        availablePlayers.add(new Player("Player2"));
        availablePlayers.add(new Player("Player3"));
        targetPlayer = new Player("Target");
        currentPlayer = new Player("Current");
        inputHandler = new ConsoleCatCardRequestInputHandler(
            new Scanner(System.in, StandardCharsets.UTF_8.name()));
        controller = new CatCardRequestController(inputHandler);
    }

    @AfterEach
    void tearDown() {
        if (scanner != null) {
            scanner.close();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } 
            catch (IOException e) {
                // Ignore close exception
            }
        }
        // Restore original System.in
        System.setIn(System.in);
    }

    private void setupInputHandler(String input) {
        if (scanner != null) {
            scanner.close();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } 
            catch (IOException e) {
                // Ignore close exception
            }
        }
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
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
        if (scanner != null) {
            scanner.close();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } 
            catch (IOException e) {
                // Ignore close exception
            }
        }
        inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
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
        scanner = new Scanner(
            new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), 
            StandardCharsets.UTF_8);
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
        scanner = new Scanner(
            new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), 
            StandardCharsets.UTF_8);
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
        scanner = new Scanner(
            new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), 
            StandardCharsets.UTF_8);
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

    @Test
    @DisplayName("Test Case 2: Invalid implementation")
    void testInvalidImplementation() {
        CatCardRequestController controller = new CatCardRequestController(
            new CatCardRequestInputHandler() {
                @Override
                public Player selectTargetPlayer(List<Player> availablePlayers) {
                    return null;
                }

                @Override
                public Card selectCard(Player targetPlayer) {
                    return null;
                }
            }
        );

        assertNotNull(controller);
    }

    @Test
    @DisplayName("Test Case 5: Exception throwing handler")
    void testExceptionThrowingHandler() {
        CatCardRequestController controller = new CatCardRequestController(
            new CatCardRequestInputHandler() {
                @Override
                public Player selectTargetPlayer(List<Player> availablePlayers) {
                    throw new RuntimeException("Test exception");
                }

                @Override
                public Card selectCard(Player targetPlayer) {
                    throw new RuntimeException("Test exception");
                }
            }
        );

        assertNotNull(controller);
    }

    private void setupPlayerCards() {
        // Give target player some cards
        targetPlayer.receiveCard(new SkipCard());
        targetPlayer.receiveCard(new CatCard(CatType.BEARD_CAT));

        // Give current player three TACOCAT cards and a CatRequestCard for the request
        requestCard = new CatRequestCard(CatType.TACOCAT);
        currentPlayer.receiveCard(requestCard);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
    }
}