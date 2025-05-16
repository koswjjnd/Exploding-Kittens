package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.player.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import java.util.List;

public class DealService {
    
    /**
     * Deals defuse cards to players.
     * @param deck the deck to deal from
     * @param players the list of players to deal to
     * @throws InvalidDeckException if the deck is null
     * @throws EmptyDeckException if the deck is empty
     */
    public void dealDefuses(Deck deck, List<Player> players) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        if (deck.isEmpty()) {
            throw new EmptyDeckException();
        }
        // TODO: Implement the rest of the method
    }
} 