package domain.deck;

import domain.card.Card;
import domain.card.CardType;
import domain.card.AttackCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
	@Test
	void testDeckCreation() {
		Deck deck = new Deck();
		assertNotNull(deck);
	}

	@Test
	void testDeckShuffle() {
		Deck deck = new Deck();
		deck.addCard(new AttackCard());
		deck.addCard(new AttackCard());
		Deck originalDeck = new Deck();
		originalDeck.addCard(new AttackCard());
		originalDeck.addCard(new AttackCard());
		deck.shuffle();
		assertNotEquals(deck, originalDeck);
	}

	@Test
	void testDeckDraw() {
		Deck deck = new Deck();
		deck.addCard(new AttackCard());
		Card card = deck.draw();
		assertNotNull(card);
		assertEquals(CardType.ATTACK, card.getType());
	}
} 