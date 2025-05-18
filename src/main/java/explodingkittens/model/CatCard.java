package explodingkittens.model;

/**
 * Represents a Cat card in the Exploding Kittens game.
 * Cat cards are used in pairs to steal cards from other players.
 */
public class CatCard extends Card {
    private CatType type;
    
    /**
     * Creates a new Cat card with the specified type.
     * @param type The type of the cat card
     */
    public CatCard(CatType type) {
        super(CardType.CAT_CARD);
        this.type = type;
    }
    
    /**
     * Gets the type of this cat card.
     * @return The type of the cat card
     */
    public CatType getCatType() {
        return type;
    }
} 