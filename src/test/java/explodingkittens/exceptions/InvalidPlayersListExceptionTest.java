package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidPlayersListExceptionTest {
    @Test
    void testDefaultConstructor() {
        InvalidPlayersListException ex = new InvalidPlayersListException();
        assertEquals("Players list cannot be null.", ex.getMessage());
        assertNull(ex.getCause());
    }
} 