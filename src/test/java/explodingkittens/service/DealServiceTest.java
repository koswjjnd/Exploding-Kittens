package explodingkittens.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.SkipCard;
import explodingkittens.model.AttackCard;
import explodingkittens.model.DefuseCard;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidPlayersListException;
import explodingkittens.exceptions.EmptyPlayersListException;
import java.util.ArrayList;
import java.util.List;

public class DealServiceTest {
    private DealService dealService;
    private Deck deck;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        dealService = new DealService();
        deck = new Deck();
        players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
    }

    // dealDefuses BVA Test Cases
    /**
     * Test Case 1: deck=null, players=2
     * Expected: InvalidDeckException
     */
    @Test
    void testDealDefusesNullDeck() {
        assertThrows(InvalidDeckException.class, 
            () -> dealService.dealDefuses(null, players),
            "Should throw InvalidDeckException when deck is null");
    }

    /**
     * Test Case 2: deck=normal, players=null
     * Expected: InvalidPlayersListException
     */
    @Test
    void testDealDefusesNullPlayers() {
        assertThrows(InvalidPlayersListException.class, 
            () -> dealService.dealDefuses(deck, null),
            "Should throw InvalidPlayersListException when players list is null");
    }

    /**
     * Test Case 3: deck=normal, players=empty
     * Expected: EmptyPlayersListException
     */
    @Test
    void testDealDefusesEmptyPlayers() {
        assertThrows(EmptyPlayersListException.class, 
            () -> dealService.dealDefuses(deck, new ArrayList<>()),
            "Should throw EmptyPlayersListException when players list is empty");
    }

    /**
     * Test Case 4: deck=normal, players=2
     * Expected: successful deal
     */
    @Test
    void testDealDefusesNormalDeck() {
        // Add enough defuse cards to the deck
        for (int i = 0; i < players.size(); i++) {
            deck.addCard(new DefuseCard());
        }
        
        dealService.dealDefuses(deck, players);
        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(1, hand.size(), "Player should have exactly one card");
            assertTrue(hand.get(0) instanceof DefuseCard, 
                "Player should have a DefuseCard");
        }
    }

    // dealInitialHands BVA Test Cases
    /**
     * Test Case 1: deck=null, players=2
     * Expected: InvalidDeckException
     */
    @Test
    void testDealInitialHandsNullDeck() {
        assertThrows(InvalidDeckException.class, 
            () -> dealService.dealInitialHands(null, players, 5),
            "Should throw InvalidDeckException when deck is null");
    }

    /**
     * Test Case 2: deck=empty, players=2
     * Expected: EmptyDeckException
     */
    @Test
    void testDealInitialHandsEmptyDeck() {
        assertThrows(EmptyDeckException.class, 
            () -> dealService.dealInitialHands(deck, players, 5),
            "Should throw EmptyDeckException when deck is empty");
    }

    /**
     * Test Case 3: deck=10 cards, players=2
     * Expected: successful deal
     */
    @Test
    void testDealInitialHandsNormalDeck() {
        // Add 10 cards to the deck (5 cards per player)
        for (int i = 0; i < 10; i++) {
            deck.addCard(new SkipCard());
        }
        
        dealService.dealInitialHands(deck, players, 5);
        
        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(5, hand.size(), "Player should have exactly 5 cards");
            for (Card card : hand) {
                assertTrue(card instanceof SkipCard, 
                    "All cards should be SkipCards");
            }
        }
    }

    /**
     * Test Case 4: deck=normal, players=null
     * Expected: InvalidPlayersListException
     */
    @Test
    void testDealInitialHandsNullPlayers() {
        assertThrows(InvalidPlayersListException.class, 
            () -> dealService.dealInitialHands(deck, null, 5),
            "Should throw InvalidPlayersListException when players list is null");
    }
} 