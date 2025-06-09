package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CardType;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.model.SeeTheFutureCard;
import explodingkittens.view.SeeTheFutureView;
import explodingkittens.view.GameView;
import java.util.List;
import java.util.stream.Collectors;

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
            
            // 移除使用的猫卡
            currentPlayer.removeCard(effect.getFirstCard());
            currentPlayer.removeCard(effect.getSecondCard());
            
            if (effect.getThirdCard() != null) {
                // 处理三张猫牌请求卡牌的情况
                currentPlayer.removeCard(effect.getThirdCard());
                CardType requestedType = effect.getRequestedCardType();
                
                // 选择目标玩家
                Player targetPlayer = selectTargetPlayer(turnOrder, currentPlayer);
                view.displayCatCardEffect("request", currentPlayer, targetPlayer);
                
                // 检查目标玩家是否有请求的卡牌类型
                boolean hasRequestedCard = targetPlayer.getHand().stream()
                    .anyMatch(c -> c.getType() == requestedType);
                    
                if (hasRequestedCard) {
                    // 过滤出指定类型的卡牌
                    List<Card> matchingCards = targetPlayer.getHand().stream()
                        .filter(c -> c.getType() == requestedType)
                        .collect(Collectors.toList());
                    
                    // 让目标玩家选择一张指定类型的卡牌
                    Card requestedCard = view.selectCardFromPlayer(targetPlayer, matchingCards);
                    targetPlayer.removeCard(requestedCard);
                    currentPlayer.receiveCard(requestedCard);
                    view.displayCardRequested(currentPlayer, targetPlayer, requestedCard);
                } else {
                    view.showError("Target player does not have the requested card type.");
                }
            } else {
                // 处理两张猫牌偷牌的情况
                Player targetPlayer = turnOrder.stream()
                    .filter(p -> p.getName().equals(effect.getTargetPlayerName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Target player not found"));
                    
                view.displayCatCardEffect("steal", currentPlayer, targetPlayer);
                
                Card stolenCard = effect.getTargetPlayerHand().get(effect.getTargetCardIndex());
                targetPlayer.removeCard(stolenCard);
                currentPlayer.receiveCard(stolenCard);
                view.displayCardStolen(currentPlayer, targetPlayer, stolenCard);
            }
        }
    }

    private Player selectTargetPlayer(List<Player> turnOrder, Player currentPlayer) {
        List<Player> availablePlayers = turnOrder.stream()
            .filter(p -> p != currentPlayer && p.isAlive() && !p.getHand().isEmpty())
            .collect(Collectors.toList());
            
        if (availablePlayers.isEmpty()) {
            throw new IllegalStateException("No valid target players available");
        }
        
        return view.selectTargetPlayer(availablePlayers);
    }
}