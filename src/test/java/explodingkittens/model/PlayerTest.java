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

} 