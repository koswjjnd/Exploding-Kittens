package explodingkittens.controller;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;

/**
 * GameContext is a singleton class that manages the global state of the game,
 * particularly the turn order of players. It provides methods to set and
 * retrieve the current turn order, ensuring proper game flow and player
 * sequencing.
 * 
 * @author Your Name
 * @version 1.0
 */
public class GameContext {
	private static List<Player> turnOrder;
	private static Deck gameDeck;

	private GameContext() {
		// Private constructor to prevent instantiation
	}

	/**
	 * Sets the turn order for the game.
	 * 
	 * @param order The list of players in the order they will take turns.
	 * @throws IllegalArgumentException if the provided order is null, empty, or 
	 *         contains null players.
	 */
	public static void setTurnOrder(List<Player> order) {
		if (order == null) {
			throw new IllegalArgumentException("Turn order cannot be null.");
		}
		if (order.isEmpty()) {
			throw new IllegalArgumentException("Turn order cannot be empty.");
		}
		if (order.contains(null)) {
			throw new IllegalArgumentException(
				"Turn order cannot contain null players.");
		}

		turnOrder = new ArrayList<>(order);
	}

	/**
	 * Retrieves the current turn order of players.
	 * 
	 * @return An unmodifiable list of players representing the current turn order.
	 */
	public static List<Player> getTurnOrder() {
		if (turnOrder == null) {
			return null;
		}
		return Collections.unmodifiableList(new ArrayList<>(turnOrder));
	}

	/**
	 * Sets the game deck.
	 * @param deck the deck to set
	 * @throws IllegalArgumentException if the provided deck is null
	 */
	public static void setGameDeck(Deck deck) {
		if (deck == null) {
			throw new IllegalArgumentException("Game deck cannot be null.");
		}
		gameDeck = new Deck(deck); // Create a copy
	}

	/**
	 * Gets the game deck.
	 * @return a copy of the current game deck
	 */
	public static Deck getGameDeck() {
		if (gameDeck == null) {
			return null;
		}
		return new Deck(gameDeck); // Return a copy
	}
}