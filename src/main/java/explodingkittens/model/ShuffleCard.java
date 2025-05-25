package explodingkittens.model;
import java.util.List;

public class ShuffleCard extends Card {

    public ShuffleCard() {
        super(CardType.SHUFFLE);
    }

    /**
     * Play this card: triggers deck shuffle.
     * @param deck the deck to shuffle
     */
    public void effect(List<Player> players, Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null when playing ShuffleCard.");
        }
        deck.shuffle();
        System.out.println("ShuffleCard played: deck has been shuffled!");
    }
}
