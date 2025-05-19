package explodingkittens.model;

import java.util.List;

public class DefuseCard extends Card {
    public DefuseCard() {
        super(CardType.DEFUSE);
    }
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 