package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.player.Player;
import explodingkittens.exceptions.InvalidDeckException;
import java.util.List;

public class DealService {
    
    /**
     * Deals defuse cards to players.
     * @param deck the deck to deal from
     * @param players the list of players to deal to
     * @throws InvalidDeckException if the deck is null
     */
    public void dealDefuses(Deck deck, List<Player> players) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        // TODO: Implement the rest of the method
    }
} 