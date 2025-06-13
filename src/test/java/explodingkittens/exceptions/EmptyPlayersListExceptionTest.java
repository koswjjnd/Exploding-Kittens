package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmptyPlayersListExceptionTest {
    @Test
    void testDefaultConstructor() {
        EmptyPlayersListException ex = new EmptyPlayersListException();
        assertEquals("Players list cannot be empty.", ex.getMessage());
        assertNull(ex.getCause());
    }
} 