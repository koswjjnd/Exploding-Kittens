package explodingkittens.view;

import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.util.I18nUtil;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * View interface for game setup phase, responsible for user interaction.
 */
public class GameSetupView {
    @SuppressFBWarnings({"EI_EXPOSE_REP2", "MS_MUTABLE_COLLECTION"})
    private final Scanner sc;

    /**
     * Constructor that accepts a Scanner instance.
     * @param scanner the Scanner to use for input
     */
    public GameSetupView(Scanner scanner) {
        // 直接使用传入的 Scanner 对象
        // this.sc = new Scanner(scanner.useDelimiter("\\A").next());
        this.sc = scanner;
    }

    /**
     * Prompts the user to input the count for the player.
     *
     * @return tth count that from console
     */
    public int promptPlayerCount() {
        while (true) {
            try {
                System.out.print(I18nUtil.getMessage("setup.player.count") + "\n");
                int count = sc.nextInt();
                sc.nextLine(); // 消费换行符
                return count;
            }
            catch (InputMismatchException e) {
                showError(I18nUtil.getMessage("setup.error.invalid.integer"));
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
        System.out.print(I18nUtil.getMessage("setup.player.name", playerIndex) + " ");
        String nickname = sc.nextLine();
        return nickname;
    }

    /**
     * Displays an error message to the user.
     * @param message the error message to display
     */
    public void showError(String message){
        System.err.println(message);
    }
}