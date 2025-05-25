package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import java.util.ArrayList;
import java.util.List;

class GameContextTest {
	private List<Player> players;
	private Deck deck;

	@BeforeEach
	void setUp() {
		// Create test players
		players = new ArrayList<>();
		players.add(new Player("Player1"));
		players.add(new Player("Player2"));
		players.add(new Player("Player3"));

		// Create test deck
		deck = new Deck();
		deck.initializeBaseDeck(3);
		
		// Reset GameContext before each test
		GameContext.reset();
	}

	@Test
	void testSetAndGetTurnOrder() {
		GameContext.setTurnOrder(players);
		List<Player> retrievedOrder = GameContext.getTurnOrder();
		
		assertNotNull(retrievedOrder);
		assertEquals(players.size(), retrievedOrder.size());
		assertTrue(retrievedOrder.containsAll(players));
	}

	@Test
	void testSetAndGetGameDeck() {
		GameContext.setGameDeck(deck);
		Deck retrievedDeck = GameContext.getGameDeck();
		
		assertNotNull(retrievedDeck);
		assertEquals(deck.size(), retrievedDeck.size());
	}

	@Test
	void testIsGameOver() {
		GameContext.setTurnOrder(players);
		assertFalse(GameContext.isGameOver());

		// Test with one player
		List<Player> singlePlayer = new ArrayList<>();
		singlePlayer.add(new Player("Single"));
		GameContext.setTurnOrder(singlePlayer);
		assertTrue(GameContext.isGameOver());

		// Test empty list should throw exception
		assertThrows(IllegalArgumentException.class, () -> 
			GameContext.setTurnOrder(new ArrayList<>()));
	}

	@Test
	void testGetCurrentPlayer() {
		GameContext.setTurnOrder(players);
		Player currentPlayer = GameContext.getCurrentPlayer();
		
		assertNotNull(currentPlayer);
		assertEquals(players.get(0), currentPlayer);
	}

	@Test
	void testNextTurn() {
		GameContext.setTurnOrder(players);
		
		// First player
		assertEquals(players.get(0), GameContext.getCurrentPlayer());
		
		// Second player
		GameContext.nextTurn();
		assertEquals(players.get(1), GameContext.getCurrentPlayer());
		
		// Third player
		GameContext.nextTurn();
		assertEquals(players.get(2), GameContext.getCurrentPlayer());
		
		// Back to first player
		GameContext.nextTurn();
		assertEquals(players.get(0), GameContext.getCurrentPlayer());
	}

	@Test
	void testRemovePlayer() {
		GameContext.setTurnOrder(players);
		Player playerToRemove = players.get(1);
		
		GameContext.removePlayer(playerToRemove);
		List<Player> remainingPlayers = GameContext.getTurnOrder();
		
		assertEquals(2, remainingPlayers.size());
		assertFalse(remainingPlayers.contains(playerToRemove));
	}

	@Test
	void testGetDeckSize() {
		GameContext.setGameDeck(deck);
		assertEquals(deck.size(), GameContext.getDeckSize());
	}

	@Test
	void testSetGameOver() {
		GameContext.setTurnOrder(players);
		assertFalse(GameContext.isGameOver());
		
		GameContext.setGameOver(true);
		assertTrue(GameContext.isGameOver());
	}

	@Test
	void testInvalidTurnOrder() {
		assertThrows(IllegalArgumentException.class, 
			() -> GameContext.setTurnOrder(null));
		assertThrows(IllegalArgumentException.class, 
			() -> GameContext.setTurnOrder(new ArrayList<>()));
	}

	@Test
	void testInvalidGameDeck() {
		assertThrows(IllegalArgumentException.class, () -> GameContext.setGameDeck(null));
	}

	@Test
	void testReset() {
		// Setup initial state
		GameContext.setTurnOrder(players);
		GameContext.setGameDeck(deck);
		GameContext.setGameOver(true);
		
		// Reset
		GameContext.reset();
		
		// Verify state is reset
		assertNull(GameContext.getTurnOrder());
		assertNull(GameContext.getGameDeck());
		assertFalse(GameContext.isGameOver());
	}

	@Test
	void testResetAfterGameOver() {
		// Setup initial state
		GameContext.setTurnOrder(players);
		GameContext.setGameDeck(deck);
		GameContext.setGameOver(true);
		
		// Reset
		GameContext.reset();
		
		// Verify game is ready for new game
		assertFalse(GameContext.isGameOver());
		assertNull(GameContext.getTurnOrder());
		assertNull(GameContext.getGameDeck());
	}
}