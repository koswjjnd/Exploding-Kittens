package explodingkittens.view;

import explodingkittens.model.Card;
import explodingkittens.model.Player;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;

/**
 * View class for handling Favor card interactions.
 * Responsible for displaying available players and getting user input.
 * When a Favor card is played, the current player selects a target player,
 * and the target player must choose a card from their hand to give to the current player.
 */
public class FavorCardView {
    private final Scanner scanner;
    private String userInput; // For testing purposes
    
    /**
     * Creates a new FavorCardView with a Scanner for user input.
     */
    public FavorCardView() {
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
    }
    
    /**
     * Sets the user input for testing purposes.
     * @param input The input to simulate
     */
    public void setUserInput(String input) {
        this.userInput = input;
    }
    
    /**
     * Prompts the current player to select a target player from the available players.
     * The target player will be asked to give a card to the current player.
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
        System.out.println("\nAvailable players to ask for a favor:");
        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.printf("%d: %s%n", i, availablePlayers.get(i).getName());
        }
        System.out.print("Please select a player to ask for a favor (enter number): ");
        
        String input = userInput != null ? userInput : scanner.nextLine();
        try {
            int selection = Integer.parseInt(input);
            if (selection >= 0 && selection < availablePlayers.size()) {
                return selection;
            }
            throw new IllegalArgumentException(
                "Invalid selection. Please choose a player between 0 and "
                + (availablePlayers.size() - 1));
        } 
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }

    /**
     * Prompts the target player to choose a card from their hand to give to the current player.
     * 
     * @param cards The list of cards in the target player's hand
     * @return The index of the selected card in the cards list
     * @throws IllegalArgumentException if cards is null or empty
     * @throws IllegalArgumentException if the selected index is invalid
     */
    public int promptCardSelection(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Cards list cannot be null or empty");
        }
        
        // Display all available cards
        System.out.println("\nChoose a card to give to the current player:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("%d: %s%n", i, cards.get(i).getType());
        }
        System.out.print("Please select a card to give (enter number): ");
        
        String input = userInput != null ? userInput : scanner.nextLine();
        try {
            int selection = Integer.parseInt(input);
            if (selection >= 0 && selection < cards.size()) {
                return selection;
            }
            throw new IllegalArgumentException(
                "Invalid selection. Please choose a card between 0 and "
                + (cards.size() - 1));
        } 
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }
}
