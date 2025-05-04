package explodingkittens.controller;

import explodingkittens.player.Player;
import java.util.List;

/**
 * GameContext is a singleton class that manages the global state of the game,
 * particularly the turn order of players. It provides methods to set and
 * retrieve
 * the current turn order, ensuring proper game flow and player sequencing.
 * 
 * @author Your Name
 * @version 1.0
 */
public class GameContext {
	private static List<Player> turnOrder;

	/**
	 * Sets the turn order for the game.
	 * 
	 * @param order The list of players in the order they will take turns.
	 * @throws IllegalArgumentException if the provided order is null.
	 */
	public static void setTurnOrder(List<Player> order) {
		if (order == null) {
			throw new IllegalArgumentException("Turn order cannot be null.");
		}

		turnOrder = order;
	}

	/**
	 * Retrieves the current turn order of players.
	 * 
	 * @return A list of players representing the current turn order.
	 */
	public static List<Player> getTurnOrder() {
		return turnOrder;
	}
}