package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {

    private PlayerService service;

    @BeforeEach
    void setUp() {
        service = new PlayerService();
    }

    @Test
    void whenCountIsBelowMinimum_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateCount(1));
    }

    @Test
    void whenCountIsAtMinimum_thenDoesNotThrow() {
        assertDoesNotThrow(() -> service.validateCount(2));
    }
}

