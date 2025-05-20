package explodingkittens.view;

import explodingkittens.model.Player;

/**
 * Interface for the game view that handles all user interactions and display.
 */
public interface GameView {
    /**
     * Displays the current player's turn.
     * @param player the current player
     */
    void displayCurrentPlayer(Player player);

    /**
     * Displays a player's hand.
     * @param player the player whose hand to display
     */
    void displayPlayerHand(Player player);

    /**
     * Prompts the player for their action.
     * @param player the player to prompt
     * @return the action chosen by the player
     */
    String promptPlayerAction(Player player);

    /**
     * Displays a message when a player is eliminated.
     * @param player the eliminated player
     */
    void displayPlayerEliminated(Player player);

    /**
     * Displays the winner of the game.
     * @param winner the winning player
     */
    void displayWinner(Player winner);

    /**
     * Displays the game over message.
     */
    void displayGameOver();
} 