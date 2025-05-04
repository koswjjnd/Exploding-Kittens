package explodingkittens.controller;

import explodingkittens.player.Player;
import explodingkittens.service.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.exceptions.InvalidNicknameException;
import java.util.ArrayList;
import java.util.List;

public class GameSetupController {
    private final GameSetupView view;
    private final PlayerService playerService;

    public GameSetupController(GameSetupView view, PlayerService playerService) {
        this.view = view;
        this.playerService = playerService;
    }

    public List<Player> createPlayers(int count) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            boolean playerCreated = false;
            while (!playerCreated) {
                try {
                    String nickname = view.promptNickname(i + 1);
                    Player player = playerService.createPlayer(nickname);
                    players.add(player);
                    playerCreated = true;
                } catch (InvalidNicknameException e) {
                    view.showError(e.getMessage());
                }
            }
        }
        return players;
    }
}
