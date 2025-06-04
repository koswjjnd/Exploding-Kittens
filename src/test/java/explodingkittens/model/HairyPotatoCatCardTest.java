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

}

    