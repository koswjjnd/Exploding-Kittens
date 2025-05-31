package explodingkittens.model;

import java.util.List;

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
public class CatRequestCard extends CatCard {
    private static CardRequestController controller;

    /**
     * Creates a new CatRequestCard with the given type.
     * @param type The type of cat card
     */
    public CatRequestCard(CatType type) {
        super(type);
    }

    /**
     * Sets the controller for handling card requests.
     * @param controller The controller to use
     */
    public static void setController(CardRequestController controller) {
        CatRequestCard.controller = controller;
    }

    /**
     * Effect of the cat request card.
     * The player must have three cat cards of the EXACT SAME TYPE.
     * The player can select a target player and a specific card to request.
     * If the target player has the requested card, they must give it to the current player.
     * If the target player doesn't have the requested card, the three cat cards are still discarded.
     * @param turnOrder The current turn order
     * @param gameDeck The game deck
     * @throws IllegalStateException if the player doesn't have three cat cards of the EXACT SAME TYPE
     * @throws IllegalStateException if there are no other players available
     * @throws IllegalStateException if the target player is dead
     * @throws IllegalStateException if the target player has no cards
     * @throws IllegalStateException if the player has no turns left
     * @throws IllegalStateException if the controller is not set
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (controller == null) {
            throw new IllegalStateException("Controller not set");
        }

        Player currentPlayer = turnOrder.get(0);
        if (currentPlayer.getLeftTurns() <= 0) {
            throw new IllegalStateException("No turns left");
        }

        // Check if player has three cat cards of the EXACT SAME TYPE
        List<Card> hand = currentPlayer.getHand();
        int sameTypeCount = 0;
        for (Card card : hand) {
            if (card instanceof CatCard && ((CatCard) card).getType() == this.getType()) {
                sameTypeCount++;
            }
        }
        if (sameTypeCount < 3) {
            throw new IllegalStateException("Need three cat cards of the EXACT SAME TYPE");
        }

        // Get available players (excluding current player and dead players)
        List<Player> availablePlayers = turnOrder.stream()
            .filter(p -> p != currentPlayer && p.isAlive())
            .toList();
        if (availablePlayers.isEmpty()) {
            throw new IllegalStateException("No other players available");
        }

        // Use controller to handle the request
        controller.handleCardRequest(currentPlayer, availablePlayers, this.getType());
    }
} 