package explodingkittens.model;

import java.util.List;

/**
 * Interface for handling user input during card stealing.
 */
public interface CardStealInputHandler {
    /**
     * Prompts the user to select a target player from the list of available players.
     * @param availablePlayers List of players that can be targeted
     * @return The selected target player, or null if no player was selected
     */
    Player selectTargetPlayer(List<Player> availablePlayers);

    /**
     * Prompts the user to select a card index from the target player's hand.
     * @param handSize The size of the target player's hand
     * @return The index of the selected card (0-based)
     */
    int selectCardIndex(int handSize);
}