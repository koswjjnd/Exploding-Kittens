package explodingkittens.model;

import java.util.List;
import java.util.Scanner;

/**
 * Implementation of CardStealInputHandler that uses console input.
 */
public class ConsoleCardStealInputHandler implements CardStealInputHandler {
    private final Scanner scanner;

    /**
     * Creates a new ConsoleCardStealInputHandler with the given scanner.
     * @param scanner The scanner to use for input
     * @throws IllegalArgumentException if scanner is null
     */
    public ConsoleCardStealInputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        this.scanner = scanner;
    }

    @Override
    public Player selectTargetPlayer(List<Player> availablePlayers) {
        if (availablePlayers == null || availablePlayers.isEmpty()) {
            throw new IllegalArgumentException("Available players list cannot be null or empty");
        }

        System.out.println("\nAvailable players:");
        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.println((i + 1) + ". " + availablePlayers.get(i).getName());
        }
        System.out.print("Select a player (1-" + availablePlayers.size() + "): ");
        
        int choice = scanner.nextInt();
        if (choice < 1 || choice > availablePlayers.size()) {
            throw new IllegalArgumentException(
                "Invalid choice. Please enter a number between 1 and " + availablePlayers.size());
        }
        
        return availablePlayers.get(choice - 1);
    }

    @Override
    public int selectCardIndex(int handSize) {
        if (handSize <= 0) {
            throw new IllegalArgumentException("Hand size must be positive");
        }

        System.out.print("\nSelect a card (1-" + handSize + "): ");
        
        int choice = scanner.nextInt();
        if (choice < 1 || choice > handSize) {
            throw new IllegalArgumentException(
                "Invalid choice. Please enter a number between 1 and " + handSize);
        }
        
        return choice - 1;
    }
} 