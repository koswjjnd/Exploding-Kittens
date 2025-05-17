package explodingkittens.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.SkipCard;

public class DrawServiceTest {
    /**
     * Tests that drawing from a null deck throws InvalidDeckException.
     */
    @Test
    void testDrawCardFromTop_NullDeck() {
        DrawService drawService = new DrawService();
        assertThrows(InvalidDeckException.class, () -> drawService.drawCardFromTop(null),
            "Should throw InvalidDeckException when deck is null");
    }

    /**
     * Tests that drawing from an empty deck throws EmptyDeckException.
     */
    @Test
    void testDrawCardFromTop_EmptyDeck() {
        DrawService drawService = new DrawService();
        Deck deck = new Deck();
        assertThrows(EmptyDeckException.class, () -> drawService.drawCardFromTop(deck),
            "Should throw EmptyDeckException when deck is empty");
    }

    /**
     * Tests that drawing from a deck with a single card returns the card and makes the deck empty.
     */
    @Test
    void testDrawCardFromTop_SingleCard() {
        DrawService drawService = new DrawService();
        Deck deck = new Deck();
        Card skipCard = new SkipCard();
        deck.addCard(skipCard);
        
        Card drawnCard = drawService.drawCardFromTop(deck);
        
        assertTrue(deck.isEmpty(), "Deck should be empty after drawing the only card");
        assertTrue(drawnCard instanceof SkipCard, "Drawn card should be a SkipCard");
    }
}
