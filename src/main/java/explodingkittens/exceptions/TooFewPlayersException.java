package explodingkittens.exceptions;

public class TooFewPlayersException extends RuntimeException {
    public TooFewPlayersException() {
        super("Too few players. Minimum number of players is 2.");
    }
} 