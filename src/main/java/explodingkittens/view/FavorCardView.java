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
    
    /**
     * Creates a new FavorCardView with a Scanner for user input.
     */
    public FavorCardView() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Prompts the user to select a target player from the available players.
     * 
     * @param availablePlayers The list of players that can be selected
     * @return The index of the selected player in the availablePlayers list
     * @throws IllegalArgumentException if availablePlayers is null or empty
     */
    public int promptTargetPlayer(List<Player> availablePlayers) {
        if (availablePlayers == null || availablePlayers.isEmpty()) {
            throw new IllegalArgumentException("Available players list cannot be null or empty");
        }
        
        // If there's only one player, return their index
        if (availablePlayers.size() == 1) {
            return 0;
        }
        
        // TODO: Implement multiple player selection
        return -1;
    }
}
