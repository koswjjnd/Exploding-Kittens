package explodingkittens.model;

import java.util.List;

/**
 * Represents a Time Rewind card in the game.
 * When played, moves the top 3 cards from the deck to the bottom,
 * effectively avoiding potentially dangerous cards temporarily.
 */
public class TimeRewindCard extends Card {

    /**
     * Creates a new Time Rewind card.
     */
    public TimeRewindCard() {
        super(CardType.TIME_REWIND);
    }

    /**
     * Executes the effect of the Time Rewind card.
     * Moves the top 3 cards from the deck to the bottom.
     *
     * @param turnOrder The list of players in turn order (not used in this effect)
     * @param gameDeck The game deck to modify
     * @throws IllegalArgumentException if gameDeck is null
     * @throws IllegalStateException if the deck has fewer than 3 cards
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (gameDeck == null) {
            throw new IllegalArgumentException("Game deck cannot be null");
        }

        if (gameDeck.getCards().size() < 3) {
            throw new IllegalStateException(
                "Cannot rewind time: deck must have at least 3 cards"
            );
        }

        // Move top 3 cards to bottom
        for (int i = 0; i < 3; i++) {
            Card topCard = gameDeck.getCards().remove(0);
            gameDeck.getCards().add(topCard);
        }
    }
}