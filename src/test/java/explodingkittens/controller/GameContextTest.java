package explodingkittens.controller;

import explodingkittens.player.Player;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameContextTest {

	@Test
	void testSetAndGetTurnOrderShouldBeConsistent() {
		Player p1 = new Player("A");
		Player p2 = new Player("B");
		List<Player> input = Arrays.asList(p1, p2);

		GameContext.setTurnOrder(input);
		List<Player> result = GameContext.getTurnOrder();

		assertEquals(input, result);
	}

	@Test
	void testOverrideTurnOrderShouldReflectNewValue() {
		Player p1 = new Player("A");
		GameContext.setTurnOrder(Arrays.asList(p1));

		Player p2 = new Player("B");
		GameContext.setTurnOrder(Arrays.asList(p2));

		List<Player> result = GameContext.getTurnOrder();
		assertEquals(1, result.size());
		assertEquals("B", result.get(0).getName());
	}

	@Test
	void testSetTurnOrderNullInputShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			GameContext.setTurnOrder(null);
		});
	}

	@Test
	void testSetTurnOrderEmptyListShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			GameContext.setTurnOrder(List.of());
		});
	}

	@Test
	void testSetTurnOrderContainsNullPlayerShouldThrow() {
		Player p1 = new Player("A");
		List<Player> input = Arrays.asList(p1, null);
		assertThrows(IllegalArgumentException.class, () -> {
			GameContext.setTurnOrder(input);
		});
	}

}