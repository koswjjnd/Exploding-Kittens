package explodingkittens.model;

public abstract class Card implements Cloneable {
    @Override
    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Card cloning failed", e);
        }
    }
}