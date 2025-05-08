package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DealServiceTest {

    private DealService dealService;

    @BeforeEach
    void setUp() {
        dealService = new DealService();
    }

    @Test
    void testDealDefusesWithEmptyPlayers_throwException() {
        List<Player> players = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> dealService.dealDefuses(players));
    }
    @Test
    void testDealDefusesWithOnePlayer() {
        Player player = new Player("name1");
        List<Player> players = List.of(player);

        dealService.dealDefuses(players);

        List<Card> hand = player.getHand();
        assertEquals(1, hand.size(), "Player should receive exactly one card");
        assertTrue(hand.get(0) instanceof DefuseCard, "Card should be of type DefuseCard");
    }
    @Test
    void testDealDefusesWithMultiplePlayers() {
        Player player1 = new Player("name1");
        Player player2 = new Player("name2");
        Player player3 = new Player("name3");
        Player player4 = new Player("name4");

        List<Player> players = List.of(player1, player2, player3, player4);

        dealService.dealDefuses(players);

        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(1, hand.size(), "Each player should receive exactly one card");
            assertTrue(hand.get(0) instanceof DefuseCard, "Each card should be of type DefuseCard");
        }
    }

}
