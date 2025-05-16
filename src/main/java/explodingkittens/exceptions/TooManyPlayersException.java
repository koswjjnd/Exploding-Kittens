package explodingkittens.exceptions;

public class TooManyPlayersException extends RuntimeException {
    public TooManyPlayersException() {
        super("Too many players. Maximum number of players is 4.");
    }
} 