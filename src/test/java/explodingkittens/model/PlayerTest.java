package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer");
    }

    @Test
    void testGetLeftTurnsDefault() {
        // Test Case 1: New player with default turns
        assertEquals(1, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsZero() {
        // Test Case 2: Player with 0 turns left
        player.setLeftTurns(0);
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsMultiple() {
        // Test Case 3: Player with multiple turns left
        player.setLeftTurns(3);
        assertEquals(3, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsAfterSkip() {
        // Test Case 4: Player after using skip card
        player.setLeftTurns(1);
        player.useSkipCard();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testGetLeftTurnsAfterAttack() {
        // Test Case 5: Player after using attack card
        player.setLeftTurns(1);
        player.getAttacked();
        assertEquals(3, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsFromOne() {
        // Test Case 6: Player with 1 turn left
        player.decrementLeftTurns();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsFromZero() {
        // Test Case 7: Player with 0 turns left
        player.setLeftTurns(0);
        player.decrementLeftTurns();
        assertEquals(0, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsFromMultiple() {
        // Test Case 8: Player with multiple turns left
        player.setLeftTurns(3);
        player.decrementLeftTurns();
        assertEquals(2, player.getLeftTurns());
    }

    @Test
    void testDecrementLeftTurnsNewPlayer() {
        // Test Case 9: New player (default 1 turn)
        player.decrementLeftTurns();
        assertEquals(0, player.getLeftTurns());
    }

} 