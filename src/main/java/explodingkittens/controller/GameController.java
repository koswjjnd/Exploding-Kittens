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

/**
 * GameController manages the main game loop and game state for Exploding Kittens.
 * It handles player turns, card actions, and game progression.
 */
public class GameController {
    private final GameView view;

    /**
     * Constructs a GameController with the given view.
     * @param view the view to interact with the user
     */
    public GameController(GameView view) {
        this.view = view;
    }

    /**
     * Starts the game by running the main game loop.
     * @throws GameOverException if the game ends unexpectedly
     */
    public void start() throws GameOverException {
        try {
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
        handleDrawAction(player);
    }

    /**
     * Handles the draw action for a player.
     * @param player the current player
     */
    private void handleDrawAction(Player player) {
        Card drawn = GameContext.getGameDeck().drawOne();
        if (!(drawn instanceof ExplodingKittenCard)) {
            player.receiveCard(drawn);
            return;
        }
        handleExplodingKittenDraw(player, drawn);
    }

    /**
     * Handles the scenario when a player draws an Exploding Kitten card.
     * @param player the current player
     * @param drawn the drawn Exploding Kitten card
     */
    private void handleExplodingKittenDraw(Player player, Card drawn) {
        if (!player.hasDefuse()) {
            player.setAlive(false);
            view.displayPlayerEliminated(player);
            return;
        }
        if (!player.useDefuse()) {
            player.setAlive(false);
            view.displayPlayerEliminated(player);
            return;
        }
        view.displayDefuseUsed(player);
        int deckSize = GameContext.getGameDeck().size();
        int position = view.promptDefusePosition(deckSize);
        GameContext.getGameDeck().insertAt(drawn, position);
        view.displayDefuseSuccess(player, position);
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

    /**
     * Handles the effect of a cat card.
     * @param card The cat card to handle
     * @param turnOrder The current turn order
     * @param gameDeck The game deck
     */
    private void handleCatCard(CatCard card, List<Player> turnOrder, Deck gameDeck) {
        try {
            card.effect(turnOrder, gameDeck);
        } 
        catch (CatCardEffect effect) {
            // Check for Nope cards
            if (!checkForNope(turnOrder)) {
                // No Nope cards played, execute the effect
                Player currentPlayer = turnOrder.get(0);
                
                // Remove the cat card pair
                currentPlayer.removeCard(effect.getFirstCard());
                currentPlayer.removeCard(effect.getSecondCard());
                
                // Get and remove the target card
                Card cardToSteal = effect.getTargetPlayer()
                    .getHand()
                    .get(effect.getTargetCardIndex());
                if (effect.getTargetPlayer().removeCard(cardToSteal)) {
                    currentPlayer.receiveCard(cardToSteal);
                }
            }
        }
    }

    /**
     * Checks if any player wants to play a Nope card.
     * @param turnOrder The current turn order
     * @return true if a Nope card was played, false otherwise
     */
    private boolean checkForNope(List<Player> turnOrder) {
        // TODO: Implement Nope card checking logic
        return false;
    }
}