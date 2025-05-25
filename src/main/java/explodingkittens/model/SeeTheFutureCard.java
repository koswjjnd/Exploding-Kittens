package explodingkittens.model;

import explodingkittens.view.SeeTheFutureView;
import java.util.List;
import java.util.ArrayList;

/**
 * A card that allows the player to see the top two cards of the deck.
 */
public class SeeTheFutureCard extends Card {

    private SeeTheFutureView view;

    /**
     * Constructs a See The Future card.
     */
    public SeeTheFutureCard() {
        super(CardType.SEE_THE_FUTURE);
    }

    /**
     * Sets the view for displaying the future cards.
     * @param view the view to display the future cards
     */
    public void setView(SeeTheFutureView view) {
        this.view = view;
    }

    @Override
    public void effect(List<Player> turnOrder, Deck deck) {
        List<Card> topCards = peekTopTwoCards(deck);

        // 这里用注入的 view 展示
        if (view != null) {
            view.display(topCards);
        } 
        else {
            System.out.println("No view available to display future cards!");
        }
    }

    /**
     * Peeks at the top two cards of the deck.
     * @param deck the deck to peek from
     * @return a list containing up to two cards from the top of the deck
     * @throws IllegalArgumentException if deck is null
     */
    public List<Card> peekTopTwoCards(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null.");
        }
        int size = Math.min(2, deck.getCards().size());
        return new ArrayList<>(deck.getCards().subList(0, size));
    }
}
