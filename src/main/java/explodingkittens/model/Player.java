package explodingkittens.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a player in the game.
 */
public class Player {
    private final String name;
    private final List<Card> hand;

    /**
     * Constructs a Player with the given name.
     * @param name the player's nickname
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    /**
     * Gets the player's nickname.
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a card to the player's hand.
     * @param card the card to add
     */
    public void receiveCard(Card card) {
        hand.add(card);
    }

    /**
     * Returns an unmodifiable view of the player's hand.
     * @return the player's hand
     */
    public List<Card> getHand() {
        return List.copyOf(hand);
    }
} 