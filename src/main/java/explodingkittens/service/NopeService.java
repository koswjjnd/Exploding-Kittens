package explodingkittens.service;

import explodingkittens.model.BasicCard;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.Player;
import explodingkittens.controller.GameContext;
import explodingkittens.view.GameView;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling Nope card logic.
 */
public class NopeService {

    private final GameView view;

    public NopeService(GameView view) {
        this.view = view;
    }

    /**
     * Determines if a card's effect is negated by the players.
     * @param targetCard The card whose effect is being checked for negation
     * @return true if the card's effect is negated, false otherwise
     * @throws IllegalArgumentException if targetCard is null
     * @throws IllegalStateException if the current player is not found in the turn order
     */
    public boolean isNegatedByPlayers(Card targetCard) {
        if (targetCard == null) {
            throw new IllegalArgumentException("Target card cannot be null");
        }
        
        List<Card> playedNopeCards = new ArrayList<>();
        List<Player> allPlayers = GameContext.getTurnOrder();
        Player currentPlayer = GameContext.getCurrentPlayer();

        // First show the card being played to all players
        for (Player p : allPlayers) {
            if (!p.isAlive()) continue;
            view.showCardPlayed(currentPlayer, targetCard);
        }

        // Find the index of current player
        int currentIndex = allPlayers.indexOf(currentPlayer);
        if (currentIndex == -1) {
            throw new IllegalStateException("Current player not found in turn order");
        }

        // Start Nope chain
        boolean someonePlayedNope = true;
        while (someonePlayedNope) {
            someonePlayedNope = false;
            
            // Start from the next player after current player
            for (int i = 0; i < allPlayers.size(); i++) {
                // Calculate the actual index, starting from next player
                int actualIndex = (currentIndex + 1 + i) % allPlayers.size();
                Player p = allPlayers.get(actualIndex);
                
                if (!p.isAlive()) continue;
                
                if (p.hasCardOfType(CardType.NOPE)) {
                    boolean playNope = view.promptPlayNope(p, targetCard);
                    if (playNope) {
                        Card nope = p.removeCardOfType(CardType.NOPE);
                        if (nope != null) {
                            playedNopeCards.add(nope);
                            view.displayPlayedNope(p);
                            view.displayPlayerHand(p);
                            someonePlayedNope = true;
                        }
                    }
                }
            }
        }

        return isNegated(currentPlayer, playedNopeCards);
    }

    /**
     * Core logic: odd = negated, even = allowed.
     * @param player The player whose card effect is being checked
     * @param nopeCards The list of Nope cards played
     * @return true if the effect is negated (odd number of Nope cards), false otherwise
     * @throws IllegalArgumentException if player or nopeCards is null
     */
    public boolean isNegated(Player player, List<? extends Card> nopeCards) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (nopeCards == null) {
            throw new IllegalArgumentException("Nope cards cannot be null");
        }

        // Count all Nope cards, regardless of their specific type
        return nopeCards.size() % 2 != 0;
    }
}
