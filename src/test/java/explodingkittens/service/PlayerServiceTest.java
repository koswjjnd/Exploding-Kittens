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
} 