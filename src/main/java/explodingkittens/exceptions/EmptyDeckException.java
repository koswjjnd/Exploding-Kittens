package explodingkittens.exceptions;

public class EmptyDeckException extends RuntimeException {
    public EmptyDeckException(String message) {
        super(message);
    }

    public EmptyDeckException() {
        super("Deck is empty");
    }
} 