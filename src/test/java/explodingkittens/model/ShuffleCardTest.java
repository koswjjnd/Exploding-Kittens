package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the ShuffleCard class.
 */
public class ShuffleCardTest {
    private ShuffleCard shuffleCard;
    private List<Player> turnOrder;
    private Deck gameDeck;

    @BeforeEach
    void setUp() {
        shuffleCard = new ShuffleCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        
        // 添加一些不同的牌到牌堆中
        gameDeck.addCard(new DefuseCard());
        gameDeck.addCard(new AttackCard());
        gameDeck.addCard(new SkipCard());
        gameDeck.addCard(new ShuffleCard());
        gameDeck.addCard(new SeeTheFutureCard());
    }

    /**
     * Tests the shuffle effect changes the order of cards while preserving their count and types.
     */
    @Test
    void testEffectShufflesDeck() {
        // 添加一些不同的牌
        deck.addCard(new DefuseCard());
        deck.addCard(new AttackCard());
        deck.addCard(new SkipCard());
        deck.addCard(new ShuffleCard());
        deck.addCard(new SeeTheFutureCard());

        // 记录原始卡牌顺序
        List<Card> originalOrder = new ArrayList<>(deck.getCards());
        
        // 使用 ShuffleCard 的 effect
        shuffleCard.effect(dummyPlayers, deck);
        
        // 获取洗牌后的顺序
        List<Card> newOrder = deck.getCards();
        
        // 检查：卡牌数量不变
        assertEquals(originalOrder.size(), newOrder.size(), 
            "Number of cards should stay the same after shuffle");
            
        // 检查：每种卡牌的数量不变
        Map<CardType, Integer> originalCounts = new HashMap<>();
        Map<CardType, Integer> newCounts = new HashMap<>();
        
        for (Card card : originalOrder) {
            originalCounts.merge(card.getType(), 1, Integer::sum);
        }
        
        for (Card card : newOrder) {
            newCounts.merge(card.getType(), 1, Integer::sum);
        }
        
        assertEquals(originalCounts, newCounts,
            "Card type counts should stay the same after shuffle");
            
        // 检查：顺序应该变化（概率上非常高）
        assertNotEquals(originalOrder, newOrder, 
            "ShuffleCard effect should change the order of cards");
    }

    @Test
    void testGetType() {
        assertEquals(CardType.SHUFFLE, shuffleCard.getType());
    }

    @Test
    void testEffect() {
        // 记录初始牌序
        List<Card> initialOrder = new ArrayList<>(gameDeck.getCards());
        
        // 执行洗牌效果
        shuffleCard.effect(turnOrder, gameDeck);
        
        // 验证牌序已改变
        List<Card> newOrder = gameDeck.getCards();
        assertNotEquals(initialOrder, newOrder);
        
        // 验证牌的数量和种类保持不变
        assertEquals(initialOrder.size(), newOrder.size());
        for (Card card : initialOrder) {
            assertTrue(newOrder.contains(card));
        }
    }
}
