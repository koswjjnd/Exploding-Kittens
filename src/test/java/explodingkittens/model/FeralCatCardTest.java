package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import explodingkittens.controller.CatCardStealInputHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyList;

class FeralCatCardTest {
    private FeralCatCard feralCatCard;
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
        feralCatCard = new FeralCatCard();
        turnOrder = new ArrayList<>();
        currentPlayerHand = new ArrayList<>();
        targetPlayerHand = new ArrayList<>();
        
        when(currentPlayer.getHand()).thenReturn(currentPlayerHand);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(currentPlayer.getName()).thenReturn("Current");
        when(targetPlayer.getName()).thenReturn("Target");
        when(currentPlayer.getLeftTurns()).thenReturn(1);
        when(targetPlayer.isAlive()).thenReturn(true);
        
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);
        
        CatCard.setInputHandler(inputHandler);
    }

    @Test
    void testFeralCatCardCreation() {
        assertEquals(CatType.FERAL_CAT, feralCatCard.getCatType());
        assertEquals(CardType.CAT_CARD, feralCatCard.getType());
    }

    @Test
    void testFindCatCardPairWithValidCards() {
        // Add a TacoCat and a FeralCat to the hand
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());

        CatCard[] pair = feralCatCard.findCatCardPair(currentPlayerHand);
        assertTrue(pair[0] instanceof TacoCatCard);
        assertTrue(pair[1] instanceof FeralCatCard);
    }
    
    @Test
    void testFindCatCardPairWithNoOtherCatCard() {
        // Add only FeralCat cards
        currentPlayerHand.add(new FeralCatCard());
        currentPlayerHand.add(new FeralCatCard());

        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testFindCatCardPairWithNoFeralCatCard() {
        // Add only other cat cards
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new TacoCatCard());

        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testFindCatCardPairWithEmptyHand() {
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testFindCatCardPairWithNonCatCards() {
        // Add non-cat cards
        currentPlayerHand.add(new DefuseCard());
        currentPlayerHand.add(new SkipCard());

        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testFindCatCardPairWithMixedCards() {
        // Add a mix of cards including one non-Feral cat and one Feral cat
        currentPlayerHand.add(new DefuseCard());
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new SkipCard());
        currentPlayerHand.add(new FeralCatCard());

        CatCard[] pair = feralCatCard.findCatCardPair(currentPlayerHand);
        assertTrue(pair[0] instanceof TacoCatCard);
        assertTrue(pair[1] instanceof FeralCatCard);
    }

    @Test
    void testEffectWithValidInput() {
        // Setup
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        targetPlayerHand.add(new DefuseCard());
        
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(targetPlayerHand.size())).thenReturn(0);
        
        // Execute and verify
        CatCard.CatCardEffect effect = assertThrows(CatCard.CatCardEffect.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });
        
        assertEquals(targetPlayer.getName(), effect.getTargetPlayerName());
        assertEquals(targetPlayerHand, effect.getTargetPlayerHand());
        assertEquals(0, effect.getTargetCardIndex());
        assertTrue(effect.getFirstCard() instanceof TacoCatCard);
        assertTrue(effect.getSecondCard() instanceof FeralCatCard);
    }

    @Test
    void testEffectWithNoValidTargets() {
        // Setup
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        turnOrder.clear();
        turnOrder.add(currentPlayer);
        
        // Execute and verify
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });
    }

    @Test
    void testEffectWithNoInputHandler() {
        // Setup
        CatCard.setInputHandler(null);
        
        // Execute and verify
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });
    }

    @Test
    void testEffectWithNoTurnsLeft() {
        // Setup
        when(currentPlayer.getLeftTurns()).thenReturn(0);
        
        // Execute and verify
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });
    }
} 

    