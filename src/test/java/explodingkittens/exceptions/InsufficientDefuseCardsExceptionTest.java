package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsufficientDefuseCardsExceptionTest {
    @Test
    void testConstructorWithMessage() {
        InsufficientDefuseCardsException ex = new InsufficientDefuseCardsException("Not enough defuse cards!");
        assertEquals("Not enough defuse cards!", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testDefaultConstructor() {
        InsufficientDefuseCardsException ex = new InsufficientDefuseCardsException();
        assertEquals("Not enough defuse cards in the deck", ex.getMessage());
        assertNull(ex.getCause());
    }
} 