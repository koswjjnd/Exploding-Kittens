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
     * Displays a player's hand without card indices (for other players' hands).
     * @param player the player whose hand to display
     */
    void displayOtherPlayerHand(Player player);

    /**
     * Displays a player's hand for card selection.
     * @param player the player whose hand to display
     * @param hand the list of cards to display
     */
    void displayHandForSelection(Player player, List<Card> hand);

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
     * @param player the player
     * @param hand the player's hand
     * @return the card chosen by the player
     */
    Card promptPlayCard(Player player, List<Card> hand);

    /**
     * Displays when a player plays a card.
     * @param player the player who played the card
     * @param card the card that was played
     */
    void displayPlayedCard(Player player, Card card);

    /**
     * Prompts the player to select a card to play.
     * @param player The current player
     * @param hand The player's hand
     * @return The selected card, or null if player chooses to end turn
     */
    Card selectCardToPlay(Player player, List<Card> hand);

    /**
     * Shows an error message to the player.
     * @param message The error message
     */
    void showError(String message);

    /**
     * Shows that a card has been played.
     * @param player The player who played the card
     * @param card The card that was played
     */
    void showCardPlayed(Player player, Card card);

    /**
     * Checks if any player wants to play a Nope card.
     * @param player The player who played the card
     * @param card The card that was played
     * @return true if a Nope card was played, false otherwise
     */
    boolean checkForNope(Player player, Card card);

    /**
     * Shows that a card was noped.
     * @param player The player whose card was noped
     * @param card The card that was noped
     */
    void showCardNoped(Player player, Card card);

    /**
     * Shows that a card was drawn.
     * @param player The player who drew the card
     * @param card The card that was drawn
     */
    void showCardDrawn(Player player, Card card);

    /**
     * Confirms if the player wants to use their defuse card.
     * @param player The player who drew the exploding kitten
     * @return true if player wants to use defuse, false otherwise
     */
    boolean confirmDefuse(Player player);

    /**
     * Prompts the player to select a position to insert the exploding kitten.
     * @param deckSize the size of the deck
     * @return The selected position (0-based)
     */
    int selectExplodingKittenPosition(int deckSize);

    /**
     * Displays when a player uses a defuse card.
     * @param player the player who used the defuse card
     */
    void displayDefuseUsed(Player player);

    /**
     * Displays when a defuse card is successfully used and the exploding kitten is inserted.
     * @param player the player who used the defuse card
     * @param position the position where the exploding kitten was inserted
     */
    void displayDefuseSuccess(Player player, int position);

    /**
     * Prompts the player to play a Nope card.
     * @param player The player who played the card
     * @param card The card that was played
     * @return true if a Nope card was played, false otherwise
     */
    boolean promptPlayNope(Player player, Card card);
    
    /**
     * Displays when a Nope card was played.
     * @param player The player who played the Nope card
     */
    void displayPlayedNope(Player player);

    /**
     * Displays the current player's turn.
     * @param player the player whose turn it is
     */
    void showCurrentPlayerTurn(Player player);
} 