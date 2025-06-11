package explodingkittens.view;

import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.Deck;
import explodingkittens.model.BasicCard;
import explodingkittens.controller.GameContext;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ConsoleGameView implements GameView {
    private final Scanner scanner;
    private final HandView handView;

    public ConsoleGameView() {
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        this.handView = new HandView();
    }

    @Override
    public void displayCurrentPlayer(Player player) {
        System.out.println("\nCurrent player: " + player.getName());
    }

    @Override
    public void displayPlayerHand(Player player) {
        handView.displayHandWithIndices(player.getName(), player.getHand());
    }

    @Override
    public void displayOtherPlayerHand(Player player) {
        handView.displayHandWithoutIndices(player.getName(), player.getHand());
    }

    @Override
    public void displayHandForSelection(Player player, List<Card> hand) {
        handView.displayHandWithIndices(player.getName(), hand);
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
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\nDrawn card: " + cardName);
    }

    @Override
    public int promptDefusePosition(int deckSize) {
        System.out.println("\nChoose a position to place the Exploding Kitten " 
            + "(0-" + (deckSize) + "): ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    public Card promptPlayCard(Player player, List<Card> hand) {
        handView.displayHandWithIndices("Player: " + player.getName(), hand);
        System.out.println("\nChoose an action:");
        System.out.println("1. Play a single card (non-cat card)");
        System.out.println("2. Play a cat card combo");
        System.out.println("3. Play winning combo (5 same cat cards)");
        System.out.println("0. End turn");
        System.out.print("Choice (0-3): ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        
        if (choice == 0) {
            return null;
        } 
        else if (choice == 1) {
            return handleSingleCardPlay(player, hand);
        } 
        else if (choice == 2) {
            return handleCatCardCombo(player, hand);
        }
        else if (choice == 3) {
            return handleWinningComboOption(player, hand);
        }
        return null;
    }

    private Card handleSingleCardPlay(Player player, List<Card> hand) {
        System.out.println("\nChoose a card to play (enter number): ");
        int cardChoice = Integer.parseInt(scanner.nextLine().trim());
        if (cardChoice == 0) {
            return null;
        }
        Card selectedCard = hand.get(cardChoice - 1);
        if (selectedCard.getType() == CardType.CAT_CARD) {
            showError("Cannot play a single cat card. Use combo option instead.");
            return promptPlayCard(player, hand);
        }
        return selectedCard;
    }

    private Card handleCatCardCombo(Player player, List<Card> hand) {
        int comboType = promptComboType();
        if (comboType == 0) {
            return promptPlayCard(player, hand);
        }
        
        if (comboType == 1) {
            return handleStealCombo(player, hand);
        } 
        else {
            return handleRequestCombo(player, hand);
        }
        
    }

    private Card handleStealCombo(Player player, List<Card> hand) {
        List<Integer> selectedIndices = promptCatCardSelection(2, hand);
        if (selectedIndices == null || selectedIndices.size() != 2) {
            showError("Invalid selection. Please try again.");
            return promptPlayCard(player, hand);
        }
        
        // check if selected cards are all the same type of cat cards
        Card firstCard = hand.get(selectedIndices.get(0));
        if (!(firstCard instanceof CatCard)) {
            showError("Selected cards must be cat cards.");
            return promptPlayCard(player, hand);
        }
        
        CatType catType = ((CatCard) firstCard).getCatType();
        for (int i = 1; i < selectedIndices.size(); i++) {
            Card card = hand.get(selectedIndices.get(i));
            if (!(card instanceof CatCard) || ((CatCard) card).getCatType() != catType) {
                showError("All selected cards must be the same type of cat card.");
                return promptPlayCard(player, hand);
            }
        }
        
        return firstCard;
    }

    private Card handleRequestCombo(Player player, List<Card> hand) {
        List<Integer> selectedIndices = promptCatCardSelection(3, hand);
        if (selectedIndices == null || selectedIndices.size() != 3) {
            showError("Invalid selection. Please try again.");
            return promptPlayCard(player, hand);
        }
        
        // check if selected cards are all the same type of cat cards
        Card firstCard = hand.get(selectedIndices.get(0));
        if (!(firstCard instanceof CatCard)) {
            showError("Selected cards must be cat cards.");
            return promptPlayCard(player, hand);
        }
        
        CatType catType = ((CatCard) firstCard).getCatType();
        for (int i = 1; i < selectedIndices.size(); i++) {
            Card card = hand.get(selectedIndices.get(i));
            if (!(card instanceof CatCard) || ((CatCard) card).getCatType() != catType) {
                showError("All selected cards must be the same type of cat card.");
                return promptPlayCard(player, hand);
            }
        }

        // select card type to request
        CardType requestedCardType = promptRequestedCardType();
        if (requestedCardType == null) {
            return promptPlayCard(player, hand);
        }

        // create a CatCard object to trigger request effect
        return new CatCard(catType) {
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {
                CatCard firstCatCard = (CatCard)hand.get(selectedIndices.get(0));
                CatCard secondCatCard = (CatCard)hand.get(selectedIndices.get(1));
                CatCard thirdCatCard = (CatCard)hand.get(selectedIndices.get(2));
                
                throw new CatCard.CatCardEffect(
                    firstCatCard,
                    secondCatCard,
                    thirdCatCard,
                    turnOrder.get(0).getName(),
                    requestedCardType
                );
            }

            @Override
            public String toString() {
                return "Request Card (" + requestedCardType + ")";
            }
        };
    }

    @Override
    public boolean handleWinningCombo(Player player, List<Card> hand) {
        List<Integer> selectedIndices = promptCatCardSelection(5, hand);
        if (selectedIndices == null || selectedIndices.size() != 5) {
            showError("Invalid selection. Please try again.");
            return false;
        }

        // 验证选择的卡牌是否都是相同类型的猫牌
        Card firstCard = hand.get(selectedIndices.get(0));
        if (!(firstCard instanceof CatCard)) {
            showError("Selected cards must be cat cards.");
            return false;
        }

        CatType catType = ((CatCard) firstCard).getCatType();
        for (int i = 1; i < selectedIndices.size(); i++) {
            Card card = hand.get(selectedIndices.get(i));
            if (!(card instanceof CatCard) || ((CatCard) card).getCatType() != catType) {
                showError("All selected cards must be the same type of cat card.");
                return false;
            }
        }
        

        // 将其他玩家全部设为淘汰，这样GameController.start()主循环会自动调用view.displayWinner(winner)并结束游戏
        List<Player> turnOrder = GameContext.getTurnOrder();
        for (Player p : turnOrder) {
            if (p != player) {
                p.setAlive(false);
            }
        }
        return true;
    }

    @Override
    public void displayPlayedCard(Player player, Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + player.getName() + " played " + cardName);
    }

    @Override
    public Card selectCardToPlay(Player player, List<Card> hand) {
        return promptPlayCard(player, hand);
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
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + player.getName() 
            + " drew " + cardName);
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
            + "(0-" + (deckSize) + "): ");
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
        System.out.println("\n" + player.getName() 
            + ", do you want to play a Nope card? (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public void displayPlayedNope(Player player) {
        System.out.println("\n" + player.getName() + " played a Nope card!");
    }

    @Override
    public void showCurrentPlayerTurn(Player player) {
        System.out.println("\nIt's " + player.getName() + "'s turn.");
    }

    @Override
    public Player selectTargetPlayer(List<Player> availablePlayers) {
        System.out.println("Available players:");
        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.println((i + 1) + ". " + availablePlayers.get(i).getName());
        }
        System.out.print("Select a player (1-" + availablePlayers.size() + "): ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        return availablePlayers.get(choice - 1);
    }

    @Override
    public Card selectCardFromPlayer(Player targetPlayer, List<Card> hand) {
        // get requested card type
        CardType requestedType = null;
        CatType requestedCatType = null;
        for (Card card : hand) {
            if (card instanceof CatCard) {
                requestedType = card.getType();
                requestedCatType = ((CatCard) card).getCatType();
                break;
            }
        }

        // if requested is cat card, only show cat cards of that type
        if (requestedType == CardType.CAT_CARD && requestedCatType != null) {
            List<Card> filteredHand = new ArrayList<>();
            for (Card card : hand) {
                if (card instanceof CatCard && ((CatCard) card).getCatType() == requestedCatType) {
                    filteredHand.add(card);
                }
            }
            
            if (filteredHand.isEmpty()) {
                System.out.println("\nNo " + requestedCatType + " cards available.");
                return null;
            }

            System.out.println("\nSelect a " + requestedCatType + " card:");
            for (int i = 0; i < filteredHand.size(); i++) {
                CatCard catCard = (CatCard) filteredHand.get(i);
                System.out.println((i + 1) + ". " + catCard.getCatType());
            }
            System.out.print("Choice (1-" + filteredHand.size() + "): ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > filteredHand.size()) {
                return null;
            }
            return filteredHand.get(choice - 1);
        }

        // 对于非猫牌，显示所有卡牌
        handView.displayHandWithIndices(targetPlayer.getName(), hand);
        System.out.print("Select a card (1-" + hand.size() + "): ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        return hand.get(choice - 1);
    }

    @Override
    public void displayCatCardEffect(String effectType, Player sourcePlayer, Player targetPlayer) {
        if (effectType.equals("steal")) {
            System.out.println("\n" + sourcePlayer.getName() 
                + " is stealing a card from " 
                + targetPlayer.getName());
        } 
        else if (effectType.equals("request")) {
            System.out.println("\n" + sourcePlayer.getName() 
                + " is requesting a card from " 
                + targetPlayer.getName());
        }
    }

    @Override
    public void displayCardStolen(Player sourcePlayer, Player targetPlayer, Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + sourcePlayer.getName() 
            + " stole " + cardName 
            + " from " + targetPlayer.getName());
    }

    @Override
    public void displayCardRequested(Player sourcePlayer, Player targetPlayer, Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + sourcePlayer.getName() 
            + " received " + cardName 
            + " from " + targetPlayer.getName());
    }

    @Override
    public void displayCardDrawnFromBottom(Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\nCard drawn from bottom: " + cardName);
    }

    private int promptComboType() {
        System.out.println("\nChoose combo type:");
        System.out.println("1. Steal a card (2 same cat cards)");
        System.out.println("2. Request a card (3 same cat cards)");
        System.out.println("0. Cancel");
        System.out.print("Choice (0-2): ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private List<Integer> promptCatCardSelection(int count, List<Card> hand) {
        System.out.println("\nSelect " + count 
            + " same cat cards (enter numbers, separated by space): ");
        String[] cardChoices = scanner.nextLine().trim().split(" ");
        if (cardChoices.length != count) {
            return null;
        }
        
        List<Integer> selectedIndices = new ArrayList<>();
        try {
            for (String choice : cardChoices) {
                int index = Integer.parseInt(choice) - 1;
                if (index < 0 || index >= hand.size()) {
                    return null;
                }
                selectedIndices.add(index);
            }
            return selectedIndices;
        } 
        catch (NumberFormatException e) {
            return null;
        }
    }

    private CardType promptRequestedCardType() {
        System.out.println("\nChoose card type to request:");
        System.out.println("1. Attack");
        System.out.println("2. Skip");
        System.out.println("3. Super Skip");
        System.out.println("4. Double Skip");
        System.out.println("5. Favor");
        System.out.println("6. Shuffle");
        System.out.println("7. See the Future");
        System.out.println("8. Nope");
        System.out.println("9. Switch Deck by Half");
        System.out.println("10. Draw from Bottom");
        System.out.println("11. Snatch");
        System.out.println("12. Reverse");
        System.out.println("13. Time Rewind");
        System.out.println("14. Taco Cat");
        System.out.println("15. Beard Cat");
        System.out.println("16. Cattermelon");
        System.out.println("17. Rainbow Cat");
        System.out.println("18. Hairy Potato Cat");
        System.out.println("19. Watermelon Cat");
        System.out.println("20. Feral Cat");
        System.out.println("21. Defuse");
        System.out.println("0. Cancel");
        System.out.print("Choice (0-21): ");
        
        int choice = Integer.parseInt(scanner.nextLine().trim());
        switch (choice) {
            case 1: return CardType.ATTACK;
            case 2: return CardType.SKIP;
            case 3: return CardType.SUPER_SKIP;
            case 4: return CardType.DOUBLE_SKIP;
            case 5: return CardType.FAVOR;
            case 6: return CardType.SHUFFLE;
            case 7: return CardType.SEE_THE_FUTURE;
            case 8: return CardType.NOPE;
            case 9: return CardType.SWITCH_DECK_BY_HALF;
            case 10: return CardType.DRAW_FROM_BOTTOM;
            case 11: return CardType.SNATCH;
            case 12: return CardType.REVERSE;
            case 13: return CardType.TIME_REWIND;
            case 14: return CardType.CAT_CARD; // Taco Cat
            case 15: return CardType.CAT_CARD; // Beard Cat
            case 16: return CardType.CAT_CARD; // Cattermelon
            case 17: return CardType.CAT_CARD; // Rainbow Cat
            case 18: return CardType.CAT_CARD; // Hairy Potato Cat
            case 19: return CardType.CAT_CARD; // Watermelon Cat
            case 20: return CardType.CAT_CARD; // Feral Cat
            case 21: return CardType.DEFUSE;
            case 0: return null;
            default: return null;
        }
    }

    private Card handleWinningComboOption(Player player, List<Card> hand) {
        if (handleWinningCombo(player, hand)) {
            return null;  // 直接返回null，结束回合
        }
        return promptPlayCard(player, hand);  // 如果失败，重新提示选择
    }
} 