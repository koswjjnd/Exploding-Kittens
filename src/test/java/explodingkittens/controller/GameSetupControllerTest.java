package explodingkittens.controller;

import explodingkittens.model.*;
import explodingkittens.view.GameSetupView;
import explodingkittens.service.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class GameSetupControllerTest {
    @Mock
    private GameSetupView view;
    
    @Mock
    private PlayerService playerService;
    
    @Mock
    private DealService dealService;
    
    private GameSetupController controller;

    @BeforeEach
    void setUp() {
        controller = new GameSetupController(view, playerService, dealService);
    }

    @Test
    void createPlayersWithCount2ReturnsListWith2Players() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(2);

        assertEquals(2, players.size());
    }

    @Test
    void createPlayersWithCount3ReturnsListWith3Players() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(3);

        assertEquals(3, players.size());
    }

    @Test
    void createPlayersWithCount4ReturnsListWith4Players() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3", "Player4");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        List<Player> players = controller.createPlayers(4);

        assertEquals(4, players.size());
    }

    @Test
    void createPlayersWithCount2CallsPromptNicknameTwice() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(2);

        verify(view, times(2)).promptNickname(anyInt());
    }

    @Test
    void createPlayersWithCount3CallsPromptNicknameThreeTimes() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(3);

        verify(view, times(3)).promptNickname(anyInt());
    }

    @Test
    void createPlayersWithCount4CallsPromptNicknameFourTimes() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3", "Player4");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(4);

        verify(view, times(4)).promptNickname(anyInt());
    }

    @Test
    void createPlayersWithCount2CallsCreatePlayerTwice() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(2);

        verify(playerService, times(2)).createPlayer(anyString());
    }

    @Test
    void createPlayersWithCount3CallsCreatePlayerThreeTimes() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(3);

        verify(playerService, times(3)).createPlayer(anyString());
    }

    @Test
    void createPlayersWithCount4CallsCreatePlayerFourTimes() throws Exception {
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2", "Player3", "Player4");
        when(playerService.createPlayer(anyString())).thenReturn(mock(Player.class));

        controller.createPlayers(4);

        verify(playerService, times(4)).createPlayer(anyString());
    }

    @Test
	void testInitializeTurnOrderNullListShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			controller.initializeTurnOrder(null);
		});
	}

	@Test
	void testInitializeTurnOrderEmptyListShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			controller.initializeTurnOrder(Collections.emptyList());
		});
	}

	@Test
	void testInitializeTurnOrderSinglePlayerShouldStoreCorrectOrder() {
		Player p1 = new Player("Alice");
		controller.initializeTurnOrder(List.of(p1));

		List<Player> order = GameContext.getTurnOrder();
		assertEquals(1, order.size());
		assertEquals("Alice", order.get(0).getName());
	}

	@Test
	void testInitializeTurnOrderMultiplePlayersShouldStoreAllPlayers() {
		Player p1 = new Player("A");
		Player p2 = new Player("B");
		Player p3 = new Player("C");

		List<Player> players = Arrays.asList(p1, p2, p3);
		controller.initializeTurnOrder(players);

		List<Player> result = GameContext.getTurnOrder();
		assertEquals(3, result.size());
		assertTrue(result.containsAll(players));
	}

	@Test
	void testInitializeTurnOrderContainsNullPlayerShouldThrow() {
		Player p1 = new Player("A");
		List<Player> players = Arrays.asList(p1, null);

		assertThrows(IllegalArgumentException.class, () -> {
			controller.initializeTurnOrder(players);
		});
	}

    @Test
    void testPrepareDeckWithOnePlayer() {
        // Given
        List<Player> players = Collections.singletonList(new Player("Player1"));
        
        // When & Then
        assertThrows(InvalidPlayerCountException.class, 
            () -> controller.prepareDeck(1, players),
            "Should throw InvalidPlayerCountException when player count is 1");
    }

    @Test
    void testPrepareDeckWithTwoPlayers() throws InvalidPlayerCountException {
        // Given
        List<Player> players = Arrays.asList(
            new Player("Player1"),
            new Player("Player2")
        );
        
        // Mock dealService behavior
        Mockito.doAnswer(invocation -> {
            List<Player> playersList = invocation.getArgument(1);
            for (Player player : playersList) {
                player.receiveCard(new DefuseCard());
            }
            return null;
        }).when(dealService).dealDefuses(any(Deck.class), anyList());
        
        Mockito.doAnswer(invocation -> {
            List<Player> playersList = invocation.getArgument(1);
            for (Player player : playersList) {
                for (int i = 0; i < 5; i++) {
                    player.receiveCard(new SkipCard());
                }
            }
            return null;
        }).when(dealService).dealInitialHands(any(Deck.class), anyList(), eq(5));
        
        // When
        Deck deck = controller.prepareDeck(2, players);
        
        // Then
        assertNotNull(deck, "Deck should not be null");
        assertFalse(deck.isEmpty(), "Deck should not be empty");
        
        // Verify card distribution in deck
        assertEquals(3, deck.getCardCounts().get("DefuseCard"), 
            "Deck should have 3 defuse cards for 2 players (5-2)");
        assertEquals(1, deck.getCardCounts().get("ExplodingKittenCard"), 
            "Deck should have 1 exploding kitten for 2 players");
            
        // Verify players' hands
        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(6, hand.size(), 
                "Each player should have 6 cards (1 defuse + 5 initial cards)");
            
            // Verify defuse card
            assertTrue(hand.stream()
                .anyMatch(card -> card instanceof DefuseCard),
                "Each player should have exactly one defuse card");
            assertEquals(1, hand.stream()
                .filter(card -> card instanceof DefuseCard)
                .count(),
                "Each player should have exactly one defuse card");
        }
            
        // Verify service calls
        verify(dealService).dealDefuses(any(Deck.class), anyList());
        verify(dealService).dealInitialHands(any(Deck.class), anyList(), eq(5));
    }

    @Test
    void testPrepareDeckWithFourPlayers() throws InvalidPlayerCountException {
        // Given
        List<Player> players = Arrays.asList(
            new Player("Player1"),
            new Player("Player2"),
            new Player("Player3"),
            new Player("Player4")
        );
        
        // Mock dealService behavior
        Mockito.doAnswer(invocation -> {
            List<Player> playersList = invocation.getArgument(1);
            for (Player player : playersList) {
                player.receiveCard(new DefuseCard());
            }
            return null;
        }).when(dealService).dealDefuses(any(Deck.class), anyList());
        
        Mockito.doAnswer(invocation -> {
            List<Player> playersList = invocation.getArgument(1);
            for (Player player : playersList) {
                for (int i = 0; i < 5; i++) {
                    player.receiveCard(new SkipCard());
                }
            }
            return null;
        }).when(dealService).dealInitialHands(any(Deck.class), anyList(), eq(5));
        
        // When
        Deck deck = controller.prepareDeck(4, players);
        
        // Then
        assertNotNull(deck, "Deck should not be null");
        assertFalse(deck.isEmpty(), "Deck should not be empty");
        
        // Verify card distribution in deck
        assertEquals(1, deck.getCardCounts().get("DefuseCard"), 
            "Deck should have 1 defuse card for 4 players (5-4)");
        assertEquals(3, deck.getCardCounts().get("ExplodingKittenCard"), 
            "Deck should have 3 exploding kittens for 4 players");
            
        // Verify players' hands
        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(6, hand.size(), 
                "Each player should have 6 cards (1 defuse + 5 initial cards)");
            
            // Verify defuse card
            assertTrue(hand.stream()
                .anyMatch(card -> card instanceof DefuseCard),
                "Each player should have exactly one defuse card");
            assertEquals(1, hand.stream()
                .filter(card -> card instanceof DefuseCard)
                .count(),
                "Each player should have exactly one defuse card");
        }
            
        // Verify service calls
        verify(dealService).dealDefuses(any(Deck.class), anyList());
        verify(dealService).dealInitialHands(any(Deck.class), anyList(), eq(5));
    }

    @Test
    void testPrepareDeckWithFivePlayers() {
        // Given
        List<Player> players = Arrays.asList(
            new Player("Player1"),
            new Player("Player2"),
            new Player("Player3"),
            new Player("Player4"),
            new Player("Player5")
        );
        
        // When & Then
        assertThrows(InvalidPlayerCountException.class, 
            () -> controller.prepareDeck(5, players),
            "Should throw InvalidPlayerCountException when player count is 5");
            
        // Verify no service calls were made
        verify(dealService, never()).dealDefuses(any(Deck.class), anyList());
        verify(dealService, never()).dealInitialHands(any(Deck.class), anyList(), anyInt());
    }
}