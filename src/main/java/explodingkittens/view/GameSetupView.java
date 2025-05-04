package explodingkittens.view;

/**
 * View interface for game setup phase, responsible for user interaction.
 */
public interface GameSetupView {
    /**
     * Prompts the user to input the nickname for the player at the given index.
     * @param playerIndex the index of the player (1-based)
     * @return the nickname entered by the user
     */
    String promptNickname(int playerIndex);

    /**
     * Displays an error message to the user.
     * @param message the error message to display
     */
    void showError(String message);
}
