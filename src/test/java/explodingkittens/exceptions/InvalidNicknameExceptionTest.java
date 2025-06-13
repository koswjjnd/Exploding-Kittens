package explodingkittens.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidNicknameExceptionTest {
    @Test
    void testConstructorWithMessage() {
        InvalidNicknameException ex = new InvalidNicknameException("Nickname cannot be null or empty.");
        assertEquals("Nickname cannot be null or empty.", ex.getMessage());
        assertNull(ex.getCause());
    }
} 