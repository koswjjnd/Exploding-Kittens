package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.io.ByteArrayInputStream;

class CatRequestCardHandlerTest {
    private ConsoleCardStealInputHandler inputHandler;

    private void setupInputHandler(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputHandler = new ConsoleCardStealInputHandler(new java.util.Scanner(System.in));
    }

    @Test
    @DisplayName("Test Case 1: handler = null")
    void testNullHandler() {
        CatRequestCard.setInputHandler(null);
        // Should not throw any exception
    }

    @Test
    @DisplayName("Test Case 2: handler = invalid implementation")
    void testInvalidHandler() {
        CatRequestCard.setInputHandler(new CardStealInputHandler() {
            @Override
            public Player selectTargetPlayer(List<Player> availablePlayers) {
                return null;
            }

            @Override
            public int selectCardIndex(int handSize) {
                return -1;
            }
        });
        // Should not throw any exception
    }

    @Test
    @DisplayName("Test Case 3: handler = valid implementation")
    void testValidHandler() {
        setupInputHandler("1\n1\n");
        CatRequestCard.setInputHandler(inputHandler);
        // Should not throw any exception
    }

    @Test
    @DisplayName("Test Case 4: handler = multiple instances")
    void testMultipleHandlers() {
        setupInputHandler("1\n1\n");
        CatRequestCard.setInputHandler(inputHandler);
        CatRequestCard.setInputHandler(inputHandler);
        // Should not throw any exception
    }

    @Test
    @DisplayName("Test Case 5: handler = exception throwing")
    void testExceptionThrowingHandler() {
        CatRequestCard.setInputHandler(new CardStealInputHandler() {
            @Override
            public Player selectTargetPlayer(List<Player> availablePlayers) {
                throw new RuntimeException("Test exception");
            }

            @Override
            public int selectCardIndex(int handSize) {
                throw new RuntimeException("Test exception");
            }
        });
        // Should not throw any exception
    }
} 