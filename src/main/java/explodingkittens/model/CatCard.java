package explodingkittens.model;

import explodingkittens.controller.CatCardStealInputHandler;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a Cat card in the Exploding Kittens game.
 * Cat cards can be used to steal cards from other players.
 */
public class CatCard extends Card {
    private final CatType catType;
    private static CatCardStealInputHandler inputHandler;

    public CatCard(CatType catType) {
        super(CardType.CAT_CARD);
        this.catType = catType;
    }

    /**
     * Gets the type of cat card.
     * @return The type of cat card
     */
    public CatType getCatType() {
        return catType;
    }

    /**
     * Sets the input handler for card stealing.
     * @param handler The input handler to use
     */
    public static void setInputHandler(CatCardStealInputHandler handler) {
        inputHandler = handler;
    }

    private CatCard findFirstCatCard(List<Card> hand) {
        for (Card card : hand) {
            if (card instanceof CatCard && ((CatCard) card).catType == this.catType) {
                return (CatCard) card;
            }
        }
        return null;
    }

    private CatCard findSecondCatCard(List<Card> hand, CatCard firstCard) {
        boolean foundFirst = false;
        for (Card card : hand) {
            if (card == firstCard) {
                foundFirst = true;
                continue;
            }
            if (foundFirst && card instanceof CatCard && ((CatCard) card).catType == this.catType) {
                return (CatCard) card;
            }
        }
        return null;
    }

    private List<Player> getAvailableTargets(List<Player> turnOrder, Player currentPlayer) {
        return turnOrder.stream()
            .filter(p -> p != currentPlayer && p.isAlive() && !p.getHand().isEmpty())
            .collect(Collectors.toList());
    }

    private void validateInputHandler() {
        if (inputHandler == null) {
            throw new IllegalStateException("Input handler not set");
        }
    }

    private CatCard[] findCatCardPair(List<Card> hand) {
        CatCard firstCard = findFirstCatCard(hand);
        if (firstCard == null) {
            throw new IllegalStateException("Player must have two cat cards to use this effect");
        }

        CatCard secondCard = findSecondCatCard(hand, firstCard);
        if (secondCard == null) {
            throw new IllegalStateException("Player must have two cat cards to use this effect");
        }

        return new CatCard[]{firstCard, secondCard};
    }

    private Player selectTargetPlayer(List<Player> turnOrder, Player currentPlayer) {
        List<Player> availablePlayers = getAvailableTargets(turnOrder, currentPlayer);
        if (availablePlayers.isEmpty()) {
            throw new IllegalStateException("No valid target players available");
        }

        Player targetPlayer = inputHandler.selectTargetPlayer(availablePlayers);
        if (targetPlayer == null || !availablePlayers.contains(targetPlayer)) {
            throw new IllegalArgumentException("Invalid target player selection");
        }
        return targetPlayer;
    }

    private int selectCardIndex(Player targetPlayer) {
        int cardIndex = inputHandler.selectCardIndex(targetPlayer.getHand().size());
        if (cardIndex < 0 || cardIndex >= targetPlayer.getHand().size()) {
            throw new IllegalArgumentException("Invalid card index selection");
        }
        return cardIndex;
    }

    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        validateInputHandler();
        Player currentPlayer = turnOrder.get(0);
        validatePlayerTurns(currentPlayer);
        
        // Find two cat cards of the same type
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

    private void validatePlayerTurns(Player currentPlayer) {
        if (currentPlayer.getLeftTurns() <= 0) {
            throw new IllegalStateException("No turns left");
        }
    }

    private void validateTargetPlayer(Player targetPlayer) {
        if (targetPlayer.getHand().isEmpty()) {
            throw new IllegalStateException("Target player has no cards");
        }
    }

    /**
     * Plays the cat card effect for the current player.
     * @param currentPlayer The player playing the card
     * @param turnOrder The list of players in turn order
     */
    public void play(Player currentPlayer, List<Player> turnOrder) {
        effect(turnOrder, null);
    }

    /**
     * Result of a cat card effect, containing the cards to be removed and the target information.
     */
    public static class CatCardEffect extends RuntimeException {
        private final CatCard firstCard;
        private final CatCard secondCard;
        private final Player targetPlayer;
        private final int targetCardIndex;

        public CatCardEffect(CatCard firstCard, CatCard secondCard, Player targetPlayer, 
                int targetCardIndex) {
            this.firstCard = firstCard;
            this.secondCard = secondCard;
            this.targetPlayer = targetPlayer;
            this.targetCardIndex = targetCardIndex;
        }

        /**
         * Gets the first cat card involved in the effect.
         * @return The first cat card
         */
        public CatCard getFirstCard() { 
            return firstCard; 
        }

        /**
         * Gets the second cat card involved in the effect.
         * @return The second cat card
         */
        public CatCard getSecondCard() { 
            return secondCard; 
        }

        /**
         * Gets the target player of the effect.
         * @return The target player
         */
        public Player getTargetPlayer() { 
            return targetPlayer; 
        }

        /**
         * Gets the index of the target card in the target player's hand.
         * @return The target card index
         */
        public int getTargetCardIndex() { 
            return targetCardIndex; 
        }
    }
}