package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for ShuffleCard.
 */
public class ShuffleCardTest {

    private Deck deck;
    private ShuffleCard shuffleCard;
    private List<Player> dummyPlayers;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        deck = new Deck();
        shuffleCard = new ShuffleCard();
        dummyPlayers = new ArrayList<>();
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
    
    /**
     * Tests that shuffling an empty deck has no effect.
     */
    @Test
    void testEffectOnEmptyDeck() {
        // 空牌堆
        shuffleCard.effect(dummyPlayers, deck);
        assertEquals(0, deck.getCardCounts().size(), 
            "Empty deck remains empty after shuffle");
    }

    /**
     * Tests that shuffling a deck with a single card has no effect.
     */
    @Test
    void testEffectOnSingleCardDeck() {
        deck.addCard(new DefuseCard());
        List<Card> originalOrder = new ArrayList<>(deck.getCards());

        shuffleCard.effect(dummyPlayers, deck);

        // 单张牌顺序保持一致
        assertEquals(originalOrder, deck.getCards(), 
            "Single card deck should remain the same after shuffle");
    }
}
