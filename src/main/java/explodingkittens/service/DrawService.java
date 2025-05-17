package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import java.util.List;

public class DrawService {
    /**
     * Draws a card from the top of the deck.
     * @param deck the deck to draw from
     * @return the drawn card
     * @throws InvalidDeckException if the deck is null
     * @throws EmptyDeckException if the deck is empty
     */
    public Card drawCardFromTop(Deck deck) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        if (deck.isEmpty()) {
            throw new EmptyDeckException();
        }
        
        return deck.removeTopCard();
    }
} 