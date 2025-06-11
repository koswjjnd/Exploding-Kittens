package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for the ShuffleCard class.
 */
public class ShuffleCardTest {
    private ShuffleCard shuffleCard;
    private List<Player> turnOrder;
    private Deck gameDeck;
    private List<Player> dummyPlayers;

    @BeforeEach
    void setUp() {
        shuffleCard = new ShuffleCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        
        // add some different cards to the deck
        gameDeck.addCard(new DefuseCard());
        gameDeck.addCard(new AttackCard());
        gameDeck.addCard(new SkipCard());
        gameDeck.addCard(new ShuffleCard());
        gameDeck.addCard(new SeeTheFutureCard());

        // Initialize dummyPlayers
        dummyPlayers = new ArrayList<>();
        dummyPlayers.add(new Player("Player1"));
        dummyPlayers.add(new Player("Player2"));
        dummyPlayers.add(new Player("Player3"));
    }

    /**
     * Tests the shuffle effect changes the order of cards while preserving their count and types.
     */
    @Test
    void testEffectShufflesDeck() {
        // add some different cards
        gameDeck.addCard(new DefuseCard());
        gameDeck.addCard(new AttackCard());
        gameDeck.addCard(new SkipCard());
        gameDeck.addCard(new ShuffleCard());
        gameDeck.addCard(new SeeTheFutureCard());

        // record original order
        List<Card> originalOrder = new ArrayList<>(gameDeck.getCards());
        
        // use ShuffleCard effect
        shuffleCard.effect(dummyPlayers, gameDeck);
        
        // get shuffled order
        List<Card> newOrder = gameDeck.getCards();
        
        // check: card count and type should stay the same
        assertEquals(originalOrder.size(), newOrder.size(), 
            "Number of cards should stay the same after shuffle");
            
        // check: card count and type should stay the same
        Map<CardType, Integer> originalCounts = new HashMap<>();
        Map<CardType, Integer> newCounts = new HashMap<>();
        
        for (Card card : originalOrder) {
            originalCounts.merge(card.getType(), 1, Integer::sum);
        }
        
        for (Card card : newOrder) {
            newCounts.merge(card.getType(), 1, Integer::sum);
        }
        
        assertEquals(originalCounts, newCounts,
            "Card type counts should stay the same after shuffle");
            
        // check: order should change (very likely)
        assertNotEquals(originalOrder, newOrder, 
            "ShuffleCard effect should change the order of cards");
    }

    @Test
    void testGetType() {
        assertEquals(CardType.SHUFFLE, shuffleCard.getType());
    }

    @Test
    void testEffect() {
        // record initial order
        List<Card> initialOrder = new ArrayList<>(gameDeck.getCards());
        
        // execute shuffle effect
        shuffleCard.effect(turnOrder, gameDeck);
        
        // check: order should change
        List<Card> newOrder = gameDeck.getCards();
        assertNotEquals(initialOrder, newOrder);
        
        // check: card count and type should stay the same
        assertEquals(initialOrder.size(), newOrder.size());
        for (Card card : initialOrder) {
            assertTrue(newOrder.contains(card));
        }
    }
}
