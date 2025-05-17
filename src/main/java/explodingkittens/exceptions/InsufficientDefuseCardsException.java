package explodingkittens.exceptions;

public class InsufficientDefuseCardsException extends RuntimeException {
    public InsufficientDefuseCardsException(String message) {
        super(message);
    }

    public InsufficientDefuseCardsException() {
        super("Not enough defuse cards in the deck");
    }
} 