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

} 
