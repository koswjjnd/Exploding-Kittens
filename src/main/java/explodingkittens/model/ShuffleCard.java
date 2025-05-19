package explodingkittens.model;

import java.util.List;

public class ShuffleCard extends Card {
    public ShuffleCard() {
        super(CardType.SHUFFLE);
    }
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 