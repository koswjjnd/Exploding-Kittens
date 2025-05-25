package explodingkittens.model.card;

import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.view.FavorCardView;
import java.util.List;

/**
 * Represents a Favor card in the game.
 * When played, the current player can ask another player to give them a card.
 * The target player must choose one card from their hand to give to the current player.
 */
public class FavorCard extends Card {
    private final FavorCardView view;

    /**
     * Creates a new Favor card.
     */
    public FavorCard() {
        super(CardType.FAVOR);
        this.view = new FavorCardView();
    }

    /**
     * Executes the effect of the Favor card.
     * The current player selects a target player, who must then choose a card from their hand to give to the current player.
     *
     * @param turnOrder The list of players in turn order
     * @param gameDeck The game deck (not used in this effect)
     * @throws IllegalStateException if the target player has no cards to give
     * @throws IllegalArgumentException if the target player is the current player
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // ... existing code ...
    }
} 