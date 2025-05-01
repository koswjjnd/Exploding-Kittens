package domain.card;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
	@Test
	void testCardType() {
		Card card = new AttackCard();
		assertEquals(CardType.ATTACK, card.getType());
	}
}