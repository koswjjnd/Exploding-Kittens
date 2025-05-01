package domain.card;

public abstract class Card {
	protected final CardType type;

	protected Card(CardType type) {
		this.type = type;
	}

	public CardType getType() {
		return type;
	}
} 