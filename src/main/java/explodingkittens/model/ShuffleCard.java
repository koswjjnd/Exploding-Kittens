package explodingkittens.model;
import java.util.List;

/**
 * Represents a Shuffle card in the game.
 * When played, the deck is shuffled.
 */
public class ShuffleCard extends Card {

    /**
     * Creates a new Shuffle card.
     */
    public ShuffleCard() {
        super(CardType.SHUFFLE);
    }

    /**
     * Executes the effect of the Shuffle card.
     * The deck is shuffled.
     *
     * @param players The list of players in turn order (not used in this effect)
     * @param gameDeck The game deck to be shuffled
     * @throws IllegalArgumentException if the game deck is null
     */
    @Override
    public void effect(List<Player> players, Deck gameDeck) {
        if (gameDeck == null) {
            throw new IllegalArgumentException("Game deck cannot be null");
        }
        gameDeck.shuffle();
        System.out.println("ShuffleCard played: deck has been shuffled!");
    }
}
