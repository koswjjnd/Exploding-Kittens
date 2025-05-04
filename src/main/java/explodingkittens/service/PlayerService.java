package explodingkittens.service;

import explodingkittens.exceptions.InvalidPlayerCountException;

public class PlayerService {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    public void validateCount(int count) throws InvalidPlayerCountException {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            throw new InvalidPlayerCountException(
                String.format("Player count must be between %d and %d", MIN_PLAYERS, MAX_PLAYERS)
            );
        }
    }
} 