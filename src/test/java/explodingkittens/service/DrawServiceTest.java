package explodingkittens.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.SkipCard;
import explodingkittens.model.AttackCard;
import explodingkittens.model.DefuseCard;
import java.util.List;

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

    /**
     * Tests that drawing from a deck with multiple cards returns the first card and decreases deck size.
     */
    @Test
    void testDrawCardFromTop_MultipleCards() {
        DrawService drawService = new DrawService();
        Deck deck = new Deck();
        Card skipCard = new SkipCard();
        Card attackCard = new AttackCard();
        Card defuseCard = new DefuseCard();
        
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        deck.addCard(defuseCard);
        
        Card drawnCard = drawService.drawCardFromTop(deck);
        
        assertFalse(deck.isEmpty(), "Deck should not be empty after drawing one card");
        assertEquals(2, deck.getCards().size(), "Deck should have 2 cards remaining");
        assertTrue(drawnCard instanceof SkipCard, "First drawn card should be a SkipCard");
        
        // Verify the remaining cards are in the correct order
        List<Card> remainingCards = deck.getCards();
        assertTrue(remainingCards.get(0) instanceof AttackCard, "Second card should be an AttackCard");
        assertTrue(remainingCards.get(1) instanceof DefuseCard, "Third card should be a DefuseCard");
    }
}
