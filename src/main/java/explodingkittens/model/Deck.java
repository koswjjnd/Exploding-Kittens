package explodingkittens.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a deck of cards in the Exploding Kittens game.
 */
public class Deck {
    private List<Card> cards;

    /** Creates an empty deck. */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Returns the list of cards in the deck.
     * @return the list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Initializes the deck with cards based on player count.
     * @param playerCount number of players (2-4)
     * @throws IllegalArgumentException if playerCount is invalid
     */
    public void initializeBaseDeck(int playerCount) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Player count must be between 2 and 4");
        }
        
        this.cards.clear();
        this.addCards(new DefuseCard(),5-playerCount);
        this.addCards(new AttackCard(),3);
        this.addCards(new SkipCard(),3);
        this.addCards(new ShuffleCard(),4);
        this.addCards(new SeeTheFutureCard(),4);
        this.addCards(new NopeCard(),4);
        
        for (CatType type : CatType.values()) {
            this.addCards(new CatCard(type),4);
        }
    }
    
    /**
     * Adds multiple copies of a card to the deck.
     * @param card The card to add
     * @param count The number of copies to add
     */
    public void addCards(Card card, int count) {
        for (int i = 0; i < count; i++) {
            this.cards.add(card.clone());
        }
    }
    
    /**
     * Adds a single card to the deck.
     * @param card The card to add
     */
    public void addCard(Card card) {
        this.cards.add(card.clone());
    }
    
    /**
     * Returns counts of each card type in the deck.
     * @return A map containing the count of each card type
     */
    public Map<String, Integer> getCardCounts() {
        Map<String, Integer> counts = new HashMap<>();
        
        for (Card card : cards) {
            String key;
            if (card instanceof CatCard) {
                key = "CatCard_" + ((CatCard) card).getType().name();
            } 
            else {
                key = card.getClass().getSimpleName();
            }
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }

    /**
     * Shuffles the deck of cards using the provided random generator.
     * If no random generator is provided, uses a default one.
     * @param random the random generator to use for shuffling, or null to use default
     */
    public void shuffle(Random random) {
        Collections.shuffle(cards, random != null ? random : new Random());
    }
}
