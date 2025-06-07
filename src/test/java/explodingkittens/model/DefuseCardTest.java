package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for DefuseCard functionality.
 */
public class DefuseCardTest {
    private DefuseCard defuseCard;
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player player1;
    private Player player2;

    /**
     * Set up the test environment.
     */
    @BeforeEach
    public void setUp() {
        // Initialize test objects
        defuseCard = new DefuseCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        turnOrder.add(player1);
        turnOrder.add(player2);
    }

    /**
     * Test that the card is created with correct type.
     */
    @Test   // BVA Test Case 1: turnOrder = empty list
    public void testDefuseCardCreation() {
        assertEquals(CardType.DEFUSE, defuseCard.getType());
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
        player1.useDefuse();
        assertFalse(player1.hasDefuse());
    }

    /**
     * Test that defuse card has no effect when no exploding kitten is drawn.
     */
    @Test // BVA Test Case 7: turnOrder = multiple players, current player's left turns = 5
    public void testDefuseCardWithoutExplodingKitten() {
        player1.receiveCard(defuseCard);
        assertTrue(player1.hasDefuse());
        player1.useDefuse();
        assertFalse(player1.hasDefuse());
    }

     /**
     * Test handling multiple defuse cards.
     */
    @Test // BVA Test Case 8: turnOrder = multiple players, current player's left turns = 6
    public void testMultipleDefuseCards() {
        DefuseCard secondDefuseCard = new DefuseCard();
        player1.receiveCard(defuseCard);
        player1.receiveCard(secondDefuseCard);
        assertTrue(player1.hasDefuse());
        player1.useDefuse();
        assertTrue(player1.hasDefuse());
        player1.useDefuse();
        assertFalse(player1.hasDefuse());
    }

    /**
     * Test defuse card interaction with other cards.
     */
    @Test // BVA Test Case 9: turnOrder = multiple players, current player's left turns = 7
    public void testDefuseCardWithOtherCards() {
        player1.receiveCard(defuseCard);
        player1.receiveCard(new AttackCard());
        player1.receiveCard(new SkipCard());
        assertTrue(player1.hasDefuse());
        player1.useDefuse();
        assertFalse(player1.hasDefuse());
        assertEquals(2, player1.getHand().size());
    }


    
} 