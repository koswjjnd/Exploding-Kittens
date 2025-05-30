package explodingkittens.model;

import java.util.List;

public class SkipCard extends Card {
    public SkipCard() {
        super(CardType.SKIP);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // Skip card effect: Reduce leftTurns by 1
        // Only skip the player (move to end) when leftTurns becomes 0
        Player currentPlayer = turnOrder.get(0);
        int currentLeftTurns = currentPlayer.getLeftTurns();
        
        if (currentLeftTurns > 0) {
            currentPlayer.setLeftTurns(currentLeftTurns - 1);
            // Only move player to end if they have no turns left
            if (currentPlayer.getLeftTurns() == 0) {
                turnOrder.remove(0);
                turnOrder.add(currentPlayer);
            }
        }
    }
} 