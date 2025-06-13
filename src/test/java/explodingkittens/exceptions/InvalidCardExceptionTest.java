package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InvalidCardExceptionTest {
    @Test
    void testDefaultConstructor() {
        InvalidCardException ex = new InvalidCardException();
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        InvalidCardException ex = new InvalidCardException("Invalid card!");
        assertEquals("Invalid card!", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidCardException ex = new InvalidCardException("Invalid card!", cause);
        assertEquals("Invalid card!", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
} 