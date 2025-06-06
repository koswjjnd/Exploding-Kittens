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

    
} 