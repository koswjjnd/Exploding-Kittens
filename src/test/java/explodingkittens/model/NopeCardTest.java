package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the NopeCard class.
 */
public class NopeCardTest {
    private NopeCard nopeCard;
    private List<Player> turnOrder;
    private Deck gameDeck;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        nopeCard = new NopeCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
    }

    /**
     * Tests the getType method of NopeCard.
     */
    @Test
    void testGetType() {
        assertEquals(CardType.NOPE, nopeCard.getType());
    }

    /**
     * Tests the effect method of NopeCard.
     */
    @Test
    void testEffect() {
        // 添加测试玩家
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        turnOrder.add(player1);
        turnOrder.add(player2);

        // 测试效果
        nopeCard.effect(turnOrder, gameDeck);
        
        // 验证效果
        assertEquals(1, player1.getLeftTurns());
        assertEquals(1, player2.getLeftTurns());
    }
} 