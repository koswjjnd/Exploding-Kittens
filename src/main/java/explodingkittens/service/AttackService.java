package explodingkittens.service;

import explodingkittens.model.Player;
import java.util.List;

/**
 * Service interface for handling attack card effects in the game.
 * This service is responsible for managing the attack mechanics,
 * such as adding extra turns to the next player.
 */
public interface AttackService {
    
    /**
     * Handles the attack effect by adding extra turns to the next player.
     * 
     * @param turnOrder The list of players in the current turn order
     * @param times The number of extra turns to add
     */
    void handleAttack(List<Player> turnOrder, int times);
} 