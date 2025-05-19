package explodingkittens.model;

import java.util.List;

public class SkipCard extends Card {
    public SkipCard() {
        super(CardType.SKIP);
    }
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 