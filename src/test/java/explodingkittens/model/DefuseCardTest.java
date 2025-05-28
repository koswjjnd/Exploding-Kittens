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
    @Test
    public void testDefuseCardCreation() {
        assertEquals(CardType.DEFUSE, defuseCard.getType());
    }

    /**
     * Test that the card can be cloned.
     */
    @Test
    public void testDefuseCardClone() {
        Card clonedCard = defuseCard.clone();
        assertNotNull(clonedCard);
        assertTrue(clonedCard instanceof DefuseCard);
        assertEquals(defuseCard.getType(), clonedCard.getType());
    }

    /**
     * Test that the effect method exists and can be called.
     */
    @Test
    public void testDefuseCardEffect() {
        defuseCard.effect(turnOrder, gameDeck);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testDefuseCardEquals() {
        DefuseCard anotherDefuseCard = new DefuseCard();
        assertTrue(defuseCard.equals(anotherDefuseCard));
        assertFalse(defuseCard.equals(null));
        assertFalse(defuseCard.equals(new AttackCard()));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testDefuseCardHashCode() {
        DefuseCard anotherDefuseCard = new DefuseCard();
        assertEquals(defuseCard.hashCode(), anotherDefuseCard.hashCode());
    }

    /**
     * Test the interaction with ExplodingKittenCard.
     */
    @Test
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
     * Test that defuse card has no effect when no exploding kitten is drawn.
     */
    @Test
    public void testDefuseCardWithoutExplodingKitten() {
        player1.receiveCard(defuseCard);
        assertTrue(player1.hasDefuse());
        assertTrue(player1.useDefuse());
        assertFalse(player1.hasDefuse());
    }

    /**
     * Test handling multiple defuse cards.
     */
    @Test
    public void testMultipleDefuseCards() {
        DefuseCard secondDefuseCard = new DefuseCard();
        player1.receiveCard(defuseCard);
        player1.receiveCard(secondDefuseCard);
        assertTrue(player1.hasDefuse());
        assertTrue(player1.useDefuse());
        assertTrue(player1.hasDefuse());
        assertTrue(player1.useDefuse());
        assertFalse(player1.hasDefuse());
    }

    /**
     * Test defuse card interaction with other cards.
     */
    @Test
    public void testDefuseCardWithOtherCards() {
        player1.receiveCard(defuseCard);
        player1.receiveCard(new AttackCard());
        player1.receiveCard(new SkipCard());
        assertTrue(player1.hasDefuse());
        assertTrue(player1.useDefuse());
        assertFalse(player1.hasDefuse());
        assertEquals(2, player1.getHand().size());
    }
} 