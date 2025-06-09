package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.model.SeeTheFutureCard;
import explodingkittens.view.SeeTheFutureView;
import explodingkittens.view.GameView;
import java.util.List;

/**
 * Service class that handles the effects of different cards in the game.
 * This service is responsible for executing the effects of cards when they are played.
 */
public class CardEffectService {
    private final SeeTheFutureView seeTheFutureView = new SeeTheFutureView();
    private final GameView view;
    
    public CardEffectService(GameView view) {
        this.view = view;
    }
    
    /**
     * Execute the effect of a played card.
     *
     * @param card   the card being played
     * @param player the player who played it
     * @throws IllegalArgumentException if card or player is null
     * @throws IllegalStateException if game context is not properly initialized
     */
    public void applyEffect(Card card, Player player) {
        if (card == null || player == null) {
            throw new IllegalArgumentException("Card and player must not be null");
        }

        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck deck = GameContext.getGameDeck();

        if (turnOrder == null || deck == null) {
            throw new IllegalStateException("Game context not initialized properly");
        }

        // 注入 SeeTheFutureView
        if (card instanceof SeeTheFutureCard) {
            ((SeeTheFutureCard) card).setView(seeTheFutureView);
        }

        try {
            card.effect(turnOrder, deck);
        } catch (CatCard.CatCardEffect effect) {
            // 处理猫卡效果
            Player currentPlayer = turnOrder.get(0);
            
            // 移除使用的两张猫卡
            currentPlayer.removeCard(effect.getFirstCard());
            currentPlayer.removeCard(effect.getSecondCard());
            
            // 从目标玩家那里偷取卡牌
            Player targetPlayer = turnOrder.stream()
                .filter(p -> p.getName().equals(effect.getTargetPlayerName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Target player not found"));
                
            Card stolenCard = effect.getTargetPlayerHand().get(effect.getTargetCardIndex());
            targetPlayer.removeCard(stolenCard);
            currentPlayer.receiveCard(stolenCard);
            
            // 显示效果
            view.displayCardStolen(currentPlayer, targetPlayer, stolenCard);
        }
    }
}