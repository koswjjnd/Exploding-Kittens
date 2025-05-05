package explodingkittens.controller;

import explodingkittens.player.Player;
import explodingkittens.service.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.exceptions.InvalidNicknameException;
import java.util.Collections;
import java.util.ArrayList;

/**
 * The GameSetupController class manages the initial setup phase of the
 * Exploding Kittens game.
 * It handles player count confirmation, nickname input, card dealing, bomb
 * insertion,
 * and turn order initialization before the game starts.
 * 
 * This controller ensures that all necessary game setup steps are completed
 * in the correct order before the main game loop begins.
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

	/**
	 * Initializes the turn order for the game by randomly shuffling the player
	 * list.
	 * The shuffled order is stored in the global game context for use during
	 * gameplay.
	 * 
	 * @param players The list of players participating in the game. Must not be
	 *                null or empty.
	 * @throws IllegalArgumentException if the players list is null or empty
	 */
	public void initializeTurnOrder(List<Player> players) {
		if (players == null || players.isEmpty()) {
			throw new IllegalArgumentException("Player list cannot be null or empty.");
		}

		List<Player> turnOrder = new ArrayList<>(players);
		Collections.shuffle(turnOrder); // 可按需要保留或去除

		GameContext.setTurnOrder(turnOrder);
	}
}
