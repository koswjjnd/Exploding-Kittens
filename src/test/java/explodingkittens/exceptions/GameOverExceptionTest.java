package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameOverExceptionTest {
    @Test
    void testConstructorWithMessage() {
        GameOverException ex = new GameOverException("Game over!");
        assertEquals("Game over!", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        GameOverException ex = new GameOverException("Game over!", cause);
        assertEquals("Game over!", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
} 