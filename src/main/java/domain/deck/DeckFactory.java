package domain.deck;

import domain.card.*;

public class DeckFactory {
    public static Deck createDeck() {
        Deck deck = new Deck();

        for (int i = 0; i < 4; i++) {
            deck.addCard(new AttackCard());
        }

        for (int i = 0; i < 4; i++) {
            deck.addCard(new SkipCard());
        }

        for (int i = 0; i < 4; i++) {
            deck.addCard(new ShuffleCard());
        }

        for (int i = 0; i < 5; i++) {
            deck.addCard(new SeeTheFutureCard());
        }

        for (int i = 0; i < 5; i++) {
            deck.addCard(new NopeCard());
        }
        
        deck.shuffle();
        return deck;
    }
} 