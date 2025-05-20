package explodingkittens.view;

import explodingkittens.model.Player;
import explodingkittens.model.Card;
import java.util.List;

/**
 * Interface for the game view that handles all user interactions and display.
 */
public interface GameView {
    /**
     * Displays the current player.
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
     * @param player the current player
     * @return the action chosen by the player ("draw" or "play")
     */
    String promptPlayerAction(Player player);

    /**
     * Displays when a player is eliminated.
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

    /**
     * Displays the result of drawing a card.
     * @param card the card that was drawn
     */
    void displayDrawResult(Card card);

    /**
     * Prompts the player to choose a position to insert the defuse card.
     * @param deckSize the size of the deck
     * @return the position chosen by the player
     */
    int promptDefusePosition(int deckSize);

    /**
     * Prompts the player to choose a card to play.
     * @param hand the player's hand
     * @return the card chosen by the player
     */
    Card promptPlayCard(List<Card> hand);

    /**
     * Displays when a player plays a card.
     * @param player the player who played the card
     * @param card the card that was played
     */
    void displayPlayedCard(Player player, Card card);
} 