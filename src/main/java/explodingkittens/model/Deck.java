package explodingkittens.model;

import explodingkittens.exceptions.EmptyDeckException;

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

    /**
     * Creates a new empty deck.
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * 拷贝构造函数，实现深拷贝。
     * @param other 要拷贝的Deck对象
     */
    public Deck(Deck other) {
        this.cards = new ArrayList<>();
        for (Card card : other.cards) {
            this.cards.add(card.clone()); // 假设Card实现了clone()
        }
    }

    /**
     * Draws one card from the top of the deck.
     * 
     * @return The card drawn from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawOne() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.remove(0);
    }

    /**
     * Inserts a card at the specified position in the deck.
     * 
     * @param card The card to insert
     * @param position The position to insert the card at (0-based)
     * @throws IllegalArgumentException if position is invalid or card is null
     */
    public void insertAt(Card card, int position) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (position < 0 || position > cards.size()) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        cards.add(position, card);
    }

    /**
     * Returns the top card of the deck without removing it.
     * 
     * @return The top card of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card peekTop() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot peek at an empty deck");
        }
        return cards.get(0);
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Returns the number of cards in the deck.
     * 
     * @return The size of the deck
     */
    public int size() {
        return cards.size();
    }

    /**
     * Returns a copy of the cards in the deck.
     * 
     * @return A list of cards in the deck
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Removes and returns the top card from the deck.
     * @return the removed card
     * @throws EmptyDeckException if the deck is empty
     */
    public Card removeTopCard() {
        if (cards.isEmpty()) {
            throw new EmptyDeckException();
        }
        return cards.remove(0);
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
        this.addCards(new DefuseCard(), 5-playerCount);
        this.addCards(new AttackCard(), 2);
        this.addCards(new SkipCard(), 2);
        this.addCards(new ShuffleCard(), 2);
        this.addCards(new SeeTheFutureCard(), 2);
        this.addCards(new NopeCard(), 4);
        this.addCards(new WatermelonCatCard(), 5);
        this.addCards(new BeardCatCard(), 5);
        this.addCards(new HairyPotatoCatCard(), 5);
        this.addCards(new RainbowCatCard(), 5);
        this.addCards(new TacoCatCard(), 5);
        this.addCards(new SnatchCard(), 1);
        this.addCards(new SwitchDeckByHalfCard(), 1);
        this.addCards(new TimeRewindCard(), 1);
        this.addCards(new FavorCard(), 1);
        this.addCards(new DrawFromBottomCard(), 2);
        this.addCards(new ReverseCard(), 2);
        this.addCards(new SuperSkipCard(), 2);
        this.addCards(new DoubleSkipCard(), 2);

    }
    
    /**
     * Adds multiple copies of a card to the deck.
     * @param card The card to add
     * @param count The number of copies to add
     * @throws IllegalArgumentException if card is null or count is negative
     */
    public void addCards(Card card, int count) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        for (int i = 0; i < count; i++) {
            this.cards.add(card.clone());
        }
    }
    
    /**
     * Adds a single card to the deck.
     * @param card The card to add
     * @throws IllegalArgumentException if card is null
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
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
                key = "CatCard_" + ((CatCard) card).getCatType().name();  // e.g., CatCard_TACOCAT
            } 
            else {
                // e.g., CardType.DEFUSE -> DefuseCard
                String base = card.getType().name().toLowerCase();
                key = Character.toUpperCase(base.charAt(0)) + base.substring(1) + "Card";
            }
    
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }
    

    /**
     * Determines the type key for a card.
     * @param card The card to determine the type for
     * @return A string key representing the card type
     */
    private String getCardTypeKey(Card card) {
        if (card instanceof CatCard) {
            return "CatCard_" + ((CatCard) card).getType().name();
        } 
        if (card instanceof DefuseCard) {
            return "DefuseCard";
        } 
        if (card instanceof AttackCard) {
            return "AttackCard";
        } 
        if (card instanceof SkipCard) {
            return "SkipCard";
        } 
        if (card instanceof ShuffleCard) {
            return "ShuffleCard";
        } 
        if (card instanceof SeeTheFutureCard) {
            return "SeeTheFutureCard";
        } 
        if (card instanceof NopeCard) {
            return "NopeCard";
        }
        if (card instanceof ExplodingKittenCard) {
            return "ExplodingKittenCard";
        }
        return "UnknownCard";
    }

    /**
     * Shuffles the deck of cards using the provided random generator.
     * If no random generator is provided, uses a default one.
     * @param random the random generator to use for shuffling, or null to use default
     */
    public void shuffle(Random random) {
        Collections.shuffle(cards, random != null ? random : new Random());
    }

    /**
     * Validates that the deck contains the correct number of cards for the given player count.
     * @param playerCount the number of players in the game
     * @return true if the deck is valid, false otherwise
     */
    public boolean validateDeck(int playerCount) {
        if (playerCount < 2 || playerCount > 4) {
            return false;
        }
        Map<String, Integer> counts = getCardCounts();
        if (!validateDefuse(counts, playerCount)) {
            return false;
        }
        if (!validateMainCards(counts)) {
            return false;
        }
        if (!validateCatCards(counts)) {
            return false;
        }
        return true;
    }

    private boolean validateDefuse(Map<String, Integer> counts, int playerCount) {
        return counts.getOrDefault("DefuseCard", 0) == 5 - playerCount;
    }

    private boolean validateMainCards(Map<String, Integer> counts) {
        if (counts.getOrDefault("AttackCard", 0) != 3) {
            return false;
        }
        if (counts.getOrDefault("SkipCard", 0) != 3) {
            return false;
        }
        if (counts.getOrDefault("ShuffleCard", 0) != 4) {
            return false;
        }
        if (counts.getOrDefault("SeeTheFutureCard", 0) != 4) {
            return false;
        }
        if (counts.getOrDefault("NopeCard", 0) != 4) {
            return false;
        }
        return true;
    }

    private boolean validateCatCards(Map<String, Integer> counts) {
        for (CatType type : CatType.values()) {
            if (counts.getOrDefault("CatCard_" + type.name(), 0) != 4) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds exploding kittens to the deck.
     * @param count The number of exploding kittens to add
     * @throws IllegalArgumentException if count is negative
     */
    public void addExplodingKittens(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        for (int i = 0; i < count; i++) {
            this.cards.add(new ExplodingKittenCard());
        }
    }

    /**
     * Switches the top and bottom halves of the deck.
     * For even-sized decks, swaps the two halves.
     * For odd-sized decks, the middle card stays in place.
     */
    public void switchTopAndBottomHalf() {
        int size = cards.size();
        if (size < 2) {
            return; // No change for empty deck or single card
        }

        int mid = size / 2;
        List<Card> topHalf;
        List<Card> bottomHalf;

        if (size % 2 == 0) {
            // Even number of cards
            topHalf = new ArrayList<>(cards.subList(0, mid));
            bottomHalf = new ArrayList<>(cards.subList(mid, size));
            cards.clear();
            cards.addAll(bottomHalf);
            cards.addAll(topHalf);
        } 
        else {
            // Odd number of cards - middle card stays in place
            topHalf = new ArrayList<>(cards.subList(0, mid));
            Card middle = cards.get(mid);
            bottomHalf = new ArrayList<>(cards.subList(mid + 1, size));

            cards.clear();
            cards.addAll(bottomHalf);
            cards.add(middle);
            cards.addAll(topHalf);
        }
    }
}
