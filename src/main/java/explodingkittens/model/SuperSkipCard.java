package explodingkittens.model;

import java.util.List;

public class SuperSkipCard extends Card {
    public SuperSkipCard() {
        super(CardType.SUPER_SKIP);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // SuperSkip card effect: Skip current player's turn and move them to the end
        // regardless of their leftTurns value
        Player currentPlayer = turnOrder.get(0);
        turnOrder.remove(0);
        turnOrder.add(currentPlayer);
    }
} 