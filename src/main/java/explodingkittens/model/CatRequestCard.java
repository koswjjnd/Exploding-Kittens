package explodingkittens.model;

import explodingkittens.controller.CatCardRequestController;
import explodingkittens.controller.CatCardRequestInputHandler;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A cat card that allows the player to request a specific card from another player.
 * The player must have three cat cards of the EXACT SAME TYPE to use this card.
 * If the target player has the requested card, they must give it to the current player.
 * If the target player doesn't have the requested card, the three cat cards are still discarded.
 * 
 * For example:
 * - Three TACOCAT cards can be used to request a card
 * - Two TACOCAT cards and one BEARD_CAT card cannot be used
 * - Three BEARD_CAT cards can be used to request a card
 */
public final class CatRequestCard extends CatCard {
    private static final class ControllerHolder {
        private static volatile CatCardRequestInputHandler inputHandler;
        
        private ControllerHolder() {
            // Private constructor to prevent instantiation
        }
    }

    private final CatType catType;

    /**
     * Creates a new CatRequestCard with the given type.
     * @param type The type of cat card
     */
    public CatRequestCard(CatType type) {
        super(type);
        this.catType = type;
    }

    /**
     * Sets the controller for handling card requests.
     * @param newController The controller to set
     * @throws IllegalArgumentException if controller is null
     */
    public static synchronized void setController(CatCardRequestController newController) {
        if (newController == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }
        // Store only the input handler, which is immutable
        ControllerHolder.inputHandler = newController.getInputHandler();
    }

    /**
     * Gets the controller for handling card requests.
     * @return A new controller instance
     * @throws IllegalStateException if controller is not set
     */
    public static synchronized CatCardRequestController getController() {
        if (ControllerHolder.inputHandler == null) {
            throw new IllegalStateException("Controller not set");
        }
        // Create a new controller instance with the stored input handler
        return new CatCardRequestController(ControllerHolder.inputHandler);
    }

    /**
     * Effect of the cat request card.
     * The player must have three cat cards of the EXACT SAME TYPE.
     * The player can select a target player and a specific card to request.
     * If the target player has the requested card, they must give it to the current player.
     * If the target player doesn't have the requested card, the three cat cards are still 
     * discarded.
     * @param turnOrder The current turn order
     * @param gameDeck The game deck
     * @throws IllegalStateException if the player doesn't have three cat cards of the EXACT 
     *         SAME TYPE
     * @throws IllegalStateException if there are no other players available
     * @throws IllegalStateException if the target player is dead
     * @throws IllegalStateException if the target player has no cards
     * @throws IllegalStateException if the player has no turns left
     * @throws IllegalStateException if the controller is not set
     * @throws IllegalArgumentException if turn order is null or empty
     * @throws IllegalArgumentException if current player is null
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (turnOrder == null || turnOrder.isEmpty()) {
            throw new IllegalArgumentException("Turn order cannot be null or empty");
        }

        Player currentPlayer = turnOrder.get(0);
        if (currentPlayer == null) {
            throw new IllegalArgumentException("Current player cannot be null");
        }

        if (ControllerHolder.inputHandler == null) {
            throw new IllegalStateException("Controller not set");
        }

        if (currentPlayer.getLeftTurns() <= 0) {
            throw new IllegalStateException("No turns left");
        }

        validateCatCards(currentPlayer);
        List<Player> availablePlayers = getAvailablePlayers(turnOrder, currentPlayer);
        getController().handleCardRequest(currentPlayer, availablePlayers, catType);
    }

    private void validateCatCards(Player currentPlayer) {
        List<Card> hand = currentPlayer.getHand();
        int sameTypeCount = 0;
        for (Card card : hand) {
            if (card instanceof CatCard 
                    && ((CatCard) card).getCatType() == catType) {
                sameTypeCount++;
            }
        }
        if (sameTypeCount < 3) {
            throw new IllegalStateException(
                "Need three cat cards of the EXACT SAME TYPE");
        }
    }

    private List<Player> getAvailablePlayers(List<Player> turnOrder, Player currentPlayer) {
        List<Player> availablePlayers = turnOrder.stream()
            .filter(p -> p != currentPlayer && p.isAlive() && !p.getHand().isEmpty())
            .collect(Collectors.toList());
        if (availablePlayers.isEmpty()) {
            throw new IllegalStateException("No other players available");
        }
        return availablePlayers;
    }
} 