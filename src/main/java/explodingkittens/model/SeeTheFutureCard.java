package explodingkittens.model;

import java.util.List;

public class SeeTheFutureCard extends Card {
    public SeeTheFutureCard() {
        super(CardType.SEE_THE_FUTURE);
    }
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
    }
} 