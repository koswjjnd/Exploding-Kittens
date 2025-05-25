package explodingkittens.exceptions;

public class InvalidCardException extends Exception {
    public InvalidCardException() {
        super();
    }

    public InvalidCardException(String message) {
        super(message);
    }

    public InvalidCardException(String message, Throwable cause) {
        super(message, cause);
    }
} 