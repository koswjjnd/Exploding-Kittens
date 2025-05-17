package explodingkittens.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.model.Deck;

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
        Deck emptyDeck = new Deck();
        assertThrows(EmptyDeckException.class, () -> drawService.drawCardFromTop(emptyDeck),
            "Should throw EmptyDeckException when deck is empty");
    }
}
