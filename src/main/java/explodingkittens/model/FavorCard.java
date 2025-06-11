package explodingkittens.model;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.view.FavorCardView;
import java.util.List;

/**
 * Represents a Favor card in the game.
 * When played, the current player can take a card from another player's hand.
 */
public class FavorCard extends Card {
    private FavorCardView view;

    /**
     * Creates a new Favor card.
     */
    public FavorCard() {
        super(CardType.FAVOR);
        this.view = new FavorCardView();
    }

    /**
     * Gets the view for testing purposes.
     * @return A copy of the view
     */
    public FavorCardView getView() {
        return new FavorCardView();
    }

    /**
     * Creates a new Favor card with a specific view (for testing purposes).
     * @param view The view to use
     */
    protected FavorCard(FavorCardView view) {
        super(CardType.FAVOR);
        this.view = view;
    }

    /**
     * Executes the effect of the Favor card.
     * The current player can take a card from another player's hand.
     *
     * @param turnOrder The list of players in turn order
     * @param gameDeck The game deck (not used in this effect)
     * @throws IllegalStateException if the target player has no cards
     * @throws IllegalArgumentException if the target player is the current player
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        if (turnOrder == null || turnOrder.isEmpty()) {
            throw new IllegalArgumentException("Turn order cannot be null or empty");
        }

        Player currentPlayer = turnOrder.get(0);
        List<Player> availablePlayers = turnOrder.subList(1, turnOrder.size());

        // Get target player selection
        int targetIndex = view.promptTargetPlayer(availablePlayers);
        Player targetPlayer = availablePlayers.get(targetIndex);

        // Check if target player has any cards
        List<Card> targetHand = targetPlayer.getRealHand();
        if (targetHand.isEmpty()) {
            throw new IllegalStateException("Target player has no cards to give");
        }

        // Get card selection
        int cardIndex = view.promptCardSelection(targetHand);
        Card selectedCard = targetHand.get(cardIndex);

        // Transfer the card
        targetHand.remove(cardIndex);
        currentPlayer.receiveCard(selectedCard);
    }
}