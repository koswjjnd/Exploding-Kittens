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

import org.junit.jupiter.api.Assertions;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class TacoCatCardTest {
    private TacoCatCard tacoCatCard;
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
        tacoCatCard = new TacoCatCard();
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
    @DisplayName("Test TacoCatCard creation")
    void testTacoCatCardCreation() {
        Assertions.assertEquals(CatType.TACOCAT, tacoCatCard.getCatType());
        Assertions.assertEquals(CardType.CAT_CARD, tacoCatCard.getType());
    }

    @Test
    @DisplayName("Test when player has no cat cards")
    void testNoCatCards() {
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no cat cards");
    }

    @Test
    @DisplayName("Test when player has only one cat card")
    void testOneCatCard() {
        setupOneTacoCatCard();
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has only one cat card");
    }

    private void setupOneTacoCatCard() {
        TacoCatCard card = new TacoCatCard();
        currentPlayerHand.add(card);
    }

    @Test
    @DisplayName("Test when player has two different types of cat cards")
    void testDifferentCatCards() {
        setupDifferentCatCards();
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has two different types of cat cards");
    }

    private void setupDifferentCatCards() {
        TacoCatCard card1 = new TacoCatCard();
        CatCard card2 = new CatCard(CatType.BEARD_CAT);
        currentPlayerHand.add(card1);
        currentPlayerHand.add(card2);
    }

    @Test
    @DisplayName("Test when player has two same type cat cards")
    void testSameCatCards() {
        setupTwoTacoCatCards();
        
        try {
            tacoCatCard.effect(turnOrder, gameDeck);
            Assertions.fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            verifyCatCardEffect(effect);
        }
    }

    private void setupTwoTacoCatCards() {
        TacoCatCard card1 = new TacoCatCard();
        TacoCatCard card2 = new TacoCatCard();
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
        Assertions.assertEquals("Cat card effect", effect.getMessage());
        Assertions.assertTrue(effect.getClass().getName().contains("CatCardEffect"));
        
        try {
            Field firstCardField = effect.getClass().getDeclaredField("firstCard");
            Field secondCardField = effect.getClass().getDeclaredField("secondCard");
            Field targetPlayerNameField = effect.getClass().getDeclaredField("targetPlayerName");
            Field targetCardIndexField = effect.getClass().getDeclaredField("targetCardIndex");
            
            firstCardField.setAccessible(true);
            secondCardField.setAccessible(true);
            targetPlayerNameField.setAccessible(true);
            targetCardIndexField.setAccessible(true);
            
            Assertions.assertEquals(currentPlayerHand.get(0), firstCardField.get(effect));
            Assertions.assertEquals(currentPlayerHand.get(1), secondCardField.get(effect));
            Assertions.assertEquals(targetPlayer.getName(), targetPlayerNameField.get(effect));
            Assertions.assertEquals(0, targetCardIndexField.get(effect));
        } 
        catch (Exception e) {
            Assertions.fail("Failed to access effect fields: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test when no other players are available")
    void testNoOtherPlayers() {
        setupTwoTacoCatCards();
        turnOrder.remove(targetPlayer);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when no other players are available");
    }

    @Test
    @DisplayName("Test when target player is dead")
    void testDeadTargetPlayer() {
        setupTwoTacoCatCards();
        when(targetPlayer.isAlive()).thenReturn(false);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player is dead");
    }

    @Test
    @DisplayName("Test when target player has empty hand")
    void testEmptyTargetHand() {
        setupTwoTacoCatCards();
        targetPlayerHand.clear();
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player has empty hand");
    }

    @Test
    @DisplayName("Test when player has no turns left")
    void testNoTurnsLeft() {
        setupTwoTacoCatCards();
        when(currentPlayer.getLeftTurns()).thenReturn(0);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no turns left");
    }

    @Test
    @DisplayName("Test when input handler is not set")
    void testNoInputHandler() {
        setupTwoTacoCatCards();
        CatCard.setInputHandler(null);
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            tacoCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when input handler is not set");
    }

    @Test
    @DisplayName("Test with valid target player")
    void testValidTargetPlayer() {
        setupTwoTacoCatCards();
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        try {
            tacoCatCard.effect(turnOrder, gameDeck);
            Assertions.fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            verifyCatCardEffect(effect);
        }
    }

    @Test
    @DisplayName("Test with valid card index")
    void testValidCardIndex() {
        setupTwoTacoCatCards();
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        
        try {
            tacoCatCard.effect(turnOrder, gameDeck);
            Assertions.fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            verifyCatCardEffect(effect);
        }
    }

    @Test
    void testValidateTargetPlayer() {
        // Setup two taco cat cards
        setupTwoTacoCatCards();
        
        // Make target player's hand empty
        targetPlayerHand.clear();
        
        // Create a spy of the taco cat card to override getAvailableTargets
        TacoCatCard spyCard = org.mockito.Mockito.spy(tacoCatCard);
        List<Player> mockTargets = new ArrayList<>();
        mockTargets.add(targetPlayer);
        
        // Override getAvailableTargets to return our mock list
        doReturn(mockTargets).when(spyCard).getAvailableTargets(anyList(), any(Player.class));
        
        // Verify exception is thrown
        IllegalStateException exception = Assertions.assertThrows(
            IllegalStateException.class,
            () -> spyCard.effect(turnOrder, gameDeck),
            "Should throw IllegalStateException when target player has no cards"
        );
        Assertions.assertEquals("Target player has no cards", exception.getMessage());
    }
}

    