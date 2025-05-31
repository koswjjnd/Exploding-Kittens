package explodingkittens.controller;

import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.model.Player;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller for handling cat card stealing between players.
 */
public class CatCardStealController {
    private final CatCardStealInputHandler inputHandler;

    /**
     * Creates a new CatCardStealController with the given input handler.
     * @param inputHandler The input handler to use
     */
    public CatCardStealController(CatCardStealInputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /**
     * Handles a card steal from one player to another.
     * @param currentPlayer The player making the steal
     * @param availablePlayers The list of available target players
     * @param catType The type of cat card being used for the steal
     * @throws IllegalStateException if the target player has no cards
     */
    public void handleCardSteal(Player currentPlayer, List<Player> availablePlayers, 
            CatType catType) {
        try {
            // Validate target player
            Player targetPlayer = inputHandler.selectTargetPlayer(availablePlayers);
            if (targetPlayer == null || !availablePlayers.contains(targetPlayer)) {
                throw new IllegalArgumentException("Invalid target player selection");
            }
            if (targetPlayer.getHand().isEmpty()) {
                throw new IllegalStateException("Target player has no cards");
            }

            // Set the input handler for CatCard
            CatCard.setInputHandler(inputHandler);
            
            // Create a cat card to trigger the effect
            CatCard catCard = new CatCard(catType);
            catCard.effect(availablePlayers, null);
        } 
        catch (CatCard.CatCardEffect effect) {
            // Remove the two cat cards used for the effect
            currentPlayer.removeCard(effect.getFirstCard());
            currentPlayer.removeCard(effect.getSecondCard());
            
            // Steal the card from the target player
            Card stolenCard = effect.getTargetPlayer().getHand().get(effect.getTargetCardIndex());
            effect.getTargetPlayer().removeCard(stolenCard);
            currentPlayer.receiveCard(stolenCard);
        }
    }

    private void removeTwoCatCards(Player currentPlayer, CatType catType) {
        List<Card> cardsToRemove = new ArrayList<>();
        List<Card> handCopy = new ArrayList<>(currentPlayer.getHand());
        for (Card card : handCopy) {
            if (card instanceof CatCard && ((CatCard) card).getCatType() == catType) {
                cardsToRemove.add(card);
                if (cardsToRemove.size() == 2) {
                    break;
                }
            }
        }

        if (cardsToRemove.size() != 2) {
            throw new IllegalStateException("Must have exactly 2 cat cards of the same type");
        }

        for (Card card : cardsToRemove) {
            currentPlayer.removeCard(card);
        }
    }

    private void stealCardFromTarget(Player currentPlayer, Player targetPlayer) {
        int cardIndex = inputHandler.selectCardIndex(targetPlayer.getHand().size());
        Card stolenCard = targetPlayer.getHand().get(cardIndex);
        targetPlayer.removeCard(stolenCard);
        currentPlayer.receiveCard(stolenCard);
    }
} 