package explodingkittens.model;

import java.util.List;

/**
 * Represents a Feral Cat card in the Exploding Kittens game.
 * Feral Cat cards can be used as any other cat card type.
 * When played, the player must choose which cat type to use it as.
 */
public class FeralCatCard extends CatCard {
    
    public FeralCatCard() {
        super(CatType.FERAL_CAT);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        validateInputHandler();
        Player currentPlayer = turnOrder.get(0);
        validatePlayerTurns(currentPlayer);
        
        // Find two cat cards (one feral cat and one other cat)
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

    /**
     * Override the findCatCardPair method to allow Feral Cat to pair with any other cat card
     */
    @Override
    protected CatCard[] findCatCardPair(List<Card> hand) {
        CatCard firstCard = null;
        CatCard secondCard = null;

        // First find a non-Feral cat card
        for (Card card : hand) {
            if (card instanceof CatCard && ((CatCard) card).getCatType() != CatType.FERAL_CAT) {
                firstCard = (CatCard) card;
                break;
            }
        }

        // Then find a Feral cat card
        if (firstCard != null) {
            for (Card card : hand) {
                if (card instanceof CatCard && ((CatCard) card).getCatType() == CatType.FERAL_CAT) {
                    secondCard = (CatCard) card;
                    break;
                }
            }
        }

        if (firstCard == null || secondCard == null) {
            throw new IllegalStateException("Need one Feral Cat card and one other cat card");
        }

        return new CatCard[]{firstCard, secondCard};
    }
} 