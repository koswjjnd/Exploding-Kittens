package explodingkittens.exceptions;

public class InvalidDeckException extends RuntimeException {
    public InvalidDeckException(String message) {
        super(message);
    }

    public InvalidDeckException() {
        super("Deck cannot be null");
    }
} 