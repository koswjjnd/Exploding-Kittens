package explodingkittens.service;

import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerServiceTest {
    private final PlayerService playerService = new PlayerService();

    @Test
    void validateCountWithCount1ThrowsInvalidPlayerCountException() {
        assertThrows(InvalidPlayerCountException.class, () -> {
            playerService.validateCount(1);
        });
    }
    @Test
    void validateCountWithCount2DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            playerService.validateCount(2);
        });
    }
    @Test
    void validateCountWithCount3DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            playerService.validateCount(3);
        });
    }
    @Test
    void validateCountWithCount4DoesNotThrowException() {
        assertDoesNotThrow(() -> {
            playerService.validateCount(4);
        });
    }
    @Test
    void validateCountWithCount5ThrowsInvalidPlayerCountException() {
        assertThrows(InvalidPlayerCountException.class, () -> {
            playerService.validateCount(5);
        });
    }

    @Test
    void createPlayerWithNullNicknameThrowsInvalidNicknameException() {
        assertThrows(InvalidNicknameException.class, () -> {
            playerService.createPlayer(null);
        });
    }

    @Test
    void createPlayerWithEmptyNicknameThrowsInvalidNicknameException() {
        assertThrows(InvalidNicknameException.class, () -> {
            playerService.createPlayer("");
        });
    }

    @Test
    void createPlayerWithWhitespaceNicknameThrowsInvalidNicknameException() {
        assertThrows(InvalidNicknameException.class, () -> {
            playerService.createPlayer(" ");
        });
    }

    @Test
    void createPlayerWithValidNicknameReturnsPlayer() throws InvalidNicknameException {
        Player player = playerService.createPlayer("Player1");
        assertNotNull(player);
    }

    @Test
    void createPlayerWithValidNicknameReturnsPlayerWithCorrectName()
            throws InvalidNicknameException {
        String nickname = "Player1";
        Player player = playerService.createPlayer(nickname);
        assertEquals(nickname, player.getName());
    }

    @Test
    void createPlayerWithLongNicknameReturnsPlayerWithSameName() throws InvalidNicknameException {
        String nickname = "VeryLongName";
        Player player = playerService.createPlayer(nickname);
        assertEquals(nickname, player.getName());
    }
} 