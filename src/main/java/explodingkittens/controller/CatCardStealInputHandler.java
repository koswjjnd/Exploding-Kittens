package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.CatType;
import java.util.List;

/**
 * Interface for handling cat card stealing input.
 * This interface is responsible for getting user input when a player wants to steal a card.
 */
public interface CatCardStealInputHandler {
    /**
     * Selects a target player from the list of available players.
     * @param availablePlayers The list of available target players
     * @return The selected target player
     */
    Player selectTargetPlayer(List<Player> availablePlayers);

    /**
     * Selects a card index from the target player's hand.
     * @param handSize The size of the target player's hand
     * @return The index of the selected card
     */
    int selectCardIndex(int handSize);

    /**
     * Handles the card stealing process.
     * @param currentPlayer The player making the steal
     * @param turnOrder The list of players in turn order
     * @param catType The type of cat card being used
     */
    void handleCardSteal(Player currentPlayer, List<Player> turnOrder, CatType catType);
} 