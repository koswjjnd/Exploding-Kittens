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

    
}
