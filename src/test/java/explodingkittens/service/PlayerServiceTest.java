package explodingkittens.service;

import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.player.Player;
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

    @Test
    void createPlayer_WithNullNickname_ThrowsInvalidNicknameException() {
        assertThrows(InvalidNicknameException.class, () -> {
            playerService.createPlayer(null);
        });
    }

    @Test
    void createPlayer_WithEmptyNickname_ThrowsInvalidNicknameException() {
        assertThrows(InvalidNicknameException.class, () -> {
            playerService.createPlayer("");
        });
    }

    @Test
    void createPlayer_WithWhitespaceNickname_ThrowsInvalidNicknameException() {
        assertThrows(InvalidNicknameException.class, () -> {
            playerService.createPlayer(" ");
        });
    }

    @Test
    void createPlayer_WithValidNickname_ReturnsPlayer() throws InvalidNicknameException {
        Player player = playerService.createPlayer("Player1");
        assertNotNull(player);
    }

    @Test
    void createPlayer_WithValidNickname_ReturnsPlayerWithCorrectName() throws InvalidNicknameException {
        String nickname = "Player1";
        Player player = playerService.createPlayer(nickname);
        assertEquals(nickname, player.getName());
    }

    @Test
    void createPlayer_WithLongNickname_ReturnsPlayerWithSameName() throws InvalidNicknameException {
        String nickname = "VeryLongName";
        Player player = playerService.createPlayer(nickname);
        assertEquals(nickname, player.getName());
    }
} 