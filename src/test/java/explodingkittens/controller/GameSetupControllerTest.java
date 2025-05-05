package explodingkittens.controller;

import explodingkittens.player.Player;
import explodingkittens.service.PlayerService;
import explodingkittens.view.GameSetupView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class GameSetupControllerTest {
    @Mock
    private GameSetupView view;
    
    @Mock
    private PlayerService playerService;
    
    private GameSetupController controller;

    @Test
    void createPlayersWithCount2ReturnsListWith2Players() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(2);

        assertEquals(2, players.size());
    }

    @Test
    void createPlayersWithCount3ReturnsListWith3Players() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(3);

        assertEquals(3, players.size());
    }

    @Test
    void createPlayersWithCount4ReturnsListWith4Players() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3", "Player4");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(4);

        assertEquals(4, players.size());
    }

    @Test
    void createPlayersWithCount2CallsPromptNicknameTwice() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(2);

        verify(view, times(2)).promptNickname(anyInt());
    }

    @Test
    void createPlayersWithCount3CallsPromptNicknameThreeTimes() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(3);

        verify(view, times(3)).promptNickname(anyInt());
    }

    @Test
    void createPlayersWithCount4CallsPromptNicknameFourTimes() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3", "Player4");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(4);

        verify(view, times(4)).promptNickname(anyInt());
    }

    @Test
    void createPlayersWithCount2CallsCreatePlayerTwice() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(2);

        verify(playerService, times(2)).createPlayer(anyString());
    }

    @Test
    void createPlayersWithCount3CallsCreatePlayerThreeTimes() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(3);

        verify(playerService, times(3)).createPlayer(anyString());
    }

    @Test
    void createPlayersWithCount4CallsCreatePlayerFourTimes() throws Exception {
        controller = new GameSetupController(view, playerService);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3", "Player4");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(4);

        verify(playerService, times(4)).createPlayer(anyString());
    }

    @Test
	void testInitializeTurnOrderNullListShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			controller.initializeTurnOrder(null);
		});
	}

	@Test
	void testInitializeTurnOrderEmptyListShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			controller.initializeTurnOrder(Collections.emptyList());
		});
	}

	@Test
	void testInitializeTurnOrderSinglePlayerShouldStoreCorrectOrder() {
		Player p1 = new Player("Alice");
		controller.initializeTurnOrder(List.of(p1));

		List<Player> order = GameContext.getTurnOrder();
		assertEquals(1, order.size());
		assertEquals("Alice", order.get(0).getName());
	}

	@Test
	void testInitializeTurnOrderMultiplePlayersShouldStoreAllPlayers() {
		Player p1 = new Player("A");
		Player p2 = new Player("B");
		Player p3 = new Player("C");

		List<Player> players = Arrays.asList(p1, p2, p3);
		controller.initializeTurnOrder(players);

		List<Player> result = GameContext.getTurnOrder();
		assertEquals(3, result.size());
		assertTrue(result.containsAll(players));
	}

	@Test
	void testInitializeTurnOrderContainsNullPlayerShouldStoreIncludingNull() {
		Player p1 = new Player("A");
		List<Player> players = Arrays.asList(p1, null);

		controller.initializeTurnOrder(players);
		List<Player> result = GameContext.getTurnOrder();

		assertEquals(2, result.size());
		assertEquals(p1, result.get(0));
		assertNull(result.get(1));
	}
}