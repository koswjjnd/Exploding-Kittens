package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.BasicCard;
import explodingkittens.model.Player;
import java.util.List;

/**
 * Service class that handles the "Nope" card functionality in the game.
 * A Nope card can negate the effect of another card, but if multiple Nope cards
 * are played in response, they cancel each other out.
 */
public class NopeService {
    
    /**
     * Determines if a card's effect is negated based on the number of Nope cards played.
     * 
     * @param player The player who played the card being potentially negated
     * @param nopeCards The list of Nope cards played in response
     * @return true if the effect is negated (odd number of Nope cards), false otherwise
     * @throws IllegalArgumentException if player or nopeCards is null
     */
    public boolean isNegated(Player player, List<? extends Card> nopeCards) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (nopeCards == null) {
            throw new IllegalArgumentException("Nope cards list cannot be null");
        }
        
        // Count the number of Nope cards
        int nopeCount = 0;
        for (Card card : nopeCards) {
            if (card instanceof BasicCard && "Nope".equals(((BasicCard) card).getType())) {
                nopeCount++;
            }
        }
        
        // If there's an odd number of Nope cards, the effect is negated
        return nopeCount % 2 != 0;
    }
} 