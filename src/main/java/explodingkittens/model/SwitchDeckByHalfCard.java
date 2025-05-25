package explodingkittens.model;

import java.util.List;

/**
 * A card that swaps the top and bottom halves of the deck.
 */
public class SwitchDeckByHalfCard extends Card {

    public SwitchDeckByHalfCard() {
        super(CardType.SWITCH_DECK_BY_HALF);
    }

    @Override
    public void effect(List<Player> turnOrder, Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        deck.switchTopAndBottomHalf();
    }
}
