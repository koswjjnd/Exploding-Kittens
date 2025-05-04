package explodingkittens.player;

import java.util.ArrayList;
import java.util.List;
import explodingkittens.card.Card;

public class Player {
    private final String name;
    private final List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return List.copyOf(hand);
    }
} 