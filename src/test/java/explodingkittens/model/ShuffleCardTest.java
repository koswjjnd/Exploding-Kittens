package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ShuffleCardTest {

    private Deck deck;
    private Random fixedRandom;
    private ShuffleCard shuffleCard;
    private List<Player> dummyPlayers; // 因为effect需要List<Player>，我们传一个空list

    @BeforeEach
    void setUp() {
        deck = new Deck();
        shuffleCard = new ShuffleCard();
        fixedRandom = new Random(42);
        dummyPlayers = new ArrayList<>(); // 只是为了满足参数要求
    }

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
        assertEquals(originalCounts, deck.getCardCounts(), "Cards count and types should stay the same after shuffle");

        // 检查：顺序应该变化（概率上非常高）
        List<Card> newOrder = deck.getCards();
        assertNotEquals(originalOrder, newOrder, "ShuffleCard effect should change the order of cards");
    }

    
}
