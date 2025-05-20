package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.ExplodingKittenCard;
import explodingkittens.view.GameView;
import explodingkittens.exceptions.GameOverException;
import java.util.List;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * GameController manages the main game loop and game state for Exploding Kittens.
 * It handles player turns, card actions, and game progression.
 */
public class GameController {
    private final GameView view;

    private final GameSetupController setupController;

    /**
     * Constructs a GameController with the given view and setup controller.
     * @param view the view to interact with the user
     * @param setupController the controller for game setup
     */
    public GameController(GameView view, GameSetupController setupController) {
        this.view = view;
        this.setupController = setupController;
    }

    /**
     * Starts the game by initializing setup and running the main game loop.
     * @throws GameOverException if the game ends unexpectedly
     */
    public void start() throws GameOverException {
        try {
            // Initialize game setup
            setupController.setupGame();
            
            // Main game loop
            while (!GameContext.isGameOver()) {
                Player currentPlayer = GameContext.getCurrentPlayer();
                view.displayCurrentPlayer(currentPlayer);
                
                // Handle player's turn
                handlePlayerTurn(currentPlayer);
                
                // Move to next player
                GameContext.nextTurn();
            }
            
            // Game over - determine and display winner
            handleGameOver();
        } 
        catch (Exception e) {
            throw new GameOverException("Game ended unexpectedly: " + e.getMessage(), e);
        }
    }

    /**
     * Handles a single player's turn.
     * @param player the current player
     */
    private void handlePlayerTurn(Player player) {
        // Process all turns for the current player
        while (player.getLeftTurns() > 0 && !GameContext.isGameOver()) {
            view.displayPlayerHand(player);
            
            // Get player's action
            String action = view.promptPlayerAction(player);
            
            // Process the action
            processPlayerAction(player, action);
            
            // Check if player is still alive
            if (!player.isAlive()) {
                GameContext.removePlayer(player);
                view.displayPlayerEliminated(player);
                break;
            }
            
            // Decrement remaining turns
            player.decrementLeftTurns();
        }
    }

    /**
     * Processes a player's action.
     * @param player the current player
     * @param action the action to process
     */
    private void processPlayerAction(Player player, String action) {
        if (!action.equals("draw")) {
            // TODO: Implement card action processing
            // This will be implemented by the card effect module
            return;
        }

        // Draw a card
        Card drawn = GameContext.getGameDeck().drawOne();
        
        if (!(drawn instanceof ExplodingKittenCard)) {
            player.receiveCard(drawn);
            return;
        }

        // Handle exploding kitten
        if (!player.hasDefuse()) {
            player.setAlive(false);
            return;
        }

        // Handle defuse card usage
        if (!player.useDefuse()) {
            player.setAlive(false);
            return;
        }

        // TODO: Handle defuse card insertion
        // This will be implemented by the card effect module
    }

    /**
     * Handles the end of the game.
     */
    private void handleGameOver() {
        List<Player> turnOrder = GameContext.getTurnOrder();
        if (turnOrder != null && turnOrder.size() == 1) {
            Player winner = turnOrder.get(0);
            view.displayWinner(winner);
        } 
        else {
            view.displayGameOver();
        }
    }
}