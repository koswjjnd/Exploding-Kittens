package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for ShuffleCard.
 */
public class ShuffleCardTest {

    private Deck deck;
    private Random fixedRandom;
    private ShuffleCard shuffleCard;
    // 因为effect需要List<Player>，我们传一个空list
    private List<Player> dummyPlayers;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        deck = new Deck();
        shuffleCard = new ShuffleCard();
        fixedRandom = new Random(42);
        // 只是为了满足参数要求
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

        Map<String, Integer> originalCounts = new HashMap<>(deck.getCardCounts());
        List<Card> originalOrder = new ArrayList<>(deck.getCards());

        // 使用 ShuffleCard 的 effect
        shuffleCard.effect(dummyPlayers, deck);

        // 检查：卡牌数量和种类不变
        assertEquals(originalCounts, deck.getCardCounts(), 
            "Cards count and types should stay the same " +
            "after shuffle");

        // 检查：顺序应该变化（概率上非常高）
        List<Card> newOrder = deck.getCards();
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
