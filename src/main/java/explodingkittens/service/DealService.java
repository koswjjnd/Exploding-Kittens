package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.Card;
import explodingkittens.player.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InsufficientDefuseCardsException;
import java.util.List;
import java.util.Map;

public class DealService {
    private final DrawService drawService;
    
    public DealService() {
        this.drawService = new DrawService();
    }
    
    public DealService(DrawService drawService) {
        this.drawService = drawService;
    }
    
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
        
        // 给每个玩家发一张拆弹卡
        for (Player player : players) {
            Card defuseCard = new DefuseCard();
            player.receiveCard(defuseCard);
        }
    }
} 