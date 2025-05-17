package explodingkittens.exceptions;

public class NoDefuseCardsException extends RuntimeException {
    public NoDefuseCardsException() {
        super("No defuse cards in the deck.");
    }
} 