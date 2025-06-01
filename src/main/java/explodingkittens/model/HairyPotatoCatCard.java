package explodingkittens.model;

import java.util.List;

/**
 * Represents a Hairy Potato Cat card in the game.
 * This is a special type of cat card that can be used in pairs to steal cards from other players.
 */
public class HairyPotatoCatCard extends CatCard {
    
    /**
     * Constructor for HairyPotatoCatCard.
     * Creates a new Hairy Potato Cat card with the HAIRY_POTATO_CAT type.
     */
    public HairyPotatoCatCard() {
        super(CatType.HAIRY_POTATO_CAT);
    }

    /**
     * The effect of the Hairy Potato Cat card.
     * @param turnOrder The order of players in the game.
     * @param gameDeck The deck of cards in the game.
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // Hairy Potato Cat cards are used in pairs to steal cards
        // The actual stealing logic is handled by the game controller
    }

    /**
     * Creates a clone of this Hairy Potato Cat card.
     * @return A new HairyPotatoCatCard instance
     */
    @Override
    public Card clone() {
        return new HairyPotatoCatCard();
    }
} 