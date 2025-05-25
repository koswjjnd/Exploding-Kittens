package explodingkittens.model;

import java.util.List;

/**
 * Represents the Draw From Bottom card.
 * Ends your turn by drawing the bottom card from the draw pile.
 * If played as a defense to an Attack Card, each card ends 1 attack turn.
 */
public class DrawFromBottomCard extends Card {
    public DrawFromBottomCard() {
        super(CardType.DRAW_FROM_BOTTOM);
    }

    /**
     * Executes the effect of the Draw From Bottom card.
     * Draws the bottom card from the deck and gives it to the current player.
     *
     * @param turnOrder The list of players in turn order (current player is first)
     * @param deck The game deck
     * @throws IllegalStateException if the deck is empty
     */
    @Override
    public void effect(List<Player> turnOrder, Deck deck) {
        if (turnOrder == null || turnOrder.isEmpty()) {
            throw new IllegalArgumentException("Turn order cannot be null or empty");
        }
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        if (deck.getCards().isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        Player currentPlayer = turnOrder.get(0);
        // Remove and get the bottom card
        int lastIndex = deck.getCards().size() - 1;
        Card bottomCard = deck.getCards().remove(lastIndex);
        currentPlayer.receiveCard(bottomCard);
    }

    /**
     * Used as a defense to Attack: each call ends 1 attack turn.
     * This can be handled by the TurnService or game logic by calling effect() for each card played.
     */
}