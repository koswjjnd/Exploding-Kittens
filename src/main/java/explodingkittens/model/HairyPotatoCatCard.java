package explodingkittens.model;

import java.util.List;

/**
 * Represents a Hairy Potato Cat card in the Exploding Kittens game.
 * Hairy Potato Cat cards can be used to steal cards from other players,
 * similar to other cat cards.
 */
public class HairyPotatoCatCard extends CatCard {
    
    public HairyPotatoCatCard() {
        super(CatType.HAIRY_POTATO_CAT);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        validateInputHandler();
        Player currentPlayer = turnOrder.get(0);
        validatePlayerTurns(currentPlayer);
        
        // Find two hairy potato cat cards
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