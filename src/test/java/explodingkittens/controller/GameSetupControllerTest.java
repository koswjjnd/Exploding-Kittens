package explodingkittens.controller;

import explodingkittens.player.Player;
import explodingkittens.service.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.exceptions.InvalidNicknameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameSetupControllerTest {
    @Mock
    private GameSetupView view;
    
    @Mock
    private PlayerService playerService;
    
    private GameSetupController controller;

    @Test
    void createPlayers_WithCount2_ReturnsListWith2Players() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));
        List<Player> players = controller.createPlayers(2);
        assertEquals(2, players.size());
    }
    @Test
    void createPlayers_WithCount3_ReturnsListWith3Players() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(3);

        assertEquals(3, players.size());
    }
}
