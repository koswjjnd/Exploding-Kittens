package explodingkittens.model;

import explodingkittens.controller.ConsoleCatCardStealInputHandler;
import explodingkittens.controller.CatCardStealInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import org.mockito.Mockito;

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
    @DisplayName("Test CatCard constructor and basic properties")
    void testCatCardConstructor() {
        CatCard card = new CatCard(CatType.TACOCAT);
        assertEquals(CatType.TACOCAT, card.getCatType());
        assertEquals(CardType.CAT_CARD, card.getType());
    }

    @Test
    @DisplayName("Test CatCardEffect exception properties")
    void testCatCardEffectException() {
        CatCard card1 = new CatCard(CatType.TACOCAT);
        CatCard card2 = new CatCard(CatType.TACOCAT);
        CatCard card3 = new CatCard(CatType.TACOCAT);
        Player target = new Player("Target");
        target.receiveCard(new SkipCard());

        CatCard.CatCardEffect effect = new CatCard.CatCardEffect(
            card1, card2, card3, target.getName(), CardType.ATTACK);

        assertEquals(card1, effect.getFirstCard());
        assertEquals(card2, effect.getSecondCard());
        assertEquals(card3, effect.getThirdCard());
        assertEquals(target.getName(), effect.getTargetPlayerName());
        assertEquals(CardType.ATTACK, effect.getRequestedCardType());
    }

    @Test
    @DisplayName("Test CatCardEffect exception with null third card")
    void testCatCardEffectExceptionWithNullThirdCard() {
        CatCard card1 = new CatCard(CatType.TACOCAT);
        CatCard card2 = new CatCard(CatType.TACOCAT);
        Player target = new Player("Target");
        target.receiveCard(new SkipCard());

        CatCard.CatCardEffect effect = new CatCard.CatCardEffect(
            card1, card2, null, target.getName(), null);

        assertEquals(card1, effect.getFirstCard());
        assertEquals(card2, effect.getSecondCard());
        assertNull(effect.getThirdCard());
        assertEquals(target.getName(), effect.getTargetPlayerName());
        assertNull(effect.getRequestedCardType());
    }

    @Test
    @DisplayName("Test when player has no cat cards")
    void testNoCatCards() {
        setupInputHandler("1\n1\n2\n2\n");
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no cat cards");
    }

    @Test
    @DisplayName("Test when player has only one cat card")
    void testOneCatCard() {
        setupInputHandler("1\n1\n2\n2\n");
        currentPlayer.receiveCard(catCard1);
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has only one cat card");
    }

    @Test
    @DisplayName("Test when player has two different types of cat cards")
    void testDifferentCatCards() {
        setupInputHandler("1\n1\n2\n2\n");
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard3);
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has two different types of cat cards");
    }

    @Test
    @DisplayName("Test when player has two same type cat cards")
    void testSameCatCards() {
        setupInputHandler("1\n1\n2\n2\n");
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
            assertEquals(targetPlayer.getName(), effect.getTargetPlayerName());
            assertEquals(targetPlayer.getHand(), effect.getTargetPlayerHand());
            assertEquals(0, effect.getTargetCardIndex());
        }
    }

    @Test
    @DisplayName("Test when player has multiple same type cat cards")
    void testMultipleSameCatCards() {
        setupInputHandler("1\n1\n2\n2\n");
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
            assertEquals(targetPlayer.getName(), effect.getTargetPlayerName());
            assertEquals(targetPlayer.getHand(), effect.getTargetPlayerHand());
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
        setupInputHandler("1\n2\n2\n3\n");
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
            assertEquals(targetPlayer.getName(), effect.getTargetPlayerName());
            assertEquals(targetPlayer.getHand(), effect.getTargetPlayerHand());
            assertEquals(1, effect.getTargetCardIndex());
        }
    }

    @Test
    @DisplayName("Test when player has no turns left")
    void testNoTurnsLeft() {
        setupInputHandler("1\n1\n2\n2\n");
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

    @Test
    @DisplayName("Test findFirstCatCard method")
    void testFindFirstCatCard() {
        // Create a list of cards
        List<Card> hand = new ArrayList<>();
        
        // Test case 1: Find matching CatCard
        CatCard matchingCard = new CatCard(CatType.TACOCAT);
        hand.add(matchingCard);
        CatCard result = catCard1.findFirstCatCard(hand);
        assertEquals(matchingCard, result, "Should find matching CatCard");
        
        // Test case 2: Find non-matching CatCard
        hand.clear();
        CatCard nonMatchingCard = new CatCard(CatType.BEARD_CAT);
        hand.add(nonMatchingCard);
        result = catCard1.findFirstCatCard(hand);
        assertNull(result, "Should not find non-matching CatCard");
        
        // Test case 3: Find non-CatCard
        hand.clear();
        SkipCard skipCard = new SkipCard();
        hand.add(skipCard);
        result = catCard1.findFirstCatCard(hand);
        assertNull(result, "Should not find non-CatCard");
        
        // Test case 4: Empty hand
        hand.clear();
        result = catCard1.findFirstCatCard(hand);
        assertNull(result, "Should return null for empty hand");
        
        // Test case 5: Multiple cards with matching CatCard
        hand.clear();
        hand.add(new SkipCard());
        hand.add(new CatCard(CatType.BEARD_CAT));
        hand.add(matchingCard);
        hand.add(new AttackCard());
        result = catCard1.findFirstCatCard(hand);
        assertEquals(matchingCard, result, "Should find first matching CatCard");
    }

    @Test
    @DisplayName("Test findSecondCatCard method")
    void testFindSecondCatCard() {
        // Create a list of cards
        List<Card> hand = new ArrayList<>();
        
        // Test case 1: Find second matching CatCard
        CatCard firstCard = new CatCard(CatType.TACOCAT);
        CatCard secondCard = new CatCard(CatType.TACOCAT);
        hand.add(firstCard);
        hand.add(new SkipCard());
        hand.add(secondCard);
        CatCard result = catCard1.findSecondCatCard(hand, firstCard);
        assertEquals(secondCard, result, "Should find second matching CatCard");
        
        // Test case 2: No second matching CatCard
        hand.clear();
        hand.add(firstCard);
        hand.add(new SkipCard());
        hand.add(new CatCard(CatType.BEARD_CAT));
        result = catCard1.findSecondCatCard(hand, firstCard);
        assertNull(result, "Should not find second matching CatCard");
        
        // Test case 3: First card not in list
        hand.clear();
        hand.add(new SkipCard());
        hand.add(secondCard);
        result = catCard1.findSecondCatCard(hand, firstCard);
        assertNull(result, "Should return null when first card not in list");
        
        // Test case 4: No cards after first card
        hand.clear();
        hand.add(firstCard);
        result = catCard1.findSecondCatCard(hand, firstCard);
        assertNull(result, "Should return null when no cards after first card");
        
        // Test case 5: Only non-matching CatCards after first card
        hand.clear();
        hand.add(firstCard);
        hand.add(new CatCard(CatType.BEARD_CAT));
        hand.add(new CatCard(CatType.RAINBOW_CAT));
        result = catCard1.findSecondCatCard(hand, firstCard);
        assertNull(result, "Should return null when only non-matching CatCards after first card");
        
        // Test case 6: Multiple matching CatCards after first card
        hand.clear();
        hand.add(firstCard);
        hand.add(new SkipCard());
        hand.add(secondCard);
        hand.add(new CatCard(CatType.TACOCAT));
        result = catCard1.findSecondCatCard(hand, firstCard);
        assertEquals(secondCard, result, "Should find first matching CatCard after first card");
    }

    @Test
    @DisplayName("Test play method")
    void testPlay() {
        // Setup
        currentPlayer.receiveCard(catCard1);
        currentPlayer.receiveCard(catCard2);
        targetPlayer.receiveCard(new SkipCard());
        setupInputHandler("1\n1\n2\n2\n");

        // Test play method
        try {
            catCard1.play(currentPlayer, turnOrder);
            fail("Should throw CatCardEffect");
        }
        catch (CatCard.CatCardEffect effect) {
            assertEquals(catCard1, effect.getFirstCard());
            assertEquals(catCard2, effect.getSecondCard());
            assertEquals(targetPlayer.getName(), effect.getTargetPlayerName());
            assertEquals(targetPlayer.getHand(), effect.getTargetPlayerHand());
            assertEquals(0, effect.getTargetCardIndex());
        }
    }

    @Test
    @DisplayName("Test play method with no input handler")
    void testPlayWithNoInputHandler() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));

        assertThrows(IllegalStateException.class, () -> {
            card.play(players.get(0), players);
        });
    }

    @Test
    @DisplayName("Test play method with no turns left")
    void testPlayWithNoTurnsLeft() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        Player player = new Player("Player1");
        player.setLeftTurns(0);
        players.add(player);
        players.add(new Player("Player2"));

        CatCard.setInputHandler(new ConsoleCatCardStealInputHandler(
            new Scanner(System.in, StandardCharsets.UTF_8.name())));

        assertThrows(IllegalStateException.class, () -> {
            card.play(player, players);
        });
    }

    @Test
    @DisplayName("Test play method with no cat cards")
    void testPlayWithNoCatCards() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> players = new ArrayList<>();
        Player player = new Player("Player1");
        player.setLeftTurns(1);
        players.add(player);
        players.add(new Player("Player2"));

        CatCard.setInputHandler(new ConsoleCatCardStealInputHandler(
            new Scanner(System.in, StandardCharsets.UTF_8.name())));

        assertThrows(IllegalStateException.class, () -> {
            card.play(player, players);
        });
    }

    @Test
    @DisplayName("Test setInputHandler method")
    void testSetInputHandler() {
        // Test setting a valid input handler
        CatCardStealInputHandler handler = new ConsoleCatCardStealInputHandler(
            new Scanner(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)), 
            StandardCharsets.UTF_8.name()));
        CatCard.setInputHandler(handler);
        
        // Test setting null input handler
        CatCard.setInputHandler(null);
    }

    @Test
    void testSelectTargetPlayerNoAvailablePlayers() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> turnOrder = new ArrayList<>();
        Player currentPlayer = new Player("A");
        turnOrder.add(currentPlayer);
        CatCard.setInputHandler(Mockito.mock(CatCardStealInputHandler.class));
        assertThrows(IllegalStateException.class, () -> card.selectTargetPlayer(turnOrder, currentPlayer));
    }

    @Test
    void testSelectTargetPlayerInvalidSelection() {
        CatCard card = new CatCard(CatType.TACOCAT);
        List<Player> turnOrder = new ArrayList<>();
        Player currentPlayer = new Player("A");
        Player other = new Player("B");
        turnOrder.add(currentPlayer);
        turnOrder.add(other);

        // mock other为活着且有手牌
        other.receiveCard(new SkipCard());

        CatCardStealInputHandler handler = Mockito.mock(CatCardStealInputHandler.class);
        Mockito.when(handler.selectTargetPlayer(Mockito.anyList())).thenReturn(null);
        CatCard.setInputHandler(handler);

        assertThrows(IllegalArgumentException.class, () -> card.selectTargetPlayer(turnOrder, currentPlayer));

        Mockito.when(handler.selectTargetPlayer(Mockito.anyList())).thenReturn(new Player("C"));
        assertThrows(IllegalArgumentException.class, () -> card.selectTargetPlayer(turnOrder, currentPlayer));
    }

    @Test
    void testSelectCardIndexInvalidIndex() {
        CatCard card = new CatCard(CatType.TACOCAT);
        Player target = Mockito.mock(Player.class);
        Mockito.when(target.getHand()).thenReturn(List.of(new SkipCard(), new AttackCard()));

        CatCardStealInputHandler handler = Mockito.mock(CatCardStealInputHandler.class);
        // too small
        Mockito.when(handler.selectCardIndex(2)).thenReturn(-1);
        CatCard.setInputHandler(handler);
        assertThrows(IllegalArgumentException.class, () -> card.selectCardIndex(target));
        // too large
        Mockito.when(handler.selectCardIndex(2)).thenReturn(2);
        assertThrows(IllegalArgumentException.class, () -> card.selectCardIndex(target));
    }

    @Test
    void testValidatePlayerTurnsNoTurnsLeft() {
        CatCard card = new CatCard(CatType.TACOCAT);
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getLeftTurns()).thenReturn(0);
        assertThrows(IllegalStateException.class, () -> card.validatePlayerTurns(player));
    }

    @Test
    void testValidateTargetPlayerEmptyHand() {
        CatCard card = new CatCard(CatType.TACOCAT);
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getHand()).thenReturn(new ArrayList<>());
        assertThrows(IllegalStateException.class, () -> card.validateTargetPlayer(player));
    }

    @Test
    void testGetRequestedCatType() {
        CatCard card1 = new CatCard(CatType.TACOCAT);
        CatCard card2 = new CatCard(CatType.TACOCAT);
        CatCard card3 = new CatCard(CatType.BEARD_CAT);

        // requestedCardType 为 CAT_CARD，requestedCatType 应为 card1.getCatType()
        CatCard.CatCardEffect effect1 = new CatCard.CatCardEffect(
            card1, card2, card3, "Player1", CardType.CAT_CARD);
        assertEquals(CatType.TACOCAT, effect1.getRequestedCatType());

        // requestedCardType 为 ATTACK，requestedCatType 应为 null
        CatCard.CatCardEffect effect2 = new CatCard.CatCardEffect(
            card1, card2, card3, "Player1", CardType.ATTACK);
        assertNull(effect2.getRequestedCatType());

        // requestedCardType 为 null，requestedCatType 应为 null
        CatCard.CatCardEffect effect3 = new CatCard.CatCardEffect(
            card1, card2, card3, "Player1", null);
        assertNull(effect3.getRequestedCatType());
    }

    @Test
    void testGetTargetPlayerHand() {
        CatCard card1 = new CatCard(CatType.TACOCAT);
        CatCard card2 = new CatCard(CatType.TACOCAT);
        Player target = new Player("Target");
        target.receiveCard(new SkipCard());

        // 构造 targetPlayerHand 不为 null 的 effect
        CatCard.CatCardEffect effect1 = new CatCard.CatCardEffect(
            card1, card2, target, 0);
        List<Card> handCopy = effect1.getTargetPlayerHand();
        assertNotNull(handCopy);
        assertEquals(target.getHand(), handCopy);
        assertNotSame(target.getHand(), handCopy);

        CatCard.CatCardEffect effect2 = new CatCard.CatCardEffect(
            card1, card2, null, "Player1", CardType.ATTACK);
        assertNull(effect2.getTargetPlayerHand());
    }
    
    @Test
    @DisplayName("Test play() hits effect() without internal exception")
    void testPlayMethodCoverageLineHit() {
        // 创建 dummy CatCard 子类，不抛异常
        CatCard card = new CatCard(CatType.TACOCAT) {
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {
            }
        };
    
        List<Player> players = new ArrayList<>();
        players.add(new Player("A"));
        players.add(new Player("B"));
    
        card.play(players.get(0), players);
    }
}