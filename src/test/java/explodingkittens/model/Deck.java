package explodingkittens.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void initializeBaseDeck(int playerCount) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Player count must be between 2 and 4");
        }
        
        // Clear existing cards
        this.cards.clear();
        
        // Add defuse cards based on player count
        this.addCards(new DefuseCard(),5-playerCount);
        
        // Add 3 Attack cards
        this.addCards(new AttackCard(),3);
        
        // Add 3 Skip cards
        this.addCards(new SkipCard(),3);
        
        // Add 4 Shuffle card
        this.addCards(new ShuffleCard(),4);
        
        // Add 4 See the Future card
        this.addCards(new SeeTheFutureCard(),4);
        
        // Add 4 Nope card
        this.addCards(new NopeCard(),4);
        
        // Add 4 of each type of Cat cards
        for (CatType type : CatType.values()) {
            this.addCards(new CatCard(type),4);
        }
    }
    
    public void addCard(Card card) {
        this.cards.add(card);
    }
    
    public void addCards(Card card, int count) {
        for (int i = 0; i < count; i++) {
            this.cards.add(card);
        }
    }
    
    public Map<String, Integer> getCardCounts() {
        Map<String, Integer> counts = new HashMap<>();
        
        for (Card card : cards) {
            String key;
            if (card instanceof CatCard) {
                key = "CatCard_" + ((CatCard) card).getType().name();
            } else {
                key = card.getClass().getSimpleName();
            }
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        
        return counts;
    }
} 