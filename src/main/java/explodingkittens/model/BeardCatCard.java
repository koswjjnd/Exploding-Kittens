package com.example.courseproject.model;

import java.util.List;

/**
 * Represents a Beard Cat card in the Exploding Kittens game.
 * Beard Cat cards can be used to steal cards from other players,
 * but only when two Beard Cat cards are used together.
 */
public class BeardCatCard extends CatCard {
    
    public BeardCatCard() {
        super(CatType.BEARD_CAT);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        validateInputHandler();
        Player currentPlayer = turnOrder.get(0);
        validatePlayerTurns(currentPlayer);
        
        // Find two beard cat cards
        CatCard[] catCards = findCatCardPair(currentPlayer.getHand());
        
        // Get available targets and validate
        List<Player> availableTargets = getAvailableTargets(turnOrder, currentPlayer);
        if (availableTargets.isEmpty()) {
            throw new IllegalStateException("No valid target players available");
        }

        // Select target player and validate
        Player targetPlayer = selectTargetPlayer(turnOrder, currentPlayer);
        validateTargetPlayer(targetPlayer);

        // Select card to steal
        int cardIndex = selectCardIndex(targetPlayer);

        // Throw CatCardEffect to handle the actual card stealing
        throw new CatCardEffect(catCards[0], catCards[1], targetPlayer, cardIndex);
    }
} 