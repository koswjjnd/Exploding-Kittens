package explodingkittens.model;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import explodingkittens.model.Card;
import explodingkittens.model.DefuseCard;

public class DealServiceTest {
    
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private DealService dealService;
    
    @BeforeEach
    void setUp() {
        player1 = createMock(Player.class);
        player2 = createMock(Player.class);
        player3 = createMock(Player.class);
        player4 = createMock(Player.class);
        player5 = createMock(Player.class);
        dealService = new DealService();
    }
    
    @Test
    void testDealDefusesWithEmptyPlayers() {
        List<Player> players = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> dealService.dealDefuses(players));
    }
    
   
} 