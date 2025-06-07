package explodingkittens.model;

import java.util.List;
import explodingkittens.controller.GameContext;

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
            throw new IllegalStateException(
                "Cannot use Skip card when you have no turns left. " +
                "You must draw a card first."
            );
        }
        
        // Reduce by 1
        int newLeftTurns = currentLeftTurns - 1;
        currentPlayer.setLeftTurns(newLeftTurns);
        
        // Only move player to end if they have no turns left
        if (newLeftTurns == 0) {
            GameContext.movePlayerToEnd(currentPlayer);
        }
    }
} 