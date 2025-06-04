package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.CatType;
import explodingkittens.model.CatCard;
import java.util.List;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Console-based implementation of CatCardStealInputHandler.
 */
public final class ConsoleCatCardStealInputHandler implements CatCardStealInputHandler {
    private final Scanner scanner;

    /**
     * Creates a new ConsoleCatCardStealInputHandler with the given scanner.
     * @param scanner The scanner to use for input
     * @throws IllegalArgumentException if scanner is null
     */
    public ConsoleCatCardStealInputHandler(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        this.scanner = scanner;
    }

    @Override
    public Player selectTargetPlayer(List<Player> availablePlayers) {
        System.out.println("Available players:");
        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.println((i + 1) + ". " + availablePlayers.get(i).getName());
        }

        while (true) {
            System.out.print("Select a player (1-" + availablePlayers.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= availablePlayers.size()) {
                    return availablePlayers.get(choice - 1);
                }
            } 
            catch (NumberFormatException e) {
                // Invalid input, try again
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    @Override
    public int selectCardIndex(int handSize) {
        while (true) {
            System.out.print("Select a card (1-" + handSize + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= handSize) {
                    return choice - 1;
                }
            } 
            catch (NumberFormatException e) {
                // Invalid input, try again
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    @Override
    public void handleCardSteal(Player currentPlayer, List<Player> turnOrder, CatType catType) {
        // Set this handler as the input handler for CatCard
        CatCard.setInputHandler(this);
        
        // Create and use the controller
        CatCardStealController controller = new CatCardStealController(this);
        controller.handleCardSteal(currentPlayer, turnOrder, catType);
    }
} 