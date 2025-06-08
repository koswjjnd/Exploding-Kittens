package explodingkittens.model;

import java.util.List;
import explodingkittens.controller.GameContext;
import explodingkittens.service.NopeService;
import explodingkittens.view.GameView;

/**
 * Represents an Attack card in the Exploding Kittens game.
 * When played, transfers current player's left turns + 2 to the next player.
 */
public class AttackCard extends Card {
    private NopeService nopeService;
    private GameView view;
    
    /**
     * Creates a new Attack card.
     */
    public AttackCard() {
        super(CardType.ATTACK);
        this.nopeService = new NopeService(null); // Will be set by TurnService
        this.view = null; // Will be set by TurnService
    }

    /**
     * Sets the NopeService and GameView for this card.
     * @param nopeService the NopeService to use
     * @param view the GameView to use
     */
    public void setServices(NopeService nopeService, GameView view) {
        this.nopeService = nopeService;
        this.view = view;
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

        // Check for Nope cards before modifying turn order
        if (nopeService != null && view != null && nopeService.isNegatedByPlayers(this)) {
            view.showCardNoped(turnOrder.get(0), this);
            return;
        }

        Player currentPlayer = turnOrder.get(0);
        Player nextPlayer = turnOrder.get(1);
        
        // Get current player's left turns and add 2
        int currentLeftTurns = currentPlayer.getLeftTurns();
        int newLeftTurns = currentLeftTurns + 2;
        
        // Set current player's turns to 0 and next player's turns to new value
        currentPlayer.setLeftTurns(0);
        nextPlayer.setLeftTurns(newLeftTurns);
        
        // Move current player to end of turn order
        GameContext.movePlayerToEnd(currentPlayer);
    }
} 