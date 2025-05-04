package explodingkittens.controller;

import explodingkittens.player.Player;
import java.util.List;

/**
 * GameContext 用于存储游戏过程中的全局状态，如当前回合顺序等。
 */
public class GameContext {
	private static List<Player> turnOrder;

	public static void setTurnOrder(List<Player> order) {
		if (order == null) {
			throw new IllegalArgumentException("Turn order cannot be null.");
		}

		turnOrder = order;
	}

	public static List<Player> getTurnOrder() {
		return turnOrder;
	}
}
