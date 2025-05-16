package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.player.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DealServiceTest {
    private DealService dealService;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        dealService = new DealService();
        players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
    }

    @Test
    void testDealDefuses_NullDeck() {
        // Given
        Deck deck = null;

        // When & Then
        assertThrows(InvalidDeckException.class, () -> {
            dealService.dealDefuses(deck, players);
        });
    }

    @Test
    void testDealDefuses_EmptyDeck() {
        // Given
        Deck deck = new Deck();

        // When & Then
        assertThrows(EmptyDeckException.class, () -> {
            dealService.dealDefuses(deck, players);
        });
    }
} 