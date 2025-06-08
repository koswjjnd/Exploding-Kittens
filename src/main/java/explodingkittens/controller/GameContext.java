package explodingkittens.controller;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;

/**
 * GameContext is a singleton class that manages the global state of the game.
 * It provides methods to access and modify game state in a controlled manner.
 * 
 * @author Your Name
 * @version 1.0
 */
public class GameContext {
	private static List<Player> turnOrder;
	private static Deck gameDeck;
	private static int currentPlayerIndex;
	private static boolean gameOver;

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
		currentPlayerIndex = 0;
		gameOver = false;
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
	 * Moves a player to the end of the turn order.
	 * @param player The player to move
	 * @throws IllegalArgumentException if the player is null or not in the turn order
	 */
	public static void movePlayerToEnd(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("Player cannot be null");
		}
		if (turnOrder == null || !turnOrder.contains(player)) {
			throw new IllegalArgumentException("Player is not in the turn order");
		}
		
		// Remove player from current position and add to end
		turnOrder.remove(player);
		turnOrder.add(player);
		
		// Adjust current player index if needed
		if (currentPlayerIndex >= turnOrder.size()) {
			currentPlayerIndex = 0;
		}
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
		return new Deck(gameDeck); // Return a copy of the deck
	}

	/**
	 * Checks if the game is over.
	 * @return true if the game is over, false otherwise
	 */
	public static boolean isGameOver() {
		if (turnOrder == null) {
			return false;
		}
		return gameOver || turnOrder.size() <= 1;
	}

	/**
	 * Gets the current player whose turn it is.
	 * @return the current player
	 * @throws IllegalStateException if the game is not properly initialized
	 */
	public static Player getCurrentPlayer() {
		if (turnOrder == null || turnOrder.isEmpty()) {
			throw new IllegalStateException("Game is not properly initialized");
		}
		Player currentPlayer = turnOrder.get(currentPlayerIndex);
		if (!currentPlayer.isAlive()) {
			// If current player is eliminated, find next alive player
			int startIndex = currentPlayerIndex;
			boolean foundAlivePlayer = false;
			
			do {
				currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
				currentPlayer = turnOrder.get(currentPlayerIndex);
				if (currentPlayer.isAlive()) {
					foundAlivePlayer = true;
					break;
				}
			} while (currentPlayerIndex != startIndex);
			
			// If no alive player is found, end the game
			if (!foundAlivePlayer) {
				endGame();
				throw new IllegalStateException("No alive players found");
			}
		}
		return currentPlayer;
	}

	/**
	 * Moves to the next player's turn.
	 * @throws IllegalStateException if the game is not properly initialized
	 */
	public static void nextTurn() {
		if (turnOrder == null || turnOrder.isEmpty()) {
			throw new IllegalStateException("Game is not properly initialized");
		}
		
		// Find the next alive player
		int startIndex = currentPlayerIndex;
		boolean foundAlivePlayer = false;
		
		do {
			currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
			if (turnOrder.get(currentPlayerIndex).isAlive()) {
				foundAlivePlayer = true;
				break;
			}
		} while (currentPlayerIndex != startIndex);
		
		// If no alive player is found, end the game
		if (!foundAlivePlayer) {
			endGame();
			throw new IllegalStateException("No alive players found");
		}
	}

	/**
	 * Removes a player from the game.
	 * @param player the player to remove
	 * @throws IllegalArgumentException if the player is null or not in the game
	 */
	public static void removePlayer(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("Player cannot be null");
		}
		if (turnOrder == null || !turnOrder.contains(player)) {
			throw new IllegalArgumentException("Player is not in the game");
		}
		turnOrder.remove(player);
		if (currentPlayerIndex >= turnOrder.size()) {
			currentPlayerIndex = 0;
		}
	}

	/**
	 * Gets the current size of the game deck.
	 * @return the number of cards remaining in the deck
	 * @throws IllegalStateException if the game deck is not initialized
	 */
	public static int getDeckSize() {
		if (gameDeck == null) {
			throw new IllegalStateException("Game deck is not initialized");
		}
		return gameDeck.size();
	}

	/**
	 * Sets the game over state.
	 * @param over true to end the game, false otherwise
	 */
	public static void setGameOver(boolean over) {
		gameOver = over;
	}

	/**
	 * Resets the game context to its initial state.
	 * This should be called when starting a new game.
	 */
	public static void reset() {
		turnOrder = null;
		gameDeck = null;
		currentPlayerIndex = 0;
		gameOver = false;
	}

	/**
	 * Sets the current player index.
	 * @param index The index to set
	 * @throws IllegalArgumentException if the index is invalid
	 * @throws IllegalStateException if the game is not properly initialized
	 * or no alive players found
	 */
	public static void setCurrentPlayerIndex(int index) {
		if (turnOrder == null || turnOrder.isEmpty()) {
			throw new IllegalStateException("Game is not properly initialized");
		}
		if (index < 0 || index >= turnOrder.size()) {
			throw new IllegalArgumentException("Invalid player index");
		}
		// First, check if the selected player is alive
		if (!turnOrder.get(index).isAlive()) {
			int startIndex = index;
			boolean foundAlivePlayer = false;
			do {
				index = (index + 1) % turnOrder.size();
				if (turnOrder.get(index).isAlive()) {
					foundAlivePlayer = true;
					break;
				}
			} while (index != startIndex);
			if (!foundAlivePlayer) {
				endGame();
				throw new IllegalStateException("No alive players found");
			}
		}
		currentPlayerIndex = index;
	}

	/**
	 * Ends the game.
	 */
	private static void endGame() {
		gameOver = true;
		// Find the last surviving player
		Player winner = null;
		for (Player player : turnOrder) {
			if (player.isAlive()) {
				winner = player;
				break;
			}
		}
		if (winner != null) {
			System.out.println("\nGame Over! " + winner.getName() + " wins!");
		} 
		else {
			System.out.println("\nGame Over! No players survived!");
		}
	}
}