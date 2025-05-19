package explodingkittens.model;

import java.util.List;

/**
 * Represents an Attack card in the Exploding Kittens game.
 * When played, transfers current player's left turns + 2 to the next player.
 */
public class AttackCard extends Card {
    
    /**
     * Creates a new Attack card.
     */
    public AttackCard() {
        super(CardType.ATTACK);
    }

    /**
     * Executes the effect of the Attack card.
     * Transfers current player's left turns + 2 to the next player.
     *
     * @param turnOrder The list of players in turn order
     * @param gameDeck The game deck
     * @throws IllegalArgumentException if turnOrder is empty
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (turnOrder.isEmpty()) {
            throw new IllegalArgumentException("Turn order cannot be empty");
        }

        Player currentPlayer = turnOrder.get(0);
        Player nextPlayer = turnOrder.get(1);
        
        // Get current player's left turns and add 2
        int currentLeftTurns = currentPlayer.getLeftTurns();
        int newLeftTurns = currentLeftTurns + 2;
        
        // Set next player's left turns
        nextPlayer.setLeftTurns(newLeftTurns);
    }
} 