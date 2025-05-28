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
    @Test
    public void testExplodingKittenCardCreation() {
        assertEquals(CardType.EXPLODING_KITTEN, explodingKittenCard.getType());
    }

    /**
     * Test that the card can be cloned.
     */
    @Test
    public void testExplodingKittenCardClone() {
        Card clonedCard = explodingKittenCard.clone();
        assertNotNull(clonedCard);
        assertTrue(clonedCard instanceof ExplodingKittenCard);
        assertEquals(explodingKittenCard.getType(), clonedCard.getType());
    }

    /**
     * Test that the effect method exists and can be called.
     */
    @Test
    public void testExplodingKittenCardEffect() {
        explodingKittenCard.effect(playerTurnOrder, gameDeck);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testExplodingKittenCardEquals() {
        ExplodingKittenCard anotherExplodingKittenCard = new ExplodingKittenCard();
        assertTrue(explodingKittenCard.equals(anotherExplodingKittenCard));
        assertFalse(explodingKittenCard.equals(null));
        assertFalse(explodingKittenCard.equals(new AttackCard()));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testExplodingKittenCardHashCode() {
        ExplodingKittenCard anotherExplodingKittenCard = new ExplodingKittenCard();
        assertEquals(explodingKittenCard.hashCode(), anotherExplodingKittenCard.hashCode());
    }

    /**
     * Test exploding kitten effect without defuse card.
     */
    @Test
    public void testExplodingKittenWithoutDefuse() {
        assertTrue(player1.isAlive());
        player1.receiveCard(explodingKittenCard);
        explodingKittenCard.effect(playerTurnOrder, gameDeck);
        assertFalse(player1.isAlive());
    }

    /**
     * Test exploding kitten effect with defuse card.
     */
    @Test
    public void testExplodingKittenWithDefuse() {
        assertTrue(player1.isAlive());
        player1.receiveCard(new DefuseCard());
        player1.receiveCard(explodingKittenCard);
        assertTrue(player1.hasDefuse());
        assertTrue(player1.useDefuse());
        assertTrue(player1.isAlive());
        assertFalse(player1.hasDefuse());
    }

    /**
     * Test that exploding kitten is properly added to the deck.
     */
    @Test
    public void testExplodingKittenInDeck() {
        assertEquals(0, gameDeck.size());
        gameDeck.addCards(explodingKittenCard, 1);
        assertEquals(1, gameDeck.size());
        Card drawnCard = gameDeck.drawOne();
        assertTrue(drawnCard instanceof ExplodingKittenCard);
        assertEquals(0, gameDeck.size());
    }

    /**
     * Test handling multiple exploding kittens in the deck.
     */
    @Test
    public void testMultipleExplodingKittens() {
        gameDeck.addCards(explodingKittenCard, 2);
        assertEquals(2, gameDeck.size());
        Card firstCard = gameDeck.drawOne();
        assertTrue(firstCard instanceof ExplodingKittenCard);
        assertEquals(1, gameDeck.size());
        Card secondCard = gameDeck.drawOne();
        assertTrue(secondCard instanceof ExplodingKittenCard);
        assertEquals(0, gameDeck.size());
    }

    /**
     * Test exploding kitten interaction with other cards.
     */
    @Test
    public void testExplodingKittenWithOtherCards() {
        player1.receiveCard(new AttackCard());
        player1.receiveCard(new SkipCard());
        player1.receiveCard(explodingKittenCard);
        assertEquals(3, player1.getHand().size());
        explodingKittenCard.effect(playerTurnOrder, gameDeck);
        assertFalse(player1.isAlive());
    }
} 