package explodingkittens.view;

import explodingkittens.model.Card;
import java.util.List;

/**
 * A view for displaying the future cards in the game.
 */
public class SeeTheFutureView {
    /**
     * Displays the future cards to the player.
     * @param cards the list of cards to display
     */
    public void display(List<Card> cards) {
        System.out.println("===== You see the future cards =====");
        for (Card c : cards) {
            System.out.println("- " + c.getType());
        }
    }
}
