package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import explodingkittens.controller.CatCardStealInputHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RainbowCatCardTest {
    private RainbowCatCard rainbowCatCard;
    private List<Player> turnOrder;
    @Mock private Player currentPlayer;
    @Mock private Player targetPlayer;
    private List<Card> currentPlayerHand;
    private List<Card> targetPlayerHand;
    @Mock private Deck gameDeck;

    @Mock
    private CatCardStealInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rainbowCatCard = new RainbowCatCard();
        turnOrder = new ArrayList<>();
        currentPlayerHand = new ArrayList<>();
        targetPlayerHand = new ArrayList<>();
        
        when(currentPlayer.getHand()).thenReturn(currentPlayerHand);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(currentPlayer.getName()).thenReturn("Current");
        when(targetPlayer.getName()).thenReturn("Target");
        when(currentPlayer.getLeftTurns()).thenReturn(1);
        
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);
        
        CatCard.setInputHandler(inputHandler);
    }

    @Test
    void testRainbowCatCardCreation() {
        assertEquals(CatType.RAINBOW_CAT, rainbowCatCard.getCatType());
        assertEquals(CardType.CAT_CARD, rainbowCatCard.getType());
    }

    @Test
    void testEffectWithNoTurnsLeft() {
        when(currentPlayer.getLeftTurns()).thenReturn(0);
        assertThrows(IllegalStateException.class, () -> 
            rainbowCatCard.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithNoTargetPlayers() {
        when(targetPlayer.getHand()).thenReturn(new ArrayList<>());
        assertThrows(IllegalStateException.class, () -> 
            rainbowCatCard.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithNoRainbowCatCards() {
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> 
            rainbowCatCard.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithNoInputHandler() {
        CatCard.setInputHandler(null);
        assertThrows(IllegalStateException.class, () -> 
            rainbowCatCard.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithOneRainbowCatCard() {
        currentPlayerHand.add(new RainbowCatCard());
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> 
            rainbowCatCard.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithTwoRainbowCatCards() {
        RainbowCatCard card1 = new RainbowCatCard();
        RainbowCatCard card2 = new RainbowCatCard();
        currentPlayerHand.add(card1);
        currentPlayerHand.add(card2);
        
        Card targetCard = new SkipCard();
        targetPlayerHand.add(targetCard);
        
        // Mock target player's state
        when(targetPlayer.isAlive()).thenReturn(true);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        RuntimeException effect = assertThrows(RuntimeException.class, () -> 
            rainbowCatCard.effect(turnOrder, gameDeck));
        
        assertEquals("Cat card effect", effect.getMessage());
        assertTrue(effect.getClass().getName().contains("CatCardEffect"));
        
        // Use reflection to access the effect's fields
        try {
            java.lang.reflect.Field firstCardField = effect.getClass().getDeclaredField("firstCard");
            java.lang.reflect.Field secondCardField = effect.getClass().getDeclaredField("secondCard");
            java.lang.reflect.Field targetPlayerNameField = effect.getClass().getDeclaredField("targetPlayerName");
            java.lang.reflect.Field targetCardIndexField = effect.getClass().getDeclaredField("targetCardIndex");
            
            firstCardField.setAccessible(true);
            secondCardField.setAccessible(true);
            targetPlayerNameField.setAccessible(true);
            targetCardIndexField.setAccessible(true);
            
            assertEquals(card1, firstCardField.get(effect));
            assertEquals(card2, secondCardField.get(effect));
            assertEquals(targetPlayer.getName(), targetPlayerNameField.get(effect));
            assertEquals(0, targetCardIndexField.get(effect));
        } catch (Exception e) {
            fail("Failed to access effect fields: " + e.getMessage());
        }
    }


} 
