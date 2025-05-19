package explodingkittens.model;

/**
 * Represents a basic card in the Exploding Kittens game.
 */
public class BasicCard extends Card {
    private final CardType type;

    /**
     * Creates a new basic card with the specified type.
     * 
     * @param type The type of the card
     */
    public BasicCard(CardType type) {
        super(type);
        this.type = type;
    }

    /**
     * Gets the type of the card.
     * 
     * @return The type of the card
     */
    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        BasicCard other = (BasicCard) obj;
        return type == other.type;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + type.hashCode();
    }

    @Override
    public String toString() {
        return type.name();
    }
} 