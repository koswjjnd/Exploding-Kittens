package explodingkittens.view;

import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.Deck;
import explodingkittens.model.BasicCard;
import explodingkittens.controller.GameContext;
import explodingkittens.util.I18nUtil;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ConsoleGameView implements GameView {
    @SuppressFBWarnings({"EI_EXPOSE_REP2", "MS_MUTABLE_COLLECTION"})
    private final Scanner scanner;
    private final HandView handView;

    public ConsoleGameView() {
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        this.handView = new HandView();
    }

    public ConsoleGameView(Scanner scanner) {
        this.scanner = scanner;
        this.handView = new HandView();
    }

    @Override
    public void displayCurrentPlayer(Player player) {
        System.out.println("\n" + I18nUtil.getMessage("player.current", player.getName()));
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
        System.out.println("\n" + I18nUtil.getMessage("action.choose"));
        System.out.println(I18nUtil.getMessage("player.action.draw"));
        System.out.println(I18nUtil.getMessage("player.action.play"));
        System.out.print(I18nUtil.getMessage("player.action.choice") + " ");
        return scanner.nextLine().trim().equals("1") ? "draw" : "play";
    }

    @Override
    public void displayPlayerEliminated(Player player) {
        System.out.println("\n" + I18nUtil.getMessage("player.eliminated", player.getName()));
    }

    @Override
    public void displayWinner(Player winner) {
        System.out.println("\n" + I18nUtil.getMessage("player.winner", winner.getName()));
    }

    @Override
    public void displayGameOver() {
        System.out.println("\n" + I18nUtil.getMessage("game.over"));
    }

    @Override
    public void displayDrawResult(Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + I18nUtil.getMessage("card.drawn", cardName));
    }

    @Override
    public int promptDefusePosition(int deckSize) {
        System.out.println("\n" + I18nUtil.getMessage("card.defuse.position", deckSize));
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    public Card promptPlayCard(Player player, List<Card> hand) {
        while (true) {
            handView.displayHandWithIndices(I18nUtil.getMessage("player.hand", player.getName()), hand);
            System.out.println("\n" + I18nUtil.getMessage("action.choose"));
            System.out.println(I18nUtil.getMessage("player.action.single"));
            System.out.println(I18nUtil.getMessage("player.action.combo"));
            System.out.println(I18nUtil.getMessage("player.action.winning"));
            System.out.println(I18nUtil.getMessage("player.action.end"));
            System.out.print(I18nUtil.getMessage("player.action.choice.range") + " ");
            
            try {
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
                else {
                    System.out.println(I18nUtil.getMessage("error.invalid.move"));
                }
            }
            catch (NumberFormatException e) {
                System.out.println(I18nUtil.getMessage("error.invalid.move"));
            }
        }
    }

    private Card handleSingleCardPlay(Player player, List<Card> hand) {
        System.out.println("\n" + I18nUtil.getMessage("card.select"));
        int cardChoice = Integer.parseInt(scanner.nextLine().trim());
        if (cardChoice == 0) {
            return null;
        }
        Card selectedCard = hand.get(cardChoice - 1);
        if (selectedCard.getType() == CardType.CAT_CARD) {
            showError(I18nUtil.getMessage("card.combo.single.error"));
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
            showError(I18nUtil.getMessage("card.combo.invalid"));
            return promptPlayCard(player, hand);
        }
        
        // check if selected cards are all the same type of cat cards
        Card firstCard = hand.get(selectedIndices.get(0));
        if (!(firstCard instanceof CatCard)) {
            showError(I18nUtil.getMessage("card.combo.must.be.cat"));
            return promptPlayCard(player, hand);
        }
        
        CatType catType = ((CatCard) firstCard).getCatType();
        for (int i = 1; i < selectedIndices.size(); i++) {
            Card card = hand.get(selectedIndices.get(i));
            if (!(card instanceof CatCard) || ((CatCard) card).getCatType() != catType) {
                showError(I18nUtil.getMessage("card.combo.same.type"));
                return promptPlayCard(player, hand);
            }
        }
        
        return firstCard;
    }

    private Card handleRequestCombo(Player player, List<Card> hand) {
        List<Integer> selectedIndices = promptCatCardSelection(3, hand);
        if (selectedIndices == null || selectedIndices.size() != 3) {
            showError(I18nUtil.getMessage("card.combo.invalid"));
            return promptPlayCard(player, hand);
        }
        
        // check if selected cards are all the same type of cat cards
        Card firstCard = hand.get(selectedIndices.get(0));
        if (!(firstCard instanceof CatCard)) {
            showError(I18nUtil.getMessage("card.combo.must.be.cat"));
            return promptPlayCard(player, hand);
        }
        
        CatType catType = ((CatCard) firstCard).getCatType();
        for (int i = 1; i < selectedIndices.size(); i++) {
            Card card = hand.get(selectedIndices.get(i));
            if (!(card instanceof CatCard) || ((CatCard) card).getCatType() != catType) {
                showError(I18nUtil.getMessage("card.combo.same.type"));
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
            showError(I18nUtil.getMessage("card.combo.invalid"));
            return false;
        }

        // 验证选择的卡牌是否都是相同类型的猫牌
        Card firstCard = hand.get(selectedIndices.get(0));
        if (!(firstCard instanceof CatCard)) {
            showError(I18nUtil.getMessage("card.combo.must.be.cat"));
            return false;
        }

        CatType catType = ((CatCard) firstCard).getCatType();
        for (int i = 1; i < selectedIndices.size(); i++) {
            Card card = hand.get(selectedIndices.get(i));
            if (!(card instanceof CatCard) || ((CatCard) card).getCatType() != catType) {
                showError(I18nUtil.getMessage("card.combo.same.type"));
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
        System.out.println("\n" + I18nUtil.getMessage("player.played", player.getName(), cardName));
    }

    @Override
    public Card selectCardToPlay(Player player, List<Card> hand) {
        return promptPlayCard(player, hand);
    }

    @Override
    public void showError(String message) {
        System.out.println("\n" + I18nUtil.getMessage("error.prefix", message));
    }

    @Override
    public void showCardPlayed(Player player, Card card) {
        displayPlayedCard(player, card);
    }

    @Override
    public boolean checkForNope(Player player, Card card) {
        System.out.println("\n" + I18nUtil.getMessage("nope.prompt"));
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public void showCardNoped(Player player, Card card) {
        System.out.println("\n" + I18nUtil.getMessage("nope.card.noped", player.getName(), card));
    }

    @Override
    public void showCardDrawn(Player player, Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + I18nUtil.getMessage("player.drew", player.getName(), cardName));
    }

    @Override
    public boolean confirmDefuse(Player player) {
        System.out.println("\n" + I18nUtil.getMessage("defuse.confirm", player.getName()));
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public int selectExplodingKittenPosition(int deckSize) {
        System.out.println("\n" + I18nUtil.getMessage("card.defuse.position", deckSize));
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    public void displayDefuseUsed(Player player) {
        System.out.println("\n" + I18nUtil.getMessage("defuse.used", player.getName()));
    }

    @Override
    public void displayDefuseSuccess(Player player, int position) {
        System.out.println("\n" + I18nUtil.getMessage("defuse.success", 
            player.getName(), position));
    }

    @Override
    public boolean promptPlayNope(Player player, Card card) {
        System.out.println("\n" + I18nUtil.getMessage("nope.player.prompt", player.getName()));
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    @Override
    public void displayPlayedNope(Player player) {
        System.out.println("\n" + I18nUtil.getMessage("nope.played", player.getName()));
    }

    @Override
    public void showCurrentPlayerTurn(Player player) {
        System.out.println("\n" + I18nUtil.getMessage("player.turn", player.getName()));
    }

    @Override
    public Player selectTargetPlayer(List<Player> availablePlayers) {
        System.out.println(I18nUtil.getMessage("player.available"));
        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.println((i + 1) + ". " + availablePlayers.get(i).getName());
        }
        String message = I18nUtil.getMessage("player.select", availablePlayers.size());
        System.out.print(message + " ");
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
                if (card instanceof CatCard 
                    && ((CatCard) card).getCatType() == requestedCatType) {
                    filteredHand.add(card);
                }
            }
            
            if (filteredHand.isEmpty()) {
                System.out.println("\n" + I18nUtil.getMessage("player.no.cards", 
                    requestedCatType));
                return null;
            }

            System.out.println("\n" 
                + I18nUtil.getMessage("player.select.specific", requestedCatType));
            for (int i = 0; i < filteredHand.size(); i++) {
                CatCard catCard = (CatCard) filteredHand.get(i);
                System.out.println((i + 1) + ". " + catCard.getCatType());
            }
            System.out.print(I18nUtil.getMessage("card.combo.choice") + " ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > filteredHand.size()) {
                return null;
            }
            return filteredHand.get(choice - 1);
        }

        // 对于非猫牌，显示所有卡牌
        handView.displayHandWithIndices(targetPlayer.getName(), hand);
        System.out.print(I18nUtil.getMessage("player.select.card", hand.size()) + " ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        return hand.get(choice - 1);
    }

    @Override
    public void displayCatCardEffect(String effectType, Player sourcePlayer, 
            Player targetPlayer) {
        if (effectType.equals("steal")) {
            System.out.println("\n" + I18nUtil.getMessage("card.effect.stealing", 
                sourcePlayer.getName(), targetPlayer.getName()));
        } 
        else if (effectType.equals("request")) {
            System.out.println("\n" + I18nUtil.getMessage("card.effect.requesting", 
                sourcePlayer.getName(), targetPlayer.getName()));
        }
    }

    @Override
    public void displayCardStolen(Player sourcePlayer, Player targetPlayer, Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + I18nUtil.getMessage("player.stole", 
            sourcePlayer.getName(), cardName, targetPlayer.getName()));
    }

    @Override
    public void displayCardRequested(Player sourcePlayer, Player targetPlayer, Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + I18nUtil.getMessage("player.received", 
            sourcePlayer.getName(), cardName, targetPlayer.getName()));
    }

    @Override
    public void displayCardDrawnFromBottom(Card card) {
        String cardName = card instanceof CatCard ? 
            ((CatCard) card).getCatType().toString() : 
            card.getType().toString();
        System.out.println("\n" + I18nUtil.getMessage("card.drawn.from.bottom", cardName));
    }

    private int promptComboType() {
        System.out.println("\n" + I18nUtil.getMessage("card.combo.type"));
        System.out.println(I18nUtil.getMessage("card.combo.steal"));
        System.out.println(I18nUtil.getMessage("card.combo.request"));
        System.out.println(I18nUtil.getMessage("card.combo.cancel"));
        System.out.print(I18nUtil.getMessage("card.combo.choice") + " ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private List<Integer> promptCatCardSelection(int count, List<Card> hand) {
        System.out.println("\n" + I18nUtil.getMessage("card.combo.select", count));
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
        System.out.println("\n" + I18nUtil.getMessage("card.request.type"));
        System.out.println(I18nUtil.getMessage("cardtype.attack"));
        System.out.println(I18nUtil.getMessage("cardtype.skip"));
        System.out.println(I18nUtil.getMessage("cardtype.super.skip"));
        System.out.println(I18nUtil.getMessage("cardtype.double.skip"));
        System.out.println(I18nUtil.getMessage("cardtype.favor"));
        System.out.println(I18nUtil.getMessage("cardtype.shuffle"));
        System.out.println(I18nUtil.getMessage("cardtype.see.future"));
        System.out.println(I18nUtil.getMessage("cardtype.nope"));
        System.out.println(I18nUtil.getMessage("cardtype.switch.deck"));
        System.out.println(I18nUtil.getMessage("cardtype.draw.bottom"));
        System.out.println(I18nUtil.getMessage("cardtype.snatch"));
        System.out.println(I18nUtil.getMessage("cardtype.reverse"));
        System.out.println(I18nUtil.getMessage("cardtype.time.rewind"));
        System.out.println(I18nUtil.getMessage("cardtype.taco.cat"));
        System.out.println(I18nUtil.getMessage("cardtype.beard.cat"));
        System.out.println(I18nUtil.getMessage("cardtype.cattermelon"));
        System.out.println(I18nUtil.getMessage("cardtype.rainbow.cat"));
        System.out.println(I18nUtil.getMessage("cardtype.hairy.potato.cat"));
        System.out.println(I18nUtil.getMessage("cardtype.watermelon.cat"));
        System.out.println(I18nUtil.getMessage("cardtype.feral.cat"));
        System.out.println(I18nUtil.getMessage("cardtype.defuse"));
        System.out.println(I18nUtil.getMessage("cardtype.cancel"));
        System.out.print(I18nUtil.getMessage("cardtype.choice") + " ");
        
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