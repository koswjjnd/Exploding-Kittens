package explodingkittens.model;

import explodingkittens.controller.ConsoleCatCardStealInputHandler;
import explodingkittens.controller.CatCardStealInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

class CatCardTest {
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player currentPlayer;
    private Player targetPlayer;
    private CatCard catCard1;
    private CatCard catCard2;
    private CatCard catCard3;
    private ConsoleCatCardStealInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        currentPlayer = new Player("Player1");
        targetPlayer = new Player("Player2");
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);

        // Create different types of cat cards
        catCard1 = new CatCard(CatType.TACOCAT);
        catCard2 = new CatCard(CatType.TACOCAT);
        catCard3 = new CatCard(CatType.BEARD_CAT);
    }

    private void setupInputHandler(String input) {
        // Create a new Scanner with the input string and UTF-8 encoding
        Scanner scanner = new Scanner(
            new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), 
            StandardCharsets.UTF_8.name());
        inputHandler = new ConsoleCatCardStealInputHandler(scanner);
        CatCard.setInputHandler(inputHandler);
    }

    @Test
    @DisplayName("Test when player has no cat cards")
    void testNoCatCards() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no cat cards");
    }

    @Test
    @DisplayName("Test when player has only one cat card")
    void testOneCatCard() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        currentPlayer.receiveCard(catCard1);
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has only one cat card");
    }

    @Test
    @DisplayName("Test when player has two different types of cat cards")
    void testDifferentCatCards() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard3);
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has two different types of cat cards");
    }

    @Test
    @DisplayName("Test when player has two same type cat cards")
    void testSameCatCards() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        targetPlayer.receiveCard(new SkipCard());
        
        try {
            catCard1.effect(turnOrder, gameDeck);
            fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            assertEquals(catCard1, effect.getFirstCard());
            assertEquals(catCard2, effect.getSecondCard());
            assertEquals(targetPlayer, effect.getTargetPlayer());
            assertEquals(0, effect.getTargetCardIndex());
        }
    }

    @Test
    @DisplayName("Test when player has multiple same type cat cards")
    void testMultipleSameCatCards() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        currentPlayer.receiveCard(new CatCard(CatType.TACOCAT));
        targetPlayer.receiveCard(new SkipCard());
        
        try {
            catCard1.effect(turnOrder, gameDeck);
            fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            assertEquals(catCard1, effect.getFirstCard());
            assertEquals(catCard2, effect.getSecondCard());
            assertEquals(targetPlayer, effect.getTargetPlayer());
            assertEquals(0, effect.getTargetCardIndex());
        }
    }

    @Test
    @DisplayName("Test when no other players are available")
    void testNoOtherPlayers() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        turnOrder.remove(targetPlayer);
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when no other players are available");
    }

    @Test
    @DisplayName("Test when target player is dead")
    void testDeadTargetPlayer() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        targetPlayer.setAlive(false);
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player is dead");
    }

    @Test
    @DisplayName("Test when target player has empty hand")
    void testEmptyTargetHand() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player has empty hand");
    }

    @Test
    @DisplayName("Test when target player has multiple cards")
    void testMultipleTargetCards() {
        setupInputHandler("1\n2\n2\n3\n"); // Provide input for both attempts
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        targetPlayer.receiveCard(new SkipCard());
        targetPlayer.receiveCard(new AttackCard());
        targetPlayer.receiveCard(new FavorCard());
        
        try {
            catCard1.effect(turnOrder, gameDeck);
            fail("Should throw CatCardEffect");
        } 
        catch (CatCard.CatCardEffect effect) {
            assertEquals(catCard1, effect.getFirstCard());
            assertEquals(catCard2, effect.getSecondCard());
            assertEquals(targetPlayer, effect.getTargetPlayer());
            assertEquals(1, effect.getTargetCardIndex());
        }
    }

    @Test
    @DisplayName("Test when player has no turns left")
    void testNoTurnsLeft() {
        setupInputHandler("1\n1\n2\n2\n"); // Provide input for both attempts
        currentPlayer.setLeftTurns(0);
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no turns left");
    }

    @Test
    @DisplayName("Test effect with no input handler")
    void testEffectWithNoInputHandler() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));

        assertThrows(IllegalStateException.class, () -> {
            card.effect(players, null);
        });
    }

    @Test
    @DisplayName("Test effect with no turns left")
    void testEffectWithNoTurnsLeft() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        Player player = new Player("Player1");
        player.setLeftTurns(0);
        players.add(player);
        players.add(new Player("Player2"));

        CatCard.setInputHandler(new ConsoleCatCardStealInputHandler(
            new Scanner(System.in, StandardCharsets.UTF_8.name())));

        assertThrows(IllegalStateException.class, () -> {
            card.effect(players, null);
        });
    }

    @Test
    @DisplayName("Test effect with no cat cards")
    void testEffectWithNoCatCards() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        Player player = new Player("Player1");
        player.setLeftTurns(1);
        players.add(player);
        players.add(new Player("Player2"));

        CatCard.setInputHandler(new ConsoleCatCardStealInputHandler(
            new Scanner(System.in, StandardCharsets.UTF_8.name())));

        assertThrows(IllegalStateException.class, () -> {
            card.effect(players, null);
        });
    }
}