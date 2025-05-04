package explodingkittens.controller;

import explodingkittens.player.Player;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GameContextTest {

	@Test
	void testSetAndGetTurnOrder_shouldBeConsistent() {
		Player p1 = new Player("A");
		Player p2 = new Player("B");
		List<Player> input = Arrays.asList(p1, p2);

		GameContext.setTurnOrder(input);
		List<Player> result = GameContext.getTurnOrder();

		assertEquals(input, result);
	}

	@Test
	void testOverrideTurnOrder_shouldReflectNewValue() {
		Player p1 = new Player("A");
		GameContext.setTurnOrder(List.of(p1));

		Player p2 = new Player("B");
		GameContext.setTurnOrder(List.of(p2));

		List<Player> result = GameContext.getTurnOrder();
		assertEquals(1, result.size());
		assertEquals("B", result.get(0).getName());
	}

	@Test
	void testSetTurnOrder_nullInput_shouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			GameContext.setTurnOrder(null);
		});
	}

	@Test
	void testSetTurnOrder_emptyList_shouldStoreEmptyOrder() {
		GameContext.setTurnOrder(List.of());
		List<Player> result = GameContext.getTurnOrder();
		assertTrue(result.isEmpty());
	}

	@Test
	void testSetTurnOrder_containsNullPlayer_shouldIncludeNull() {
		Player p1 = new Player("A");
		List<Player> input = Arrays.asList(p1, null);
		GameContext.setTurnOrder(input);
		List<Player> result = GameContext.getTurnOrder();

		assertEquals(2, result.size());
		assertEquals(p1, result.get(0));
		assertNull(result.get(1));
	}

}
