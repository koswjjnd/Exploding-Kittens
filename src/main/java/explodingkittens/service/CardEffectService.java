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
        } 
        catch (CatCard.CatCardEffect effect) {
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
                    if (requestedCard != null) {
                        targetPlayer.removeCard(requestedCard);
                        currentPlayer.receiveCard(requestedCard);
                        view.displayCardRequested(currentPlayer, targetPlayer, requestedCard);
                    } 
                    else {
                        view.displayCardRequested(currentPlayer, targetPlayer, null);
                        view.showError("No card was selected.");
                    }
                } 
                else {
                    view.displayCardRequested(currentPlayer, targetPlayer, null);
                    view.showError("Target player does not have the requested card type.");
                }
            } 
            else {
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

    /**
     * Handles a card effect.
     * @param card The card to handle
     * @param turnOrder The current turn order
     * @param gameDeck The game deck
     */
    public void handleCardEffect(Card card, List<Player> turnOrder, Deck gameDeck) {
        if (card == null) {
            return;
        }
        try {
            card.effect(turnOrder, gameDeck);
        } 
        catch (CatCard.CatCardEffect e) {
            handleCatCardEffect(e, turnOrder);
        }
    }

    /**
     * Handles the effect of a cat card.
     * @param effect The cat card effect to handle
     * @param turnOrder The current turn order
     */
    private void handleCatCardEffect(CatCard.CatCardEffect effect, List<Player> turnOrder) {
        Player sourcePlayer = turnOrder.get(0);
        Player targetPlayer = turnOrder.stream()
            .filter(p -> p.getName().equals(effect.getTargetPlayerName()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Target player not found"));
        
        if (effect.getThirdCard() == null) {
            handleStealEffect(sourcePlayer, targetPlayer, effect);
        } 
        else {
            handleRequestEffect(sourcePlayer, targetPlayer, effect);
        }
    }

    /**
     * Handles the steal effect of a cat card.
     * @param sourcePlayer The player who played the cat card
     * @param targetPlayer The player who is being stolen from
     * @param effect The cat card effect
     */
    private void handleStealEffect(Player sourcePlayer, Player targetPlayer, 
            CatCard.CatCardEffect effect) {
        // Remove the two cat cards used for the effect
        sourcePlayer.removeCard(effect.getFirstCard());
        sourcePlayer.removeCard(effect.getSecondCard());
        
        // Steal the card from the target player
        Card stolenCard = effect.getTargetPlayerHand().get(effect.getTargetCardIndex());
        targetPlayer.removeCard(stolenCard);
        sourcePlayer.receiveCard(stolenCard);
        
        view.displayCardStolen(sourcePlayer, targetPlayer, stolenCard);
    }

    /**
     * Handles the request effect of a cat card.
     * @param sourcePlayer The player who played the cat card
     * @param targetPlayer The player who is being requested from
     * @param effect The cat card effect
     */
    private void handleRequestEffect(Player sourcePlayer, Player targetPlayer, 
            CatCard.CatCardEffect effect) {
        // Remove the three cat cards used for the effect
        sourcePlayer.removeCard(effect.getFirstCard());
        sourcePlayer.removeCard(effect.getSecondCard());
        sourcePlayer.removeCard(effect.getThirdCard());
        
        // Check if target player has the requested card type
        CardType requestedType = effect.getRequestedCardType();
        List<Card> targetHand = targetPlayer.getHand();
        for (int i = 0; i < targetHand.size(); i++) {
            Card card = targetHand.get(i);
            if (card.getType() == requestedType) {
                targetPlayer.removeCard(card);
                sourcePlayer.receiveCard(card);
                view.displayCardRequested(sourcePlayer, targetPlayer, card);
                return;
            }
        }
        
        // If we get here, the target player didn't have the requested card
        view.displayCardRequested(sourcePlayer, targetPlayer, null);
    }
}