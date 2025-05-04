package explodingkittens.model;

public class CatCard extends Card {
    private CatType type;
    
    public CatCard(CatType type) {
        this.type = type;
    }
    
    public CatType getType() {
        return type;
    }
} 