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

    @Test
    public void testPeekTopTwoCards_oneCard() {
        Card card = new DefuseCard();
        deck.addCard(card);
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertEquals(1, topCards.size(), "Should return 1 card");
        assertEquals(card, topCards.get(0));
    }

    @Test
    public void testPeekTopTwoCards_twoCards() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        deck.addCard(card1);
        deck.addCard(card2);
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertEquals(2, topCards.size(), "Should return 2 cards");
        assertEquals(card1, topCards.get(0));
        assertEquals(card2, topCards.get(1));
    }


    @Test
    public void testPeekTopTwoCards_moreThanTwoCards() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        Card card3 = new DefuseCard();
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertEquals(2, topCards.size(), "Should return top 2 cards");
        assertEquals(card1, topCards.get(0));
        assertEquals(card2, topCards.get(1));
    }

    @Test
    public void testPeekTopTwoCards_nullDeck() {
        assertThrows(IllegalArgumentException.class, () -> {
            seeTheFutureCard.peekTopTwoCards(null);
        }, "Should throw IllegalArgumentException for null deck");
    }
}
