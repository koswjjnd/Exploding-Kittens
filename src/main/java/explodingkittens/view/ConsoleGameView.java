package explodingkittens.view;

import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.controller.GameContext;
import java.util.List;
import java.util.Scanner;

public class ConsoleGameView implements GameView {
    private final Scanner scanner;

    public ConsoleGameView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayCurrentPlayer(Player player) {
        System.out.println("\nCurrent player: " + player.getName());
    }

    @Override
    public void displayPlayerHand(Player player) {
        System.out.println("\nYour hand:");
        List<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i));
        }
    }

    @Override
    public String promptPlayerAction(Player player) {
        System.out.println("\nChoose an action:");
        System.out.println("1. Draw a card");
        System.out.println("2. Play a card");
        System.out.print("Choice (1/2): ");
        return scanner.nextLine().trim().equals("1") ? "draw" : "play";
    }

    @Override
    public void displayPlayerEliminated(Player player) {
        System.out.println("\n" + player.getName() + " has been eliminated!");
    }

    @Override
    public void displayWinner(Player winner) {
        System.out.println("\nGame Over! The winner is: " + winner.getName());
    }

    @Override
    public void displayGameOver() {
        System.out.println("\nGame Over!");
    }

    @Override
    public void displayDrawResult(Card card) {
        System.out.println("\nDrawn card: " + card);
    }

    @Override
    public int promptDefusePosition(int deckSize) {
        System.out.println("\nChoose a position to place the Exploding Kitten " 
            + "(0-" + (deckSize - 1) + "): ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    public Card promptPlayCard(List<Card> hand) {
        System.out.println("\nChoose a card to play "
            + "(enter number, or 0 to end turn): ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        return choice == 0 ? null : hand.get(choice - 1);
    }

    @Override
    public void displayPlayedCard(Player player, Card card) {
        System.out.println("\n" + player.getName() + " played " + card);
    }

    @Override
    public Card selectCardToPlay(Player player, List<Card> hand) {
        return promptPlayCard(hand);
    }

    @Override
    public void showError(String message) {
        System.out.println("\nError: " + message);
    }

    @Override
    public void showCardPlayed(Player player, Card card) {
        displayPlayedCard(player, card);
    }

    @Override
    public boolean checkForNope(Player player, Card card) {
        System.out.println("\nDoes any player want to play a Nope card? (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public void showCardNoped(Player player, Card card) {
        System.out.println("\n" + player.getName() + "'s " + card + " was Noped!");
    }

    @Override
    public void showCardDrawn(Player player, Card card) {
        System.out.println("\n" + player.getName() + " drew " + card);
    }

    @Override
    public boolean confirmDefuse(Player player) {
        System.out.println("\n" + player.getName() + ", do you want to use your Defuse card?" 
            + "(y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public int selectExplodingKittenPosition(int deckSize) {
        System.out.println("\nChoose a position to place the Exploding Kitten "
            + "(0-" + (deckSize - 1) + "): ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    public void displayDefuseUsed(Player player) {
        System.out.println("\n" + player.getName() + " used a Defuse card!");
    }

    @Override
    public void displayDefuseSuccess(Player player, int position) {
        System.out.println("\n" + player.getName() 
            + " successfully placed the Exploding Kitten at position " 
            + position);
    }

    @Override
    public boolean promptPlayNope(Player player, Card card) {
        System.out.println("\n" + player.getName() + ", do you want to play a Nope card? (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public void displayPlayedNope(Player player) {
        System.out.println("\n" + player.getName() + " played a Nope card!");
    }
} 