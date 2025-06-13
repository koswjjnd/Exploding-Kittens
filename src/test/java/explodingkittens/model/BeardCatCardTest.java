package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import explodingkittens.controller.CatCardStealInputHandler;

/**
 * Test class for BeardCatCard.
 */
public class BeardCatCardTest {
    private BeardCatCard card;
    private List<Player> turnOrder;
    private Deck gameDeck;
    @Mock private Player player1;
    @Mock private Player player2;
    private List<Card> player1Hand;
    private List<Card> player2Hand;
    @Mock private CatCardStealInputHandler inputHandler;

    /**
     * Mock InputHandler for testing.
     */
    private static class MockInputHandler implements CatCardStealInputHandler {
        @Override
        public Player selectTargetPlayer(List<Player> availablePlayers) {
            return availablePlayers.get(0); // Always select the first available player
        }

        @Override
        public int selectCardIndex(int handSize) {
            return 0; // Always select the first card
        }

        @Override
        public void handleCardSteal(Player currentPlayer, List<Player> turnOrder, CatType catType) {
            // Mock implementation
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        card = new BeardCatCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        player1Hand = new ArrayList<>();
        player2Hand = new ArrayList<>();
        
        when(player1.getHand()).thenReturn(player1Hand);
        when(player2.getHand()).thenReturn(player2Hand);
        when(player1.getName()).thenReturn("Player1");
        when(player2.getName()).thenReturn("Player2");
        when(player1.getLeftTurns()).thenReturn(1);
        when(player2.isAlive()).thenReturn(true);
        
        turnOrder.add(player1);
        turnOrder.add(player2);
        
        CatCard.setInputHandler(inputHandler);
    }

    @Test
    void testConstructor() {
        assertNotNull(card);
        assertEquals(CatType.BEARD_CAT, card.getCatType());
        assertEquals(CardType.CAT_CARD, card.getType());
    }

    @Test
    void testEffectWithTwoBeardCatCards() {
        // Setup
        BeardCatCard card1 = new BeardCatCard();
        BeardCatCard card2 = new BeardCatCard();
        player1Hand.add(card1);
        player1Hand.add(card2);
        
        Card targetCard = new DefuseCard();
        player2Hand.add(targetCard);
        
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(player2);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        // Execute and verify
        CatCard.CatCardEffect effect = assertThrows(CatCard.CatCardEffect.class, () -> 
            card.effect(turnOrder, gameDeck));
        
        assertEquals(player2.getName(), effect.getTargetPlayerName());
        assertEquals(player2Hand, effect.getTargetPlayerHand());
        assertEquals(0, effect.getTargetCardIndex());
        assertTrue(effect.getFirstCard() instanceof BeardCatCard);
        assertTrue(effect.getSecondCard() instanceof BeardCatCard);
    }

    @Test
    void testEffectWithDifferentCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.receiveCard(card);
        player1.receiveCard(new RainbowCatCard());
        player1.setLeftTurns(1);
        card.setInputHandler(new MockInputHandler());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithThreeBeardCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.receiveCard(card);
        player1.receiveCard(new BeardCatCard());
        player1.receiveCard(new BeardCatCard());
        player1.setLeftTurns(1);
        card.setInputHandler(new MockInputHandler());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, gameDeck));
    }

    @Test
    void testValidateInputHandler() {
        // Setup
        player1Hand.add(new BeardCatCard());
        player1Hand.add(new BeardCatCard());
        CatCard.setInputHandler(null);
        
        // Execute and verify
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> card.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when input handler is null"
        );
        assertEquals("Input handler not set", exception.getMessage());
    }

    @Test
    void testValidatePlayerTurns() {
        // Setup
        player1Hand.add(new BeardCatCard());
        player1Hand.add(new BeardCatCard());
        when(player1.getLeftTurns()).thenReturn(0);
        
        // Execute and verify
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> card.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when player has no turns left"
        );
        assertEquals("No turns left", exception.getMessage());
    }

    @Test
    void testValidateTargetPlayer() {
        // Setup
        player1Hand.add(new BeardCatCard());
        player1Hand.add(new BeardCatCard());
        
        // Make target player's hand empty
        player2Hand.clear();
        
        // Create a spy of the beard cat card to override getAvailableTargets
        BeardCatCard spyCard = spy(card);
        List<Player> mockTargets = new ArrayList<>();
        mockTargets.add(player2);
        
        // Override getAvailableTargets to return our mock list
        doReturn(mockTargets).when(spyCard).getAvailableTargets(anyList(), any(Player.class));
        
        // Mock input handler to return target player
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(player2);
        
        // Execute and verify
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> spyCard.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when target player has no cards"
        );
        assertEquals("Target player has no cards", exception.getMessage());
    }

    @Test
    void testEffectWithNoValidTargetPlayers() {
        // Setup
        BeardCatCard card1 = new BeardCatCard();
        BeardCatCard card2 = new BeardCatCard();
        player1Hand.add(card1);
        player1Hand.add(card2);
        
        // Make target player invalid (not alive)
        when(player2.isAlive()).thenReturn(false);
        
        // Execute and verify
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> card.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when no valid target players available"
        );
        assertEquals("No valid target players available", exception.getMessage());
    }
} 