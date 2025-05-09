package explodingkittens.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;


public class ShuffleTest {
    private Deck deck;
    private Random fixedRandom;

    @BeforeEach
    void setUp() {
        fixedRandom = new Random(42);
        deck = new Deck();
    }

    @Test
    void testShuffleEmptyDeck() {
        // test empty deck
        deck.shuffle(null); 
        assertEquals(0, deck.getCardCounts().size(), 
                "Empty deck should remain empty after shuffle");
    }

    @Test
    void testShuffleSingleCard() {
        // test single card
        deck.addCard(new DefuseCard());
        Map<String, Integer> originalCounts = new HashMap<>(deck.getCardCounts());
        List<Card> originalOrder = new ArrayList<>(deck.getCards());
        
        deck.shuffle(null);
        
        assertEquals(originalCounts, deck.getCardCounts(), 
                "Card counts should remain the same after shuffle");
        assertEquals(originalOrder, deck.getCards(), 
                "Single card should remain in the same position");
    }

    @Test
    void testShuffleFiveDifferentCards() {
        // test 5 different cards
        deck.addCard(new DefuseCard());
        deck.addCard(new AttackCard());
        deck.addCard(new SkipCard());
        deck.addCard(new ShuffleCard());
        deck.addCard(new SeeTheFutureCard());
        
        Map<String, Integer> originalCounts = new HashMap<>(deck.getCardCounts());
        List<Card> originalOrder = new ArrayList<>(deck.getCards());
        
        deck.shuffle(fixedRandom);
        
        assertEquals(originalCounts, deck.getCardCounts(), 
                "All original cards should be preserved");
        assertNotEquals(originalOrder, deck.getCards(), 
                "Shuffle should change the order of cards");
    }

    @Test
    void testShuffleFullDeck() {
        // TC4: test the full deck
        deck.initializeBaseDeck(2); // initialize a base deck
        Map<String, Integer> originalCounts = new HashMap<>(deck.getCardCounts());
        List<Card> originalOrder = new ArrayList<>(deck.getCards());
        
        deck.shuffle(fixedRandom);
        
        assertEquals(originalCounts, deck.getCardCounts(), 
                "All original cards should be preserved");
        assertNotEquals(originalOrder, deck.getCards(), 
                "Shuffle should change the order of cards");
    }

    @Test
    void testMultipleShuffles() {
        // TC5: test multiple shuffles
        deck.initializeBaseDeck(2);
        deck.shuffle(fixedRandom); // first shuffle using fixed random seed
        List<Card> firstShuffle = new ArrayList<>(deck.getCards());
        
        deck.shuffle(new Random(43)); // second shuffle using different random seed
        List<Card> secondShuffle = new ArrayList<>(deck.getCards());
        
        assertNotEquals(firstShuffle, secondShuffle, 
                "Multiple shuffles should result in different card orders");
    }
}
