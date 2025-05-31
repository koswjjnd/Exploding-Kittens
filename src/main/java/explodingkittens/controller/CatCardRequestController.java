package explodingkittens.controller;

import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatRequestCard;
import explodingkittens.model.CatType;
import explodingkittens.model.Player;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller for handling cat card requests between players.
 */
public class CatCardRequestController {
    private final CatCardRequestInputHandler inputHandler;

    /**
     * Creates a new CatCardRequestController with the given input handler.
     * @param inputHandler The input handler to use
     * @throws NullPointerException if inputHandler is null
     */
    public CatCardRequestController(CatCardRequestInputHandler inputHandler) {
        if (inputHandler == null) {
            throw new NullPointerException("Input handler cannot be null");
        }
        this.inputHandler = inputHandler;
    }

    /**
     * Handles a card request from one player to another.
     * @param currentPlayer The player making the request
     * @param availablePlayers The list of available target players
     * @param catType The type of cat card being used for the request
     * @throws IllegalStateException if the target player has no cards
     * @throws IllegalArgumentException if the target player selection is invalid
     */
    public void handleCardRequest(Player currentPlayer, List<Player> availablePlayers, 
            CatType catType) {
        Player targetPlayer = inputHandler.selectTargetPlayer(availablePlayers);
        if (targetPlayer == null || !availablePlayers.contains(targetPlayer)) {
            throw new IllegalArgumentException("Invalid target player selection");
        }
        if (targetPlayer.getHand().isEmpty()) {
            throw new IllegalStateException("Target player has no cards");
        }

        // Validate that the current player has a CatRequestCard
        boolean hasRequestCard = false;
        for (Card card : currentPlayer.getHand()) {
            if (card instanceof CatRequestCard) {
                hasRequestCard = true;
                break;
            }
        }
        if (!hasRequestCard) {
            throw new IllegalStateException("Current player must have a CatRequestCard");
        }

        removeThreeCatCards(currentPlayer, catType);
        removeCatRequestCard(currentPlayer);
        requestCardFromTarget(currentPlayer, targetPlayer);
    }

    private void removeThreeCatCards(Player currentPlayer, CatType catType) {
        List<Card> cardsToRemove = new ArrayList<>();
        List<Card> handCopy = new ArrayList<>(currentPlayer.getHand());
        for (Card card : handCopy) {
            if (card instanceof CatCard && ((CatCard) card).getCatType() == catType) {
                cardsToRemove.add(card);
                if (cardsToRemove.size() == 3) {
                    break;
                }
            }
        }

        if (cardsToRemove.size() != 3) {
            throw new IllegalStateException("Must have exactly 3 cat cards of the same type");
        }

        for (Card card : cardsToRemove) {
            currentPlayer.removeCard(card);
        }
    }

    private void removeCatRequestCard(Player currentPlayer) {
        List<Card> handCopy = new ArrayList<>(currentPlayer.getHand());
        for (Card card : handCopy) {
            if (card instanceof CatRequestCard) {
                currentPlayer.removeCard(card);
                break;
            }
        }
    }

    private void requestCardFromTarget(Player currentPlayer, Player targetPlayer) {
        Card requestedCard = inputHandler.selectCard(targetPlayer);
        targetPlayer.removeCard(requestedCard);
        currentPlayer.receiveCard(requestedCard);
    }
} 