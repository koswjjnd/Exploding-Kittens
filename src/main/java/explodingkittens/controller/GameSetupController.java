package explodingkittens.controller;

import explodingkittens.player.Player;
import explodingkittens.service.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.exceptions.InvalidNicknameException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for game setup phase, responsible for creating players and handling setup logic.
 */
public class GameSetupController {
    private final GameSetupView view;
    private final PlayerService playerService;

    /**
     * Constructs a GameSetupController with the given view and player service.
     * @param view the view to interact with the user
     * @param playerService the service to create and validate players
     */
    public GameSetupController(GameSetupView view, PlayerService playerService) {
        this.view = view;
        this.playerService = playerService;
    }

    /**
     * Creates a list of players by prompting for nicknames and validating them.
     * Retries if the nickname is invalid.
     * @param count the number of players to create
     * @return a list of created Player objects
     */
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
                } 
                catch (InvalidNicknameException e) {
                    view.showError(e.getMessage());
                }
            }
        }
        return players;
    }
}
