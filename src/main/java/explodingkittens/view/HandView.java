package explodingkittens.view;

import explodingkittens.model.Card;
import java.util.List;

/**
 * A view component for displaying player hands.
 */
public class HandView {
    private static final int CARD_WIDTH = 20;
    
    /**
     * Displays a player's hand in a formatted way.
     * @param playerName The name of the player
     * @param hand The list of cards in the hand
     * @param showIndices Whether to show card indices
     */
    public void displayHand(String playerName, List<Card> hand, boolean showIndices) {
        if (hand.isEmpty()) {
            System.out.println("\n" + playerName + "'s hand is empty.");
            return;
        }

        System.out.println("\n" + playerName + "'s hand:");
        
        // Display one card per line
        for (int i = 0; i < hand.size(); i++) {
            String cardName = getCardDisplayName(hand.get(i));
            if (showIndices) {
                System.out.printf("%d. %s%n", i + 1, cardName);
            } else {
                System.out.println(cardName);
            }
        }
    }
    
    /**
     * Gets the display name for a card.
     * @param card The card to get the display name for
     * @return The display name of the card
     */
    private String getCardDisplayName(Card card) {
        String className = card.getClass().getSimpleName();
        // Remove "Card" suffix if present
        if (className.endsWith("Card")) {
            className = className.substring(0, className.length() - 4);
        }
        return className;
    }
    
    /**
     * Displays a player's hand with card indices.
     * @param playerName The name of the player
     * @param hand The list of cards in the hand
     */
    public void displayHandWithIndices(String playerName, List<Card> hand) {
        displayHand(playerName, hand, true);
    }
    
    /**
     * Displays a player's hand without card indices.
     * @param playerName The name of the player
     * @param hand The list of cards in the hand
     */
    public void displayHandWithoutIndices(String playerName, List<Card> hand) {
        displayHand(playerName, hand, false);
    }
} 