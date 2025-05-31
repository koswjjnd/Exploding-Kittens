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
} 