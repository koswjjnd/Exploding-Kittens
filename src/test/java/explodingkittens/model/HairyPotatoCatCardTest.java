package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.*;

class HairyPotatoCatCardTest {
    private HairyPotatoCatCard hairyPotatoCatCard;
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
        hairyPotatoCatCard = new HairyPotatoCatCard();
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
    @DisplayName("Test HairyPotatoCatCard creation")
    void testHairyPotatoCatCardCreation() {
        assertEquals(CatType.HAIRY_POTATO_CAT, hairyPotatoCatCard.getCatType());
        assertEquals(CardType.CAT_CARD, hairyPotatoCatCard.getType());
    }

    @Test
    @DisplayName("Test when player has no cat cards")
    void testNoCatCards() {
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no cat cards");
    }

    @Test
    @DisplayName("Test when player has only one cat card")
    void testOneCatCard() {
        setupOneHairyPotatoCatCard();
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has only one cat card");
    }

    private void setupOneHairyPotatoCatCard() {
        HairyPotatoCatCard card = new HairyPotatoCatCard();
        currentPlayerHand.add(card);
    }

    @Test
    @DisplayName("Test when player has two different types of cat cards")
    void testDifferentCatCards() {
        setupDifferentCatCards();
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        assertThrows(IllegalStateException.class, () -> {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has two different types of cat cards");
    }

    private void setupDifferentCatCards() {
        HairyPotatoCatCard card1 = new HairyPotatoCatCard();
        CatCard card2 = new CatCard(CatType.TACOCAT);
        currentPlayerHand.add(card1);
        currentPlayerHand.add(card2);
    }

    @Test
    @DisplayName("Test when player has two same type cat cards")
    void testSameCatCards() {
        setupTwoHairyPotatoCatCards();
        
        try {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
            fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            verifyCatCardEffect(effect);
        }
    }

    private void setupTwoHairyPotatoCatCards() {
        HairyPotatoCatCard card1 = new HairyPotatoCatCard();
        HairyPotatoCatCard card2 = new HairyPotatoCatCard();
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
    @DisplayName("Test when no other players are available")
    void testNoOtherPlayers() {
        setupTwoHairyPotatoCatCards();
        turnOrder.remove(targetPlayer);
        
        assertThrows(IllegalStateException.class, () -> {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when no other players are available");
    }

    @Test
    @DisplayName("Test when target player is dead")
    void testDeadTargetPlayer() {
        setupTwoHairyPotatoCatCards();
        when(targetPlayer.isAlive()).thenReturn(false);
        
        assertThrows(IllegalStateException.class, () -> {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player is dead");
    }
     
    @Test
    @DisplayName("Test when target player has empty hand")
    void testEmptyTargetHand() {
        setupTwoHairyPotatoCatCards();
        targetPlayerHand.clear();
        
        assertThrows(IllegalStateException.class, () -> {
            hairyPotatoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player has empty hand");
    }
   


}

    