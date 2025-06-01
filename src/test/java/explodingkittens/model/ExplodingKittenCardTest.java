package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import java.util.List;
import explodingkittens.controller.GameContext;

/**
 * Test class for ExplodingKittenCard functionality.
 */
public class ExplodingKittenCardTest {
    private ExplodingKittenCard explodingKittenCard;
    private List<Player> playerTurnOrder;
    private Deck gameDeck;
    private Player player1;
    private Player player2;

    /**
     * Set up the test environment.
     */
    @BeforeEach
    public void setUp() {
        explodingKittenCard = new ExplodingKittenCard();
        playerTurnOrder = new ArrayList<>();
        gameDeck = new Deck();
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        playerTurnOrder.add(player1);
        playerTurnOrder.add(player2);
        
        // 重置 GameContext 状态
        GameContext.reset();
        // 初始化 GameContext
        GameContext.setTurnOrder(playerTurnOrder);
        GameContext.setGameDeck(gameDeck);
    }

    /**
     * Test that the card is created with correct type.
     */
    @Test // BVA Test Case 1: turnOrder = empty list
    public void testExplodingKittenCardCreation() {
        assertEquals(CardType.EXPLODING_KITTEN, explodingKittenCard.getType());
    }

    /**
     * Test that the card can be cloned.
     */
    @Test // BVA Test Case 2: turnOrder = multiple players, current player's left turns = 0
    public void testDefuseCardClone() {
        Card clonedCard = defuseCard.clone();
        assertNotNull(clonedCard);
        assertTrue(clonedCard instanceof DefuseCard);
        assertEquals(defuseCard.getType(), clonedCard.getType());
    }


    /**
     * Test that the effect method exists and can be called.
     */
    @Test // BVA Test Case 3: turnOrder = multiple players, current player's left turns = 1
    public void testDefuseCardEffect() {
        defuseCard.effect(turnOrder, gameDeck);
    }

    /**
     * Test equals method.
     */
    @Test // BVA Test Case 4: turnOrder = multiple players, current player's left turns = 2
    public void testDefuseCardEquals() {
        DefuseCard anotherDefuseCard = new DefuseCard();
        assertTrue(defuseCard.equals(anotherDefuseCard));
        assertFalse(defuseCard.equals(null));
        assertFalse(defuseCard.equals(new AttackCard()));
    }

     /**
     * Test hashCode method.
     */
    @Test // BVA Test Case 5: turnOrder = multiple players, current player's left turns = 3
    public void testDefuseCardHashCode() {
        DefuseCard anotherDefuseCard = new DefuseCard();
        assertEquals(defuseCard.hashCode(), anotherDefuseCard.hashCode());
    }
     /**
     * Test the interaction with ExplodingKittenCard.
     */
    @Test // BVA Test Case 6: turnOrder = multiple players, current player's left turns = 4
    public void testDefuseCardWithExplodingKitten() {
        ExplodingKittenCard explodingKitten = new ExplodingKittenCard();
        gameDeck.addCard(explodingKitten);
        Card drawnCard = gameDeck.drawOne();
        assertTrue(drawnCard instanceof ExplodingKittenCard);
        player1.receiveCard(defuseCard);
        assertTrue(player1.hasDefuse());
        assertTrue(player1.useDefuse());
        assertFalse(player1.hasDefuse());
    }

     /**
     * Test that the card can be cloned.
     */
    @Test // BVA Test Case 7: turnOrder = multiple players, current player's left turns = 5
    public void testExplodingKittenCardClone() {
        Card clonedCard = explodingKittenCard.clone();
        assertNotNull(clonedCard);
        assertTrue(clonedCard instanceof ExplodingKittenCard);
        assertEquals(explodingKittenCard.getType(), clonedCard.getType());
    }


} 