package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SeeTheFutureCardTest {

    private Deck deck;
    private SeeTheFutureCard seeTheFutureCard;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
        seeTheFutureCard = new SeeTheFutureCard();
    }

    @Test
    public void testPeekTopTwoCards_emptyDeck() {
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertTrue(topCards.isEmpty(), "Empty deck should return empty list");
    }

    
}
