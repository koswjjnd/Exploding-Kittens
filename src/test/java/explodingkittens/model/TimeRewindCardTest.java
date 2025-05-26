package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class TimeRewindCardTest {
    private TimeRewindCard card;
    private List<Player> turnOrder;
    private Deck deck;
    private List<Card> deckCards;

    @BeforeEach
    void setUp() {
        card = new TimeRewindCard();
        turnOrder = new ArrayList<>();
        deck = mock(Deck.class);
        deckCards = new ArrayList<>();
        when(deck.getCards()).thenReturn(deckCards);
    }

    @Test
    void testNullDeckThrows() {
        assertThrows(IllegalArgumentException.class, () -> 
            card.effect(turnOrder, null));
    }

    @Test
    void testEmptyDeckThrows() {
        assertThrows(IllegalStateException.class, () -> 
            card.effect(turnOrder, deck));
    }

    @Test
    void testSingleCardThrows() {
        deckCards.add(mock(Card.class));
        assertThrows(IllegalStateException.class, () -> 
            card.effect(turnOrder, deck));
    }

    @Test
    void testTwoCardsThrows() {
        deckCards.add(mock(Card.class));
        deckCards.add(mock(Card.class));
        assertThrows(IllegalStateException.class, () -> 
            card.effect(turnOrder, deck));
    }

    @Test
    void testThreeCardsMovesAllToBottom() {
        // Create cards with specific types for easier verification
        Card card1 = new SkipCard();
        Card card2 = new AttackCard();
        Card card3 = new FavorCard();
        
        deckCards.add(card1);
        deckCards.add(card2);
        deckCards.add(card3);
        
        card.effect(turnOrder, deck);
        
        assertEquals(3, deckCards.size());
        assertEquals(CardType.SKIP, deckCards.get(0).getType());
        assertEquals(CardType.ATTACK, deckCards.get(1).getType());
        assertEquals(CardType.FAVOR, deckCards.get(2).getType());
    }

    @Test
    void testMoreThanThreeCardsMovesTopThree() {
        Card card1 = new SkipCard();
        Card card2 = new AttackCard();
        Card card3 = new FavorCard();
        Card card4 = new SeeTheFutureCard();
        
        deckCards.add(card1);
        deckCards.add(card2);
        deckCards.add(card3);
        deckCards.add(card4);
        
        card.effect(turnOrder, deck);
        assertEquals(4, deckCards.size());
        assertEquals(CardType.SEE_THE_FUTURE, deckCards.get(0).getType());
        assertEquals(CardType.SKIP, deckCards.get(1).getType());
        assertEquals(CardType.ATTACK, deckCards.get(2).getType());
        assertEquals(CardType.FAVOR, deckCards.get(3).getType());
    }
} 
