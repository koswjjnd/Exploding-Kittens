package explodingkittens.model;

import java.util.List;



/**
 * Service class for dealing cards to players.
 */
public class DealService {
    
    /**
     * Deals one defuse card to each player.
     * @param players List of players to deal cards to
     * @throws IllegalArgumentException if the number of players is invalid (0 or >4)
     */
    public void dealDefuses(List<Player> players) {
        if (players == null || players.isEmpty() || players.size() > 4) {
            throw new IllegalArgumentException("Number of players must be between 1 and 4");
        }
        
        for (Player player : players) {
            player.receiveCard(new DefuseCard());
        }
    }

} 