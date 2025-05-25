package explodingkittens.model;

import java.util.ArrayList;
import java.util.List;

public class SeeTheFutureCard extends Card {
    public SeeTheFutureCard() {
        super(CardType.SEE_THE_FUTURE);
    }
    
    @Override
    public void effect(List<Player> turnOrder, Deck deck) {
        List<Card> topCards = peekTopTwoCards(deck);
        System.out.println("Next 2 cards: " + topCards);
    }

    public List<Card> peekTopTwoCards(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null.");
        }
        int size = Math.min(2, deck.getCards().size());
        return new ArrayList<>(deck.getCards().subList(0, size));
    }

} 