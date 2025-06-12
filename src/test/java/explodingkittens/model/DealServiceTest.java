package explodingkittens.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for DealService functionality.
 */
public class DealServiceTest {
    private DealService dealService;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        dealService = new DealService();
        players = new ArrayList<>();
    }

    @Test
    void testDealDefusesWithValidPlayers() {
        // Test with 1 player
        players.add(new Player("Player1"));
        dealService.dealDefuses(players);
        assertEquals(1, players.get(0).getHand().size());
        assertTrue(players.get(0).getHand().get(0) instanceof DefuseCard);

        // Test with 4 players
        players.clear();
        for (int i = 1; i <= 4; i++) {
            players.add(new Player("Player" + i));
        }
        dealService.dealDefuses(players);
        for (Player player : players) {
            assertEquals(1, player.getHand().size());
            assertTrue(player.getHand().get(0) instanceof DefuseCard);
        }
    }

    @Test
    void testDealDefusesWithNullPlayers() {
        assertThrows(IllegalArgumentException.class, () -> {
            dealService.dealDefuses(null);
        });
    }

    @Test
    void testDealDefusesWithEmptyPlayers() {
        assertThrows(IllegalArgumentException.class, () -> {
            dealService.dealDefuses(players);
        });
    }

    @Test
    void testDealDefusesWithTooManyPlayers() {
        for (int i = 1; i <= 5; i++) {
            players.add(new Player("Player" + i));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            dealService.dealDefuses(players);
        });
    }
} 