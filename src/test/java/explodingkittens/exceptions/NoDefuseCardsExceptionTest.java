package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NoDefuseCardsExceptionTest {
    @Test
    void testDefaultConstructor() {
        NoDefuseCardsException ex = new NoDefuseCardsException();
        assertEquals("No defuse cards in the deck.", ex.getMessage());
        assertNull(ex.getCause());
    }
} 