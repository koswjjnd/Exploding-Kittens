package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import explodingkittens.controller.CatCardStealInputHandler;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

class WatermelonCatCardTest {
    private WatermelonCatCard watermelonCatCard;
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
        watermelonCatCard = new WatermelonCatCard();
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
    void testWatermelonCatCardCreation() {
        assertEquals(CatType.WATERMELON_CAT, watermelonCatCard.getCatType());
        assertEquals(CardType.CAT_CARD, watermelonCatCard.getType());
    }



    private void setupTwoWatermelonCatCards() {
        WatermelonCatCard card1 = new WatermelonCatCard();
        WatermelonCatCard card2 = new WatermelonCatCard();
        currentPlayerHand.add(card1);
        currentPlayerHand.add(card2);
        
        Card targetCard = new SkipCard();
        targetPlayerHand.add(targetCard);
        
        when(targetPlayer.isAlive()).thenReturn(true);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
    }

    private void verifyCatCardEffect(RuntimeException effect) {
        assertEquals("Cat card effect", effect.getMessage());
        assertTrue(effect.getClass().getName().contains("CatCardEffect"));
        
        try {
            Field firstCardField = effect.getClass().getDeclaredField("firstCard");
            Field secondCardField = effect.getClass().getDeclaredField("secondCard");
            Field targetPlayerNameField = effect.getClass().getDeclaredField("targetPlayerName");
            Field targetCardIndexField = effect.getClass().getDeclaredField("targetCardIndex");
            
            firstCardField.setAccessible(true);
            secondCardField.setAccessible(true);
            targetPlayerNameField.setAccessible(true);
            targetCardIndexField.setAccessible(true);
            
            assertEquals(currentPlayerHand.get(0), firstCardField.get(effect));
            assertEquals(currentPlayerHand.get(1), secondCardField.get(effect));
            assertEquals(targetPlayer.getName(), targetPlayerNameField.get(effect));
            assertEquals(0, targetCardIndexField.get(effect));
        } 
        catch (Exception e) {
            fail("Failed to access effect fields: " + e.getMessage());
        }
    }
    @Test
    void testEffectWithNoInputHandler() {
        CatCard.setInputHandler(null);
        assertThrows(IllegalStateException.class, () ->
                watermelonCatCard.effect(turnOrder, gameDeck));
    }
    @Test
    void testEffectWithNoTurnsLeft() {
        when(currentPlayer.getLeftTurns()).thenReturn(0);
        assertThrows(IllegalStateException.class, () ->
                watermelonCatCard.effect(turnOrder, gameDeck));
    }
    
} 