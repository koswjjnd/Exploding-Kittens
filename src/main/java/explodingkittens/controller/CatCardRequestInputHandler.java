package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.Card;
import java.util.List;

/**
 * Interface for handling cat card request input.
 * This interface is responsible for getting user input when a player wants to request a card.
 */
public interface CatCardRequestInputHandler {
    /**
     * Selects a target player from the list of available players.
     * @param availablePlayers The list of available target players
     * @return The selected target player
     */
    Player selectTargetPlayer(List<Player> availablePlayers);

    /**
     * Selects a card from the target player's hand.
     * @param targetPlayer The target player whose hand to select from
     * @return The selected card
     * @throws IllegalArgumentException if the selected card is not a functional card
     */
    Card selectCard(Player targetPlayer);
} 