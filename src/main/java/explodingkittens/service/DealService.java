package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.DefuseCard;
import explodingkittens.player.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InsufficientDefuseCardsException;
import java.util.List;
import java.util.Map;

public class DealService {
    
    /**
     * Deals defuse cards to players.
     * @param deck the deck to deal from
     * @param players the list of players to deal to
     * @throws InvalidDeckException if the deck is null
     * @throws EmptyDeckException if the deck is empty
     * @throws InsufficientDefuseCardsException if there are not enough defuse cards
     */
    public void dealDefuses(Deck deck, List<Player> players) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        if (deck.isEmpty()) {
            throw new EmptyDeckException();
        }

        // 检查拆弹卡数量
        Map<String, Integer> cardCounts = deck.getCardCounts();
        int defuseCardCount = cardCounts.getOrDefault("DefuseCard", 0);
        if (defuseCardCount < players.size()) {
            throw new InsufficientDefuseCardsException();
        }
        // TODO: Implement the rest of the method
    }
} 