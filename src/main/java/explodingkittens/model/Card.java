package explodingkittens.model;

public abstract class Card implements Cloneable {
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