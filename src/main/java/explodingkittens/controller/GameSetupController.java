package explodingkittens.controller;

import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.model.Player;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.model.Deck;
import explodingkittens.service.DealService;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.model.CatCard;
import explodingkittens.model.ConsoleCardStealInputHandler;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
     * Sets up the game by initializing players, deck, and turn order.
     * @throws InvalidPlayerCountException when the player count is invalid
     * @throws InvalidDeckException when the deck setup fails
     */
    public void setupGame() throws InvalidPlayerCountException, InvalidDeckException {
        // Get player count
        int playerCount = view.promptPlayerCount();
        if (playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS) {
            throw new InvalidPlayerCountException(
                "Number of players must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS);
        }

        // Create players
        List<Player> players = createPlayers(playerCount);

        // Initialize deck and deal cards
        Deck gameDeck = initializeDeck(playerCount, players);

        // Initialize GameContext with game state
        GameContext.setGameDeck(gameDeck);
        GameContext.setTurnOrder(players);
        GameContext.setGameOver(false);

        Scanner scanner = new Scanner(System.in);
        CatCard.setInputHandler(new ConsoleCardStealInputHandler(scanner));
    }

    /**
     * Initializes the game deck with cards and deals them to players.
     * @param playerCount the number of players in the game
     * @param players the list of players to deal cards to
     * @return the initialized game deck
     * @throws InvalidDeckException when the deck setup fails
     */
    private Deck initializeDeck(int playerCount, List<Player> players) throws InvalidDeckException {
        Deck gameDeck = new Deck();
        gameDeck.initializeBaseDeck(playerCount);
        
        // Deal defuse cards to players
        dealService.dealDefuses(gameDeck, players);
        
        // Shuffle the deck
        gameDeck.shuffle(new Random());
        
        // Deal initial hands
        dealService.dealInitialHands(gameDeck, players, 5);
        
        // Add exploding kittens (number of players - 1)
        gameDeck.addExplodingKittens(playerCount - 1);
        
        // Final shuffle
        gameDeck.shuffle(new Random());

        return gameDeck;
    }

    /**
     * Creates a list of players by prompting for nicknames and validating them.
     * @param count the number of players to create
     * @return a list of created Player objects
     */
    private List<Player> createPlayers(int count) {
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
