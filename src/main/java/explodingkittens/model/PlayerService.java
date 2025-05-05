package explodingkittens.model;

import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.player.Player;

public class PlayerService {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    /**
     * Validates the player count.
     * @param count the number of players
     * @throws InvalidPlayerCountException if the count is not within the valid range
     */
    public void validateCount(int count) throws InvalidPlayerCountException {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            throw new InvalidPlayerCountException(
                String.format("Player count must be between %d and %d", MIN_PLAYERS, MAX_PLAYERS)
            );
        }
    }
    
    /**
     * Creates a Player with the given nickname after validation.
     * @param rawNickname the raw nickname input
     * @return a new Player object
     * @throws InvalidNicknameException if the nickname is invalid
     */
    public Player createPlayer(String rawNickname) throws InvalidNicknameException {
        if (rawNickname == null) {
            throw new InvalidNicknameException("Nickname cannot be null");
        }
        
        String trimmedNickname = rawNickname.trim();
        if (trimmedNickname.isEmpty()) {
            throw new InvalidNicknameException("Nickname cannot be empty or only whitespace");
        }

        return new Player(trimmedNickname);
    }
}
