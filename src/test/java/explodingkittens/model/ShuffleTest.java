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
        assertEquals(0, deck.getCardCounts().size(), "Empty deck should remain empty after shuffle");
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

    
}
