package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidDeckExceptionTest {
    @Test
    void testConstructorWithMessage() {
        InvalidDeckException ex = new InvalidDeckException("Invalid deck!");
        assertEquals("Invalid deck!", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testDefaultConstructor() {
        InvalidDeckException ex = new InvalidDeckException();
        assertEquals("Deck cannot be null", ex.getMessage());
        assertNull(ex.getCause());
    }
} 