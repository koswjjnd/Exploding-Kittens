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
     */
    public boolean isNegatedByPlayers(Card targetCard) {
        List<Card> playedNopeCards = new ArrayList<>();

        for (Player p : GameContext.getTurnOrder()) {
            if (!p.isAlive()) continue;

            if (p.hasCardOfType(CardType.NOPE)) {
                boolean playNope = view.promptPlayNope(p, targetCard);
                if (playNope) {
                    Card nope = p.removeCardOfType(CardType.NOPE);
                    if (nope != null) {
                        playedNopeCards.add(nope);
                        view.displayPlayedNope(p);
                        view.displayPlayerHand(p);
                    }
                }
            }
        }

        return isNegated(GameContext.getCurrentPlayer(), playedNopeCards);
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

        int count = 0;
        for (Card card : nopeCards) {
            if (card instanceof BasicCard && ((BasicCard) card).getType() == CardType.NOPE) {
                count++;
            }
        }
        return count % 2 != 0;
    }
}
