package explodingkittens.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String name;
    private final List<Card> hand;
    private int leftTurns;

    /**
     * Constructs a Player with the given name.
     * @param name the player's nickname
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.leftTurns = 1;
    }

    /**
     * Gets the player's nickname.
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the number of turns left for the player.
     * @return The number of turns left
     */
    public int getLeftTurns() {
        return leftTurns;
    }

    /**
     * Sets the number of turns left for the player.
     * @param turns The number of turns to set
     */
    public void setLeftTurns(int turns) {
        this.leftTurns = turns;
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