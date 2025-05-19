package explodingkittens.model;

import java.util.List;

/**
 * Represents an Attack card in the game.
 */
public class AttackCard extends Card {
    
    public AttackCard() {
        super(CardType.ATTACK);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 