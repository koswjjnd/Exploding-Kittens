package explodingkittens.model;

import java.util.List;

/**
 * Represents an Exploding Kitten card in the game.
 */
public class ExplodingKittenCard extends Card {
    public ExplodingKittenCard() {
        super(CardType.EXPLODING_KITTEN);
    }
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 