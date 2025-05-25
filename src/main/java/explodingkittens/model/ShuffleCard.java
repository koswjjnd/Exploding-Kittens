package explodingkittens.model;
import java.util.List;

/**
 * A card that shuffles the deck when played.
 */
public class ShuffleCard extends Card {

    /**
     * Constructs a Shuffle card.
     */
    public ShuffleCard() {
        super(CardType.SHUFFLE);
    }

    /**
     * Play this card: triggers deck shuffle.
     * @param players the list of players in the game
     * @param deck the deck to shuffle
     * @throws IllegalArgumentException if deck is null
     */
    public void effect(List<Player> players, Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null when playing ShuffleCard.");
        }
        deck.shuffle();
        System.out.println("ShuffleCard played: deck has been shuffled!");
    }
}
