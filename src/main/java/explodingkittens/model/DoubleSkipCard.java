package explodingkittens.model;

import java.util.List;
import explodingkittens.controller.GameContext;

public class DoubleSkipCard extends Card {
    public DoubleSkipCard() {
        super(CardType.DOUBLE_SKIP);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // DoubleSkip card effect: Reduce leftTurns by 2, but not below 0
        Player currentPlayer = turnOrder.get(0);
        int currentLeftTurns = currentPlayer.getLeftTurns();
        
        if (currentLeftTurns <= 0) {
            throw new IllegalStateException(
                "Cannot use DoubleSkip card when leftTurns is 0 or negative"
            );
        }
        
        // Reduce by 2, but not below 0
        int newLeftTurns = Math.max(0, currentLeftTurns - 2);
        currentPlayer.setLeftTurns(newLeftTurns);
        
        // Only move player to end if they have no turns left
        if (newLeftTurns == 0) {
            GameContext.movePlayerToEnd(currentPlayer);
        }
    }
} 