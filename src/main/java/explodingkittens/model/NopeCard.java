package explodingkittens.model;

import java.util.List;

public class NopeCard extends Card {
    public NopeCard() {
        super(CardType.NOPE);
    }
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 