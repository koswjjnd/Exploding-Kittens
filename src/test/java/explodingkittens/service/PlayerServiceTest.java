package explodingkittens.service;

import explodingkittens.exceptions.InvalidPlayerCountException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {
    private final PlayerService playerService = new PlayerService();

    @Test
    void validateCount_WithCount1_ThrowsInvalidPlayerCountException() {
        assertThrows(InvalidPlayerCountException.class, () -> {
            playerService.validateCount(1);
        });
    }
    @Test
    void validateCount_WithCount2_DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            playerService.validateCount(2);
        });
    }
    @Test
    void validateCount_WithCount3_DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            playerService.validateCount(3);
        });
    }
    @Test
    void validateCount_WithCount4_DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            playerService.validateCount(4);
        });
    }
    @Test
    void validateCount_WithCount5_ThrowsInvalidPlayerCountException() {
        assertThrows(InvalidPlayerCountException.class, () -> {
            playerService.validateCount(5);
        });
    }
} 