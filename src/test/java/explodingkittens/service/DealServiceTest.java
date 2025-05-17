package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.Card;
import explodingkittens.model.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InsufficientDefuseCardsException;
import explodingkittens.exceptions.TooManyPlayersException;
import explodingkittens.exceptions.InvalidPlayersListException;
import explodingkittens.exceptions.EmptyPlayersListException;
import explodingkittens.exceptions.TooFewPlayersException;
import explodingkittens.exceptions.NoDefuseCardsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

class DealServiceTest {
    private DealService dealService;

    @Mock
    private Deck mockDeck;
    
    @Mock
    private DrawService mockDrawService;

    private List<Player> players;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dealService = new DealService(mockDrawService);
        players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
    }

    @Test
    void testDealDefusesWithNullDeck() {
        // Given
        Deck deck = null;

        // When & Then
        assertThrows(InvalidDeckException.class, () -> {
            dealService.dealDefuses(deck, players);
        });
    }

    @Test
    void testDealDefusesWithEmptyDeck() {
        // Given
        when(mockDeck.isEmpty()).thenReturn(true);

        // When & Then
        assertThrows(EmptyDeckException.class, () -> {
            dealService.dealDefuses(mockDeck, players);
        });
        verify(mockDeck).isEmpty();
    }

    @Test
    void testDealDefusesWithSuccessfulDeal() {
        // Given
        when(mockDeck.isEmpty()).thenReturn(false);

        // When
        dealService.dealDefuses(mockDeck, players);

        // Then
        verify(mockDeck).isEmpty();
        
        // 验证每个玩家都收到了一张拆弹卡
        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(1, hand.size(), "Player should have exactly one card");
            assertTrue(hand.get(0) instanceof DefuseCard, "Card should be a DefuseCard");
        }
    }

    @Test
    void testDealDefusesWithTooManyPlayers() {
        // Given
        List<Player> tooManyPlayers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tooManyPlayers.add(new Player("Player" + i));
        }

        when(mockDeck.isEmpty()).thenReturn(false);

        // When & Then
        assertThrows(TooManyPlayersException.class, () -> {
            dealService.dealDefuses(mockDeck, tooManyPlayers);
        });
    }

    @Test
    void testDealDefusesWithTooFewPlayers() {
        // Given
        List<Player> singlePlayer = new ArrayList<>();
        singlePlayer.add(new Player("Player1"));

        when(mockDeck.isEmpty()).thenReturn(false);

        // When & Then
        assertThrows(TooFewPlayersException.class, () -> {
            dealService.dealDefuses(mockDeck, singlePlayer);
        });
    }

    @Test
    void testDealDefusesWithMultiplePlayers() {
        // Given
        List<Player> multiplePlayers = new ArrayList<>();
        multiplePlayers.add(new Player("Player1"));
        multiplePlayers.add(new Player("Player2"));
        multiplePlayers.add(new Player("Player3"));
        multiplePlayers.add(new Player("Player4"));

        when(mockDeck.isEmpty()).thenReturn(false);

        // When
        dealService.dealDefuses(mockDeck, multiplePlayers);

        // Then
        verify(mockDeck).isEmpty();
        
        // 验证每个玩家都收到了一张拆弹卡
        for (Player player : multiplePlayers) {
            List<Card> hand = player.getHand();
            assertEquals(1, hand.size(), "Player should have exactly one card");
            assertTrue(hand.get(0) instanceof DefuseCard, "Card should be a DefuseCard");
        }
    }

    @Test
    void testDealDefusesWithNullPlayerList() {
        // Given
        List<Player> nullPlayers = null;
        when(mockDeck.isEmpty()).thenReturn(false);

        // When & Then
        assertThrows(InvalidPlayersListException.class, () -> {
            dealService.dealDefuses(mockDeck, nullPlayers);
        });
    }

    @Test
    void testDealDefusesWithEmptyPlayerList() {
        // Given
        List<Player> emptyPlayers = new ArrayList<>();
        when(mockDeck.isEmpty()).thenReturn(false);

        // When & Then
        assertThrows(EmptyPlayersListException.class, () -> {
            dealService.dealDefuses(mockDeck, emptyPlayers);
        });
    }

    @Test
    void testDealInitialHandsWithNullDeck() {
        // Arrange
        Deck deck = null;

        // Act & Assert
        assertThrows(InvalidDeckException.class, () -> {
            dealService.dealInitialHands(deck, players);
        });
    }

    @Test
    void testDealInitialHandsWithEmptyDeck() {
        // Arrange
        when(mockDeck.isEmpty()).thenReturn(true);

        // Act & Assert
        assertThrows(EmptyDeckException.class, () -> {
            dealService.dealInitialHands(mockDeck, players);
        });
        verify(mockDeck).isEmpty();
    }

    @Test
    void testDealInitialHandsWithValidDeck() {
        // Arrange
        when(mockDeck.isEmpty()).thenReturn(false);
        Card mockCard = mock(Card.class);
        when(mockDrawService.drawCard(mockDeck)).thenReturn(mockCard);

        // Act
        dealService.dealInitialHands(mockDeck, players);

        // Assert
        verify(mockDeck).isEmpty();
        // 验证每个玩家收到4张牌
        verify(mockDrawService, times(8)).drawCard(mockDeck);
        for (Player player : players) {
            assertEquals(4, player.getHand().size(), "Each player should have 4 cards");
        }
    }

    @Test
    void testDealInitialHandsWithTooManyPlayers() {
        // Arrange
        List<Player> tooManyPlayers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tooManyPlayers.add(new Player("Player" + i));
        }
        when(mockDeck.isEmpty()).thenReturn(false);

        // Act & Assert
        assertThrows(TooManyPlayersException.class, () -> {
            dealService.dealInitialHands(mockDeck, tooManyPlayers);
        });
        verify(mockDeck).isEmpty();
    }

    @Test
    void testDealInitialHandsWithTooFewPlayers() {
        // Arrange
        List<Player> singlePlayer = new ArrayList<>();
        singlePlayer.add(new Player("Player1"));
        when(mockDeck.isEmpty()).thenReturn(false);

        // Act & Assert
        assertThrows(TooFewPlayersException.class, () -> {
            dealService.dealInitialHands(mockDeck, singlePlayer);
        });
        verify(mockDeck).isEmpty();
    }
} 