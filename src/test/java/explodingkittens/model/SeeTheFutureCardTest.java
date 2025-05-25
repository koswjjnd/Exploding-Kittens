package explodingkittens.model;

import explodingkittens.view.SeeTheFutureView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for SeeTheFutureCard.
 */
public class SeeTheFutureCardTest {

    private Deck deck;
    private SeeTheFutureCard seeTheFutureCard;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        deck = new Deck();
        seeTheFutureCard = new SeeTheFutureCard();
        System.setOut(new PrintStream(outContent)); // 拦截 System.out
    }

    /**
     * Tests peeking top cards from an empty deck.
     */
    @Test
    public void testPeekTopTwoCardsEmptyDeck() {
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertTrue(topCards.isEmpty(), "Empty deck should return empty list");
    }

    /**
     * Tests peeking top cards from a deck with one card.
     */
    @Test
    public void testPeekTopTwoCardsOneCard() {
        Card card = new DefuseCard();
        deck.addCard(card);
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertEquals(1, topCards.size(), "Should return 1 card");
        assertEquals(card, topCards.get(0));
    }

    /**
     * Tests peeking top cards from a deck with two cards.
     */
    @Test
    public void testPeekTopTwoCardsTwoCards() {
        Card card1 = new AttackCard();
        Card card2 = new SkipCard();
        deck.addCard(card1);
        deck.addCard(card2);
        List<Card> topCards = seeTheFutureCard.peekTopTwoCards(deck);
        assertEquals(2, topCards.size(), "Should return 2 cards");
        assertEquals(card1, topCards.get(0));
        assertEquals(card2, topCards.get(1));
    }

    /**
     * Tests peeking top cards from a deck with more than two cards.
     */
    @Test
    public void testPeekTopTwoCardsMoreThanTwoCards() {
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

    /**
     * Tests peeking top cards from a null deck.
     */
    @Test
    public void testPeekTopTwoCardsNullDeck() {
        assertThrows(IllegalArgumentException.class, () -> {
            seeTheFutureCard.peekTopTwoCards(null);
        }, "Should throw IllegalArgumentException for null deck");
    }

    /**
     * Tests the effect method with a view.
     */
    @Test
    public void testEffectWithView() {
        // 创建一个假的 view
        FakeSeeTheFutureView fakeView = new FakeSeeTheFutureView();
        seeTheFutureCard.setView(fakeView);

        // 添加几张牌
        deck.addCard(new AttackCard());
        deck.addCard(new SkipCard());
        deck.addCard(new ShuffleCard());

        seeTheFutureCard.effect(List.of(), deck);

        assertTrue(fakeView.wasCalled, "View's display() should be called");
        assertEquals(2, fakeView.cardsShown.size(), "Should show 2 cards");
    }

    /**
     * Tests the effect method without a view.
     */
    @Test
    public void testEffectWithoutView() {
        // 添加几张牌
        deck.addCard(new AttackCard());
        deck.addCard(new SkipCard());

        seeTheFutureCard.effect(List.of(), deck);

        String output = outContent.toString();
        assertTrue(output.contains("No view available to display future cards!"), 
            "Should print fallback message");
    }

    /**
     * Tests the effect method with a view on an empty deck.
     */
    @Test
    public void testEffectWithViewEmptyDeck() {
        FakeSeeTheFutureView fakeView = new FakeSeeTheFutureView();
        seeTheFutureCard.setView(fakeView);

        seeTheFutureCard.effect(List.of(), deck);

        assertTrue(fakeView.wasCalled, "View's display() should be called");
        assertEquals(0, fakeView.cardsShown.size(), "Should show empty list");
    }

    /**
     * Tests the effect method with a view on a deck with one card.
     */
    @Test
    public void testEffectWithViewOneCard() {
        FakeSeeTheFutureView fakeView = new FakeSeeTheFutureView();
        seeTheFutureCard.setView(fakeView);

        deck.addCard(new AttackCard());
        seeTheFutureCard.effect(List.of(), deck);

        assertTrue(fakeView.wasCalled);
        assertEquals(1, fakeView.cardsShown.size(), "Should show 1 card");
    }

    /**
     * Tests the effect method with a view on a deck with two cards.
     */
    @Test
    public void testEffectWithViewTwoCards() {
        FakeSeeTheFutureView fakeView = new FakeSeeTheFutureView();
        seeTheFutureCard.setView(fakeView);

        deck.addCard(new AttackCard());
        deck.addCard(new SkipCard());
        seeTheFutureCard.effect(List.of(), deck);

        assertTrue(fakeView.wasCalled);
        assertEquals(2, fakeView.cardsShown.size(), "Should show 2 cards");
    }

    /**
     * A fake implementation of SeeTheFutureView for testing.
     */
    private static class FakeSeeTheFutureView extends SeeTheFutureView {
        boolean wasCalled = false;
        List<Card> cardsShown;

        @Override
        public void display(List<Card> cards) {
            wasCalled = true;
            cardsShown = cards;
        }
    }
}
