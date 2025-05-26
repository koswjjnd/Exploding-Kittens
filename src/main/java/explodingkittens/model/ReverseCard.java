package explodingkittens.model;

import java.util.List;
import java.util.Collections;

/**
 * Represents a Reverse card in the game.
 * When played, the order of play is reversed and the current player's turn ends without drawing a card.
 * If played after being attacked, the order is reversed but only 1 of 2 turns is ended.
 */
public class ReverseCard extends Card {

    /**
     * Creates a new Reverse card.
     */
    public ReverseCard() {
        super(CardType.REVERSE);
    }

    /**
     * Executes the effect of the Reverse card.
     * Reverses the turn order and ends the current player's turn without drawing a card.
     * If the player was attacked, only 1 of 2 turns is ended.
     *
     * @param turnOrder The list of players in turn order
     * @param gameDeck The game deck (not used in this effect)
     * @throws IllegalArgumentException if turnOrder is null or empty
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (turnOrder == null || turnOrder.isEmpty()) {
            throw new IllegalArgumentException("Turn order cannot be null or empty");
        }

        // Reverse the turn order
        Collections.reverse(turnOrder);

        // If the player was attacked (leftTurns > 1), only end 1 turn
        Player currentPlayer = turnOrder.get(0);
        if (currentPlayer.getLeftTurns() > 1) {
            currentPlayer.decrementLeftTurns();
        } else {
            currentPlayer.setLeftTurns(0);
        }
    }
}
