package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmptyDeckExceptionTest {
    @Test
    void testDefaultConstructor() {
        EmptyDeckException ex = new EmptyDeckException();
        assertEquals("Deck is empty", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        EmptyDeckException ex = new EmptyDeckException("Custom message");
        assertEquals("Custom message", ex.getMessage());
        assertNull(ex.getCause());
    }
} 