package explodingkittens.model;

import java.util.List;

/**
 * Abstract class representing a card in the game.
 */
public abstract class Card implements Cloneable {
    private final CardType type;

    /**
     * Constructs a card with the specified type.
     * @param type the type of the card
     */
    public Card(CardType type) {
        this.type = type;
    }

    /**
     * Gets the type of the card.
     * @return the card type
     */
    public CardType getType() {
        return type;
    }
    
    /**
     * Execute the effect of this card.
     * 
     * @param turnOrder The list of players in the current turn order
     * @param gameDeck The game deck
     */
    public abstract void effect(List<Player> turnOrder, Deck gameDeck);
    
    @Override
    public Card clone() {
        try {
            return (Card) super.clone();
        } 
        catch (CloneNotSupportedException e) {
            throw new AssertionError("Card cloning failed", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}