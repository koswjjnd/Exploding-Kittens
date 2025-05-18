package explodingkittens.model;

/**
 * Represents a basic card in the Exploding Kittens game.
 */
public class BasicCard extends Card {
    private final String type;

    /**
     * Creates a new basic card with the specified type.
     * 
     * @param type The type of the card (e.g., "Attack", "Skip", "Favor", "Nope")
     */
    public BasicCard(String type) {
        this.type = type;
    }

    /**
     * Gets the type of the card.
     * 
     * @return The type of the card
     */
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        BasicCard other = (BasicCard) obj;
        return type.equals(other.type);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + type.hashCode();
    }

    @Override
    public String toString() {
        return type;
    }
} 