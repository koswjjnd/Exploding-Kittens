package explodingkittens.exceptions;

public class GameOverException extends Exception {
    public GameOverException(String message) {
        super(message);
    }

    public GameOverException(String message, Throwable cause) {
        super(message, cause);
    }
} 