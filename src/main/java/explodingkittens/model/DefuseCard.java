package explodingkittens.model;

import java.util.List;

/**
 * Represents a Defuse card in the game.
 * When a player draws an Exploding Kitten card, they can use a Defuse card to survive.
 * The Exploding Kitten card is then placed back into the deck at a position of the player's choice.
 */
public class DefuseCard extends Card {
    /**
     * Constructor for DefuseCard.
     */
    public DefuseCard() {
        super(CardType.DEFUSE);
    }

    /**
     * The effect of the DefuseCard.
     * @param turnOrder The order of players in the game.
     * @param gameDeck The deck of cards in the game.
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // The actual effect is handled in the game controller when a player draws
        // an Exploding Kitten
        // This method is empty because the defuse logic is handled
        // in the game controller when a player draws an Exploding Kitten
    }
} 