package explodingkittens.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import explodingkittens.model.BasicCard;
import explodingkittens.model.Player;
import java.util.ArrayList;
import java.util.List;

public class NopeServiceTest {
    private NopeService nopeService;
    private Player player1;
    private BasicCard nopeCard;
    

    @BeforeEach
    void setUp() {
        nopeService = new NopeService();
        player1 = new Player("Player1");
        nopeCard = new BasicCard("Nope");
    }

    @Test
    void testIsNegatedWithNoNopeCards() {
        assertFalse(nopeService.isNegated(player1, new ArrayList<>()));
    }

    @Test
    void testIsNegatedWithOneNopeCard() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        assertTrue(nopeService.isNegated(player1, nopeCards));
    }

    @Test
    void testIsNegatedWithMultipleNopeCards() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        nopeCards.add(nopeCard);
        assertFalse(nopeService.isNegated(player1, nopeCards));
    }

    @Test
    void testIsNegatedWithNullPlayer() {
        List<BasicCard> nopeCards = new ArrayList<>();
        nopeCards.add(nopeCard);
        assertThrows(IllegalArgumentException.class, 
            () -> nopeService.isNegated(null, nopeCards));
    }

    @Test
    void testIsNegatedWithNullNopeCards() {
        assertThrows(IllegalArgumentException.class, 
            () -> nopeService.isNegated(player1, null));
    }
} 