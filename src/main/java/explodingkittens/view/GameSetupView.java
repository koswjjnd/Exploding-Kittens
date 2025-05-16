package explodingkittens.view;

import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * View interface for game setup phase, responsible for user interaction.
 */
public class GameSetupView {
    Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

    /**
     * Prompts the user to input the count for the player.
     *
     * @return tth count that from console
     */
    public int promptPlayerCount() {
        while (true) {
            try {
                System.out.print("Player Count: ");
                return sc.nextInt();
            }
            catch (InputMismatchException e) {
                showError("Please input a valid integer.");
                sc.nextLine();
            }
        }
    }
    /**
     * Prompts the user to input the nickname for the player at the given index.
     * @param playerIndex the index of the player (1-based)
     * @return the nickname entered by the user
     */
    public String promptNickname(int playerIndex){
        System.out.printf("Input Name of Player %d", playerIndex);
        return sc.nextLine();
    }

    /**
     * Displays an error message to the user.
     * @param message the error message to display
     */
    public void showError(String message){
        System.err.println(message);
    }
}