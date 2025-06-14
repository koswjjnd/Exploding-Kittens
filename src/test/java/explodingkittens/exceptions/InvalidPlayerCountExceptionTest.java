package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InvalidPlayerCountExceptionTest {
    @Test
    void testConstructorWithMessage() {
        InvalidPlayerCountException ex = new InvalidPlayerCountException("Invalid number of players.");
        assertEquals("Invalid number of players.", ex.getMessage());
        assertNull(ex.getCause());
    }
} 