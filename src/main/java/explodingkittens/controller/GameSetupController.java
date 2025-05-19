package explodingkittens.controller;

import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.model.Player;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.model.Deck;
import explodingkittens.service.DealService;
import explodingkittens.exceptions.InvalidDeckException;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The GameSetupController class manages the initial setup phase of the
 * Exploding Kittens game.
 * It handles player count confirmation, nickname input, card dealing, 
 * bomb insertion, and turn order initialization before the game starts.
 * 
 * This controller ensures that all necessary game setup steps are completed
 * in the correct order before the main game loop begins.
 * Controller for game setup phase, responsible for creating players and 
 * handling setup logic.
 */
public class GameSetupController {
    private final GameSetupView view;
    private final PlayerService playerService;
    private final DealService dealService;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private Deck gameDeck;

    /**
     * Constructs a GameSetupController with the given view and player service.
     * @param view the view to interact with the user
     * @param playerService the service to create and validate players
     * @param dealService the service to deal cards and initial hands
     */
    public GameSetupController(
        GameSetupView view, 
        PlayerService playerService, 
        DealService dealService
    ) {
        this.view = view;
        this.playerService = playerService;
        this.dealService = dealService;
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
		Collections.shuffle(turnOrder);

		GameContext.setTurnOrder(turnOrder);
	}

    /**
     * Prepares the game deck based on the number of players
     * @param count the number of players
     * @param players the list of players to deal cards to
     * @throws InvalidPlayerCountException when the player count is invalid
     */
    public void prepareDeck(int count, List<Player> players) throws InvalidPlayerCountException {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            throw new InvalidPlayerCountException(
                "Number of players must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS);
        }

        gameDeck = new Deck();  // Use public deck variable
        gameDeck.initializeBaseDeck(count);
        
        // Deal defuse cards to players
        dealService.dealDefuses(gameDeck, players);
        
        // Shuffle the deck
        gameDeck.shuffle(new Random());
        
        // Deal initial hands
        dealService.dealInitialHands(gameDeck, players, 5);
        
        // Add exploding kittens (number of players - 1)
        gameDeck.addExplodingKittens(count - 1);
        
        // Final shuffle
        gameDeck.shuffle(new Random());
    }

    /**
     * Sets up the game by prompting for player count, creating players,
     * preparing the deck, and initializing turn order.
     * @throws InvalidPlayerCountException if the player count is invalid
     * @throws InvalidNicknameException if any player nickname is invalid
     * @throws InvalidDeckException if deck preparation fails
     */
    public void setupGame() throws InvalidPlayerCountException, InvalidNicknameException, 
            InvalidDeckException {
        int count = view.promptPlayerCount();
        playerService.validateCount(count);
        
        List<Player> players = createPlayers(count);
        
        prepareDeck(count, players);
        GameContext.setGameDeck(gameDeck);
        initializeTurnOrder(players);
    }

    /**
     * Gets the current game deck.
     * @return a copy of the current game deck
     */
    public Deck getGameDeck() {
        if (gameDeck == null) {
            return null;
        }
        return new Deck(gameDeck);
    }
}
