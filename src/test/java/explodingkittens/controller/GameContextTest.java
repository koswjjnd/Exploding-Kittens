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
		GameContext.setTurnOrder(List.of(p1));

		Player p2 = new Player("B");
		GameContext.setTurnOrder(List.of(p2));

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
	void testSetTurnOrderEmptyListShouldStoreEmptyOrder() {
		GameContext.setTurnOrder(List.of());
		List<Player> result = GameContext.getTurnOrder();
		assertTrue(result.isEmpty());
	}

	@Test
	void testSetTurnOrderContainsNullPlayerShouldIncludeNull() {
		Player p1 = new Player("A");
		List<Player> input = Arrays.asList(p1, null);
		GameContext.setTurnOrder(input);
		List<Player> result = GameContext.getTurnOrder();

		assertEquals(2, result.size());
		assertEquals(p1, result.get(0));
		assertNull(result.get(1));
	}

}
