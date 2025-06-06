package explodingkittens.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String name;
    private final List<Card> hand;
    private int leftTurns;
    private boolean alive;

    /**
     * Constructs a Player with the given name.
     * @param name the player's nickname
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.leftTurns = 1;
        this.alive = true;
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
     * Uses a skip card, reducing turns by 1.
     */
    public void useSkipCard() {
        if (leftTurns > 0) {
            leftTurns--;
        }
    }

    /**
     * Gets attacked, adding 2 turns.
     */
    public void getAttacked() {
        leftTurns += 2;
    }

    /**
     * Decrements the number of turns left by 1.
     * If the player has 0 turns left, the number of turns remains 0.
     */
    public void decrementLeftTurns() {
        if (leftTurns > 0) {
            leftTurns--;
        }
    }

    /**
     * Checks if the player has a defuse card in their hand.
     * @return true if the player has at least one defuse card, false otherwise
     */
    public boolean hasDefuse() {
        return hand.stream()
                .anyMatch(card -> card.getType() == CardType.DEFUSE);
    }

     /**
     * Attempts to use a defuse card from the player's hand.
     * If successful, removes one defuse card from the hand.
     * @return true if a defuse card was successfully used, false otherwise
     */
    public boolean useDefuse() {
        Optional<Card> defuseCard = hand.stream()
                .filter(card -> card.getType() == CardType.DEFUSE)
                .findFirst();
        
        if (defuseCard.isPresent()) {
            hand.remove(defuseCard.get());
            return true;
        }
        return false;
    }
    
    /**
     * Adds a card to the player's hand.
     * @param card the card to add
     * @throws IllegalArgumentException if card is null
     */
    public void receiveCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        hand.add(card);
    }

    /**
     * Returns an unmodifiable view of the player's hand.
     * @return the player's hand
     */
    public List<Card> getHand() {
        return List.copyOf(hand);
    }

    /**
     * Removes a card from the player's hand.
     * @param card the card to remove
     * @return true if the card was removed, false if the card was not in the hand
     * @throws IllegalArgumentException if card is null
     */
    public boolean removeCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        return hand.remove(card);
    }

    /**
     * Checks if the player is still alive in the game.
     * @return true if the player is alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the player's alive status.
     * @param alive true if the player is alive, false if eliminated
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Checks if the player has a card of the specified type.
     * @param type the type of card to check for
     * @return true if the player has at least one card of the specified type
     */
    public boolean hasCardOfType(CardType type) {
        return hand.stream()
                .anyMatch(card -> card.getType() == type);
    }

    /**
     * Removes and returns the first card of the specified type from the player's hand.
     * @param type the type of card to remove
     * @return the removed card, or null if no card of that type was found
     */
    public Card removeFirstCardOfType(CardType type) {
        Optional<Card> card = hand.stream()
                .filter(c -> c.getType() == type)
                .findFirst();
        
        if (card.isPresent()) {
            hand.remove(card.get());
            return card.get();
        }
        return null;
    }
} 