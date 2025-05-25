package explodingkittens.view;

import explodingkittens.model.Player;
import java.util.List;
import java.util.Scanner;

/**
 * View class for handling Favor card interactions.
 * Responsible for displaying available players and getting user input.
 */
public class FavorCardView {
    private final Scanner scanner;
    private String userInput; // 用于测试时模拟用户输入
    
    /**
     * Creates a new FavorCardView with a Scanner for user input.
     */
    public FavorCardView() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Sets the user input for testing purposes.
     * @param input The input to simulate
     */
    void setUserInput(String input) {
        this.userInput = input;
    }
    
    /**
     * Prompts the user to select a target player from the available players.
     * 
     * @param availablePlayers The list of players that can be selected
     * @return The index of the selected player in the availablePlayers list
     * @throws IllegalArgumentException if availablePlayers is null or empty
     * @throws IllegalArgumentException if the selected index is invalid
     */
    public int promptTargetPlayer(List<Player> availablePlayers) {
        if (availablePlayers == null || availablePlayers.isEmpty()) {
            throw new IllegalArgumentException("Available players list cannot be null or empty");
        }
        
        // If there's only one player, validate input
        if (availablePlayers.size() == 1) {
            String input = userInput != null ? userInput : scanner.nextLine();
            try {
                int selection = Integer.parseInt(input);
                if (selection == 0) {
                    return 0;
                }
                throw new IllegalArgumentException("Invalid selection. Only player 0 is available.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Please enter a valid number.");
            }
        }
        
        // TODO: Implement multiple player selection
        throw new IllegalArgumentException("Not implemented for multiple players");
    }
}
