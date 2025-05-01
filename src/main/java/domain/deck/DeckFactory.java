package domain.deck;

import domain.card.*;

public class DeckFactory {
	public static Deck createDeck() {
		Deck deck = new Deck();
		
		// 添加攻击牌
		for (int i = 0; i < 4; i++) {
			deck.addCard(new AttackCard());
		}
		
		// 添加跳过牌
		for (int i = 0; i < 4; i++) {
			deck.addCard(new SkipCard());
		}
		
		// 添加洗牌牌
		for (int i = 0; i < 4; i++) {
			deck.addCard(new ShuffleCard());
		}
		
		// 添加预见未来牌
		for (int i = 0; i < 5; i++) {
			deck.addCard(new SeeTheFutureCard());
		}
		
		// 添加否决牌
		for (int i = 0; i < 5; i++) {
			deck.addCard(new NopeCard());
		}
		
		deck.shuffle();
		return deck;
	}
} 