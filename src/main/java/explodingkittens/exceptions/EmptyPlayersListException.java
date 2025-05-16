package explodingkittens.exceptions;

public class EmptyPlayersListException extends RuntimeException {
    public EmptyPlayersListException() {
        super("Players list cannot be empty.");
    }
} 