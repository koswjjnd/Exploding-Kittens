package explodingkittens.model;

import java.util.List;
import java.util.Random;
import explodingkittens.view.FavorCardView;

public class SnatchCard extends Card {
    
    private static final Random RANDOM = new Random();
    private final FavorCardView favorCardView;
    
    /**
     * Creates a new Snatch card.
     */
    public SnatchCard() {
        super(CardType.SNATCH);
        this.favorCardView = new FavorCardView();
    }
    
    /**
     * Effect of the Snatch card: Randomly takes a card from the target player's hand.
     * Uses FavorCardView's promptTargetPlayer method to select the target player.
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
        List<Player> availablePlayers = turnOrder.subList(1, turnOrder.size());
        int targetIndex = favorCardView.promptTargetPlayer(availablePlayers);
        Player targetPlayer = availablePlayers.get(targetIndex);
        
        List<Card> targetHand = targetPlayer.getHand();
        if (targetHand.isEmpty()) {
            throw new IllegalStateException("Target player has no cards to snatch");
        }
        
        int randomIndex = RANDOM.nextInt(targetHand.size());
        Card snatchedCard = targetHand.remove(randomIndex);
        currentPlayer.receiveCard(snatchedCard);
    }
} 