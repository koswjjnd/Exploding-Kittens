package explodingkittens.model;

import java.util.List;

public class SkipCard extends Card {
    public SkipCard() {
        super(CardType.SKIP);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // Skip card effect: Reduce leftTurns by 1, but not below 0
        Player currentPlayer = turnOrder.get(0);
        int currentLeftTurns = currentPlayer.getLeftTurns();
        
        if (currentLeftTurns <= 0) {
            throw new IllegalStateException("Cannot use Skip card when leftTurns is 0 or negative");
        }
        
        // Reduce by 1
        int newLeftTurns = currentLeftTurns - 1;
        currentPlayer.setLeftTurns(newLeftTurns);
        
        // Only move player to end if they have no turns left
        if (newLeftTurns == 0) {
            turnOrder.remove(0);
            turnOrder.add(currentPlayer);
        }
    }
} 