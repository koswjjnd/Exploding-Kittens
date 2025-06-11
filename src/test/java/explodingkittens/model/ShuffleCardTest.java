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
