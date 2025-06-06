package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatCard.CatCardEffect;
import explodingkittens.model.ExplodingKittenCard;
import explodingkittens.view.GameView;
import explodingkittens.exceptions.GameOverException;
import java.util.List;
import explodingkittens.service.TurnService;
import explodingkittens.service.CardEffectService;
import explodingkittens.controller.GameContext;

/**
 * GameController manages the main game loop and game state for Exploding Kittens.
 * It handles player turns, card actions, and game progression.
 */
public class GameController {
    private final GameView view;
    private final TurnService turnService;

    /**
     * Constructs a GameController with the given view.
     * @param view the view to interact with the user
     */
    public GameController(GameView view) {
        this.view = view;
        this.turnService = new TurnService(view, new CardEffectService());
    }

    /**
     * Starts the game by running the main game loop.
     * @throws GameOverException if the game ends unexpectedly
     */
    public void start() throws GameOverException {
        try {
            // Main game loop
            while (!GameContext.isGameOver()) {
                try {
                    Player current = GameContext.getCurrentPlayer();
                    view.displayCurrentPlayer(current);

                    // Execute the current player's turn
                    turnService.takeTurn(current);

                    // Check if game is over after the turn
                    if (GameContext.isGameOver()) {
                        break;
                    }

                    // Move to next player's turn
                    GameContext.nextTurn();
                } catch (Exception e) {
                    System.err.println("Error in main game loop: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            }
            
            // Game over - determine and display winner
            handleGameOver();
        } 
        catch (Exception e) {
            System.err.println("Fatal error in game: " + e.getMessage());
            e.printStackTrace();
            throw new GameOverException("Game ended unexpectedly: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the end of the game.
     */
    private void handleGameOver() {
        if (GameContext.getTurnOrder().size() == 1) {
            view.displayWinner(GameContext.getTurnOrder().get(0));
        } 
        else {
            view.displayGameOver();
        }
    }
}