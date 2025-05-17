package explodingkittens.exceptions;

public class InvalidPlayersListException extends RuntimeException {
    public InvalidPlayersListException() {
        super("Players list cannot be null.");
    }
} 