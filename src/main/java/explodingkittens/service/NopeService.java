package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.BasicCard;
import explodingkittens.model.CardType;
import explodingkittens.model.Player;
import java.util.List;

/**
 * Service class that handles the "Nope" card functionality in the game.
 * A Nope card can negate the effect of another card, but if multiple Nope cards
 * are played in response, they cancel each other out.
 * 
 * This service implements the game rule where:
 * - An odd number of Nope cards negates the effect of the target card
 * - An even number of Nope cards (including zero) allows the effect to proceed
 * - Multiple Nope cards can be played in response to each other
 */
public class NopeService {
    
    /**
     * Determines if a card's effect is negated based on the number of Nope cards played.
     * This method implements the core Nope card game mechanic where:
     * - Each Nope card cancels out the effect of the previous Nope card
     * - The final result depends on whether there is an odd or even number of Nope cards
     * 
     * @param player The player who played the card being potentially negated.
     *              This parameter is used for validation and future extensibility.
     *              Must not be null.
     * 
     * @param nopeCards The list of Nope cards played in response to the target card.
     *                 The order of cards in the list represents the sequence in which
     *                 they were played. Must not be null, but can be empty.
     *                 Can contain any type of Card, but only BasicCard instances with
     *                 type NOPE are counted.
     * 
     * @return true if the effect is negated (odd number of Nope cards), false otherwise.
     *         - Returns true when there is an odd number of valid Nope cards
     *         - Returns false when there is an even number of valid Nope cards (including zero)
     * 
     * @throws IllegalArgumentException if player is null
     * @throws IllegalArgumentException if nopeCards is null
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
            if (card instanceof BasicCard && CardType.NOPE.equals(((BasicCard) card).getType())) {
                nopeCount++;
            }
        }
        
        // If there's an odd number of Nope cards, the effect is negated
        return nopeCount % 2 != 0;
    }
} 