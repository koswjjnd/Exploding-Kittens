package explodingkittens.model;

import java.util.List;

public class SuperSkipCard extends Card {
    public SuperSkipCard() {
        super(CardType.SUPER_SKIP);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // SuperSkip card effect: Unconditionally skip current player
        Player currentPlayer = turnOrder.get(0);
        int currentLeftTurns = currentPlayer.getLeftTurns();
        
        if (currentLeftTurns <= 0) {
            throw new IllegalStateException(
                "Cannot use SuperSkip card when leftTurns is 0 or negative"
            );
        }
        
        // Set leftTurns to 0 and move player to end
        currentPlayer.setLeftTurns(0);
        turnOrder.remove(0);
        turnOrder.add(currentPlayer);
    }
} 