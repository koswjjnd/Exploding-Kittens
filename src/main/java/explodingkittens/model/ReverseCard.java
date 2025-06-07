package explodingkittens.model;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import explodingkittens.controller.GameContext;

/**
 * Represents a Reverse card in the game.
 * When played, the order of play is reversed 
 * and the current player's turn ends without drawing a card.
 * If played after being attacked, the order is reversed 
 * but only 1 of 2 turns is ended.
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

        // Get current player before reversing
        Player currentPlayer = turnOrder.get(0);
        
        // Handle turn end for current player
        if (currentPlayer.getLeftTurns() > 1) {
            currentPlayer.decrementLeftTurns();
        } 
        else {
            currentPlayer.setLeftTurns(0);  // End turn completely for normal turns
        }

        try {
            // Create a new ArrayList with reversed order
            List<Player> reversedOrder = new ArrayList<>(turnOrder);
            Collections.reverse(reversedOrder);
            
            // Set the new turn order in GameContext
            GameContext.setTurnOrder(reversedOrder);
            
            // Print the new turn order
            System.out.println("\nTurn order after Reverse card:");
            for (int i = 0; i < reversedOrder.size(); i++) {
                Player p = reversedOrder.get(i);
                System.out.println((i + 1) + ". " + p.getName() + 
                    (p.isAlive() ? "" : " (Eliminated)"));
            }
            System.out.println();
        } catch (Exception e) {
            // If something goes wrong during reverse, try to recover
            System.out.println("\nError during reverse operation: " + e.getMessage());
            System.out.println("Attempting to recover...");
            
            // Try to find a valid next player
            for (int i = 0; i < turnOrder.size(); i++) {
                if (turnOrder.get(i).isAlive()) {
                    GameContext.setCurrentPlayerIndex(i);
                    System.out.println("Recovered: Next player will be " + turnOrder.get(i).getName());
                    return;
                }
            }
            
            // If we can't find a valid player, throw a more specific exception
            throw new IllegalStateException("No valid players found after reverse operation");
        }
    }
}
