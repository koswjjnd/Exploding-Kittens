package explodingkittens.model;

public class PlayerService {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    /**
     * Validates that the number of players is within the allowed range [2, 4].
     *
     * @param count the number of players
     * @throws IllegalArgumentException if count < 2 or count > 4
     */
    public void validateCount(int count) {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    String.format("Player count must be between %d and %d, but was %d",
                            MIN_PLAYERS, MAX_PLAYERS, count));
        }
    }
}
