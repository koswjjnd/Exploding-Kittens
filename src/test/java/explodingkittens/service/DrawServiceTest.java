package explodingkittens.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import explodingkittens.exceptions.InvalidDeckException;
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
}
