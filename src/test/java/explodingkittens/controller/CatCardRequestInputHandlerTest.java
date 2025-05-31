package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.ArrayList;
import java.util.List;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.SkipCard;
import explodingkittens.model.CatType;
import explodingkittens.model.CatCard;
import java.util.Scanner;
import explodingkittens.model.CatRequestCard;

/**
 * Tests for CatCardRequestInputHandler.
 */
class CatCardRequestInputHandlerTest {
    private CatCardRequestInputHandler inputHandler;
    private CatCardRequestController controller;
    private Scanner scanner;
    private List<Player> players;
    private Player currentPlayer;
    private Player targetPlayer;
    private CatRequestCard requestCard;

    @BeforeEach
    void setUp() {
        inputHandler = new ConsoleCatCardRequestInputHandler(new Scanner(System.in));
        controller = new CatCardRequestController(inputHandler);
    }

    @Test
    @DisplayName("Test Case 1: Null handler")
    void testNullHandler() {
        assertThrows(NullPointerException.class, () -> {
            new CatCardRequestController(null);
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
            });

        assertNotNull(controller);
    }

    @Test
    @DisplayName("Test Case 3: Valid implementation")
    void testValidImplementation() {
        CatCardRequestController controller = new CatCardRequestController(inputHandler);
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Test Case 4: Multiple handlers")
    void testMultipleHandlers() {
        CatCardRequestController controller1 = new CatCardRequestController(inputHandler);
        CatCardRequestController controller2 = new CatCardRequestController(inputHandler);
        assertNotSame(controller1, controller2);
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
            });

        assertNotNull(controller);
    }

    @Test
    @DisplayName("Test Case 2: Handle card request with invalid target")
    void testHandleCardRequestWithInvalidTarget() {
        setupPlayers();
        setupInvalidTargetHandler();
        setupPlayerCards();
        assertThrows(IllegalArgumentException.class, () -> {
            requestCard.effect(players, null);
        });
    }

    @Test
    @DisplayName("Test Case 3: Handle card request with valid target")
    void testHandleCardRequestWithValidTarget() {
        setupPlayers();
        setupValidTargetHandler();
        setupPlayerCards();
        assertDoesNotThrow(() -> {
            requestCard.effect(players, null);
        });
    }

    private void setupPlayers() {
        players = new ArrayList<>();
        currentPlayer = new Player("Player1");
        targetPlayer = new Player("Player2");
        players.add(currentPlayer);
        players.add(targetPlayer);
    }

    private void setupInvalidTargetHandler() {
        inputHandler = new CatCardRequestInputHandler() {
            @Override
            public Player selectTargetPlayer(List<Player> availablePlayers) {
                return null;
            }

            @Override
            public Card selectCard(Player targetPlayer) {
                return targetPlayer.getHand().get(0);
            }
        };
        controller = new CatCardRequestController(inputHandler);
        CatRequestCard.setController(controller);
    }

    private void setupValidTargetHandler() {
        inputHandler = new CatCardRequestInputHandler() {
            @Override
            public Player selectTargetPlayer(List<Player> availablePlayers) {
                return targetPlayer;
            }

            @Override
            public Card selectCard(Player targetPlayer) {
                return targetPlayer.getHand().get(0);
            }
        };
        controller = new CatCardRequestController(inputHandler);
        CatRequestCard.setController(controller);
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