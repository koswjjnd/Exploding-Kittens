package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.Card;
import explodingkittens.player.Player;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(mockDeck.isEmpty()).thenReturn(true);

        // When & Then
        assertThrows(EmptyDeckException.class, () -> {
            dealService.dealDefuses(mockDeck, players);
        });
        verify(mockDeck).isEmpty();
    }

    @Test
    void testDealDefuses_InsufficientDefuseCards() {
        // Given
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 1);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When & Then
        assertThrows(InsufficientDefuseCardsException.class, () -> {
            dealService.dealDefuses(mockDeck, players);
        });
        verify(mockDeck).isEmpty();
        verify(mockDeck).getCardCounts();
    }

    @Test
    void testDealDefuses_SuccessfulDeal() {
        // Given
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 2);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When
        dealService.dealDefuses(mockDeck, players);

        // Then
        verify(mockDeck).isEmpty();
        verify(mockDeck).getCardCounts();
        
        // 验证每个玩家都收到了一张拆弹卡
        for (Player player : players) {
            List<Card> hand = player.getHand();
            assertEquals(1, hand.size(), "Player should have exactly one card");
            assertTrue(hand.get(0) instanceof DefuseCard, "Card should be a DefuseCard");
        }
    }

    @Test
    void testDealDefuses_MultiplePlayers() {
        // Given
        List<Player> multiplePlayers = new ArrayList<>();
        multiplePlayers.add(new Player("Player1"));
        multiplePlayers.add(new Player("Player2"));
        multiplePlayers.add(new Player("Player3"));
        multiplePlayers.add(new Player("Player4"));

        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 4);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When
        dealService.dealDefuses(mockDeck, multiplePlayers);

        // Then
        verify(mockDeck).isEmpty();
        verify(mockDeck).getCardCounts();
        
        // 验证每个玩家都收到了一张拆弹卡
        for (Player player : multiplePlayers) {
            List<Card> hand = player.getHand();
            assertEquals(1, hand.size(), "Player should have exactly one card");
            assertTrue(hand.get(0) instanceof DefuseCard, "Card should be a DefuseCard");
        }
    }

    @Test
    void testDealDefuses_TooManyPlayers() {
        // Given
        List<Player> tooManyPlayers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tooManyPlayers.add(new Player("Player" + i));
        }

        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 4);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When & Then
        assertThrows(TooManyPlayersException.class, () -> {
            dealService.dealDefuses(mockDeck, tooManyPlayers);
        });
    }

    @Test
    void testDealDefuses_NullPlayerList() {
        // Given
        List<Player> nullPlayers = null;
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 1);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When & Then
        assertThrows(InvalidPlayersListException.class, () -> {
            dealService.dealDefuses(mockDeck, nullPlayers);
        });
    }

    @Test
    void testDealDefuses_EmptyPlayerList() {
        // Given
        List<Player> emptyPlayers = new ArrayList<>();
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 1);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When & Then
        assertThrows(EmptyPlayersListException.class, () -> {
            dealService.dealDefuses(mockDeck, emptyPlayers);
        });
    }

    @Test
    void testDealDefuses_TooFewPlayers() {
        // Given
        List<Player> singlePlayer = new ArrayList<>();
        singlePlayer.add(new Player("Player1"));

        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 1);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When & Then
        assertThrows(TooFewPlayersException.class, () -> {
            dealService.dealDefuses(mockDeck, singlePlayer);
        });
    }

    @Test
    void testDealDefuses_NoDefuseCards() {
        // Given
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 0);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When & Then
        assertThrows(NoDefuseCardsException.class, () -> {
            dealService.dealDefuses(mockDeck, players);
        });
        verify(mockDeck).isEmpty();
        verify(mockDeck).getCardCounts();
    }

    @Test
    void dealInitialHands_WithNullDeck_ShouldThrowInvalidDeckException() {
        // Arrange
        Deck deck = null;

        // Act & Assert
        assertThrows(InvalidDeckException.class, () -> {
            dealService.dealInitialHands(deck, players);
        });
    }

    @Test
    void dealInitialHands_WithEmptyDeck_ShouldThrowEmptyDeckException() {
        // Arrange
        when(mockDeck.isEmpty()).thenReturn(true);

        // Act & Assert
        assertThrows(EmptyDeckException.class, () -> {
            dealService.dealInitialHands(mockDeck, players);
        });
        verify(mockDeck).isEmpty();
    }

    @Test
    void dealInitialHands_WithValidDeck_ShouldDealCardsSuccessfully() {
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
} 