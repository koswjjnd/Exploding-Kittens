package explodingkittens.model;

import java.util.List;
import java.util.Random;

public class SnatchCard extends Card {
    
    public SnatchCard() {
        super(CardType.SNATCH);
    }
    
    /**
     * Effect of the Snatch card: Randomly takes a card from the target player's hand.
     * 
     * @param turnOrder List of players in turn order
     * @param gameDeck The game deck (not used in this effect)
     * @throws IllegalStateException if target player has no cards
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (turnOrder == null || turnOrder.size() < 2) {
            throw new IllegalArgumentException("Invalid turn order");
        }
        
        Player currentPlayer = turnOrder.get(0);
        Player targetPlayer = turnOrder.get(1);
        
        List<Card> targetHand = targetPlayer.getHand();
        if (targetHand.isEmpty()) {
            throw new IllegalStateException("Target player has no cards to snatch");
        }
        
        // Randomly select a card from target's hand
        Random random = new Random();
        int randomIndex = random.nextInt(targetHand.size());
        Card snatchedCard = targetHand.remove(randomIndex);
        
        // Add the card to current player's hand
        currentPlayer.receiveCard(snatchedCard);
    }
} 