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
    private String userInput; // For testing purposes
    
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
        
        // Display all available players
        System.out.println("\nAvailable players:");
        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.printf("%d: %s%n", i, availablePlayers.get(i).getName());
        }
        System.out.print("Please select a player (enter number): ");
        
        String input = userInput != null ? userInput : scanner.nextLine();
        try {
            int selection = Integer.parseInt(input);
            if (selection >= 0 && selection < availablePlayers.size()) {
                return selection;
            }
            throw new IllegalArgumentException("Invalid selection. Please choose a player between 0 and " + (availablePlayers.size() - 1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }
}
