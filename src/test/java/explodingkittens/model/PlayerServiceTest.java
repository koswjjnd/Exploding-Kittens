package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerServiceTest {

    private PlayerService service;

    @BeforeEach
    void setUp() {
        service = new PlayerService();
    }

    @Test
    void whenCountIsBelowMinimumThenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.validateCount(1));
    }

    @Test
    void whenCountIsAtMinimumThenDoesNotThrow() {
        assertDoesNotThrow(() -> service.validateCount(2));
    }

    @Test
    void whenCountIsTypicalMiddleValueThenDoesNotThrow() {
        assertDoesNotThrow(() -> service.validateCount(3));
    }

    @Test
    void whenCountIsAtMaximumThenDoesNotThrow() {
        assertDoesNotThrow(() -> service.validateCount(4));
    }

    @Test
    void whenCountIsAboveMaximumThenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.validateCount(5));
    }
}
