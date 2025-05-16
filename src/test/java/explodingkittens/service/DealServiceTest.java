package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.Card;
import explodingkittens.player.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InsufficientDefuseCardsException;
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
    void testDealDefuses_EmptyPlayerList() {
        // Given
        List<Player> emptyPlayers = new ArrayList<>();
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("DefuseCard", 1);
        when(mockDeck.isEmpty()).thenReturn(false);
        when(mockDeck.getCardCounts()).thenReturn(cardCounts);

        // When
        dealService.dealDefuses(mockDeck, emptyPlayers);

        // Then
        verify(mockDeck).isEmpty();
        verify(mockDeck).getCardCounts();
        
        // 验证没有玩家收到卡牌
        assertTrue(emptyPlayers.isEmpty(), "Player list should remain empty");
    }
} 