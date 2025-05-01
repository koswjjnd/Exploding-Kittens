package domain.deck;

import domain.card.Card;
import domain.card.CardType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeckFactoryTest {
	@Test
	void testCreateDeck() {
		Deck deck = DeckFactory.createDeck();
		assertNotNull(deck);
	}

	@Test
	void testDeckCardCount() {
		Deck deck = DeckFactory.createDeck();
		int count = 0;
		while (deck.draw() != null) {
			count++;
		}
		assertEquals(22, count);
	}

	@Test
	void testCardDistribution() {
		Deck deck = DeckFactory.createDeck();
		int attackCount = 0;
		int skipCount = 0;
		int shuffleCount = 0;
		int seeTheFutureCount = 0;
		int nopeCount = 0;

		Card card;
		while ((card = deck.draw()) != null) {
			switch (card.getType()) {
				case ATTACK:
					attackCount++;
					break;
				case SKIP:
					skipCount++;
					break;
				case SHUFFLE:
					shuffleCount++;
					break;
				case SEE_THE_FUTURE:
					seeTheFutureCount++;
					break;
				case NOPE:
					nopeCount++;
					break;
			}
		}

		assertEquals(4, attackCount);
		assertEquals(4, skipCount);
		assertEquals(4, shuffleCount);
		assertEquals(5, seeTheFutureCount);
		assertEquals(5, nopeCount);
	}
} 