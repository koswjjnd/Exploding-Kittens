package explodingkittens.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import explodingkittens.exceptions.EmptyDeckException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

/**
 * Test class for the Deck class.
 */
public class DeckTest {
    private Deck deck;
    private Card skipCard;
    private Card attackCard;
    private Card seeTheFutureCard;
    private Card defuseCard;
    private Card shuffleCard;
    private Card catCard;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        skipCard = new SkipCard();
        attackCard = new AttackCard();
        seeTheFutureCard = new SeeTheFutureCard();
        defuseCard = new DefuseCard();
        shuffleCard = new ShuffleCard();
        catCard = new CatCard(CatType.TACOCAT);
    }

    @Test
    void testConstructor() {
        assertNotNull(deck);
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    @Test
    void testCopyConstructor() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        Deck copyDeck = new Deck(deck);
        
        assertNotSame(deck, copyDeck);
        assertEquals(deck.size(), copyDeck.size());
        assertNotSame(deck.getCards().get(0), copyDeck.getCards().get(0));
    }

    @Test
    void testInitializeBaseDeckInvalidPlayerCount() {
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(1));
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(5));
    }

    @Test
    void testInitializeBaseDeckValidPlayerCount() {
        deck.initializeBaseDeck(2);
        Map<String, Integer> counts = deck.getCardCounts();
        
        // Debug output
        System.out.println("Actual counts:");
        counts.forEach((key, value) -> System.out.println(key + ": " + value));
        
        // Debug card types
        System.out.println("\nCard types in deck:");
        for (Card card : deck.getCards()) {
            System.out.println("Card class: " + card.getClass().getSimpleName());
            System.out.println("Card type: " + card.getType().name());
            if (card instanceof CatCard) {
                System.out.println("Cat type: " + ((CatCard) card).getCatType().name());
            }
            System.out.println("---");
        }
        
        // Check all expected cards are present
        assertEquals(3, counts.get("DefuseCard")); // 5-2
        assertEquals(2, counts.get("AttackCard"));
        assertEquals(2, counts.get("SkipCard"));
        assertEquals(2, counts.get("ShuffleCard"));
        assertEquals(2, counts.get("See_the_futureCard"));
        assertEquals(4, counts.get("NopeCard"));
        assertEquals(5, counts.get("CatCard_WATERMELON_CAT"));
        assertEquals(5, counts.get("CatCard_BEARD_CAT"));
        assertEquals(5, counts.get("CatCard_HAIRY_POTATO_CAT"));
        assertEquals(5, counts.get("CatCard_RAINBOW_CAT"));
        assertEquals(5, counts.get("CatCard_TACOCAT"));
        assertEquals(1, counts.get("SnatchCard"));
        assertEquals(1, counts.get("Switch_deck_by_halfCard"));
        assertEquals(1, counts.get("Time_rewindCard"));
        assertEquals(1, counts.get("FavorCard"));
        assertEquals(2, counts.get("Draw_from_bottomCard"));
        assertEquals(2, counts.get("ReverseCard"));
        assertEquals(2, counts.get("Super_skipCard"));
        assertEquals(2, counts.get("Double_skipCard"));
    }

    @Test
    void testAddCards() {
        deck.addCards(skipCard, 3);
        assertEquals(3, deck.size());
        
        Map<String, Integer> counts = deck.getCardCounts();
        assertEquals(3, counts.get("SkipCard"));
    }

    @Test
    void testAddCardsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addCards(null, 1));
        assertThrows(IllegalArgumentException.class, () -> deck.addCards(skipCard, -1));
    }

    @Test
    void testAddCardsBoundaryConditions() {
        // Test valid boundaries
        assertDoesNotThrow(() -> deck.addCards(skipCard, 0));  // 最小有效值
        assertDoesNotThrow(() -> deck.addCards(skipCard, 1));  // 正常值
        assertDoesNotThrow(() -> deck.addCards(skipCard, 100));  // 大数值
        
        // Test invalid boundaries
        assertThrows(IllegalArgumentException.class, () -> deck.addCards(skipCard, -1));  // 负数
        
        // Verify cards were added correctly
        deck.clear();
        deck.addCards(skipCard, 0);
        assertEquals(0, deck.size(), "Adding 0 cards should not change deck size");
        
        deck.clear();
        deck.addCards(skipCard, 1);
        assertEquals(1, deck.size(), "Adding 1 card should increase deck size by 1");
        assertEquals(skipCard, deck.getCards().get(0), "Added card should be the same as input");
        
        deck.clear();
        deck.addCards(skipCard, 100);
        assertEquals(100, deck.size(), "Adding 100 cards should increase deck size by 100");
        for (int i = 0; i < 100; i++) {
            assertEquals(skipCard, deck.getCards().get(i), "All added cards should be the same as input");
        }
    }

    @Test
    void testAddCard() {
        deck.addCard(skipCard);
        assertEquals(1, deck.size());
        assertEquals(skipCard, deck.peekTop());
    }

    @Test
    void testAddCardInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addCard(null));
    }

    @Test
    void testDrawOne() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        
        Card drawn = deck.drawOne();
        assertEquals(skipCard, drawn);
        assertEquals(1, deck.size());
    }

    @Test
    void testDrawOneEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> deck.drawOne());
    }

    @Test
    void testInsertAt() {
        deck.addCard(skipCard);
        deck.insertAt(attackCard, 0);
        
        assertEquals(attackCard, deck.drawOne());
        assertEquals(skipCard, deck.drawOne());
    }

    @Test
    void testInsertAtInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(null, 0));
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(skipCard, -1));
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(skipCard, 1));
    }

    @Test
    void testInsertAtBoundaryConditions() {
        // Setup
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        
        // Test valid positions
        assertDoesNotThrow(() -> deck.insertAt(seeTheFutureCard, 0));  // 插入到开头
        assertDoesNotThrow(() -> deck.insertAt(seeTheFutureCard, 1));  // 插入到中间
        assertDoesNotThrow(() -> deck.insertAt(seeTheFutureCard, 4));  // 插入到末尾（等于size）
        
        // Test invalid positions
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(seeTheFutureCard, -1));  // 负数位置
        assertThrows(IllegalArgumentException.class, () -> deck.insertAt(seeTheFutureCard, 6));   // 超出范围
        
        // Verify the cards were inserted at correct positions
        List<Card> cards = deck.getCards();
        assertEquals(seeTheFutureCard, cards.get(0));  // 验证插入到开头
        assertEquals(seeTheFutureCard, cards.get(1));  // 验证插入到中间
        assertEquals(seeTheFutureCard, cards.get(4));  // 验证插入到末尾
    }

    @Test
    void testPeekTop() {
        deck.addCard(skipCard);
        assertEquals(skipCard, deck.peekTop());
        assertEquals(1, deck.size());
    }

    @Test
    void testPeekTopEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> deck.peekTop());
    }

    @Test
    void testShuffle() {
        deck.addCards(skipCard, 5);
        deck.addCards(attackCard, 5);
        List<Card> beforeShuffle = new ArrayList<>(deck.getCards());
        
        deck.shuffle();
        List<Card> afterShuffle = deck.getCards();
        
        assertEquals(beforeShuffle.size(), afterShuffle.size());
        assertNotEquals(beforeShuffle, afterShuffle);
    }

    @Test
    void testShuffleWithRandom() {
        deck.addCards(skipCard, 5);
        deck.addCards(attackCard, 5);
        List<Card> beforeShuffle = new ArrayList<>(deck.getCards());
        
        deck.shuffle(new Random(42)); // Fixed seed for deterministic testing
        List<Card> afterShuffle = deck.getCards();
        
        assertEquals(beforeShuffle.size(), afterShuffle.size());
        assertNotEquals(beforeShuffle, afterShuffle);
    }

    @Test
    void testShuffleWithNullRandom() {
        deck.addCards(skipCard, 5);
        assertDoesNotThrow(() -> deck.shuffle(null));
    }

    @Test
    void testGetCards() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        
        List<Card> cards = deck.getCards();
        assertEquals(2, cards.size());
        assertNotSame(deck.getCards(), cards); // Should return a copy
    }

    @Test
    void testGetRealCards() {
        deck.addCard(skipCard);
        List<Card> realCards = deck.getRealCards();
        assertEquals(1, realCards.size());
        assertSame(deck.getRealCards(), realCards); // Should return the actual list
    }

    @Test
    void testGetUnmodifiableCards() {
        deck.addCard(skipCard);
        List<Card> unmodifiableCards = deck.getUnmodifiableCards();
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableCards.add(attackCard));
    }

    @Test
    void testRemoveTopCard() {
        deck.addCard(skipCard);
        Card removed = deck.removeTopCard();
        assertEquals(skipCard, removed);
        assertTrue(deck.isEmpty());
    }

    @Test
    void testRemoveTopCardEmptyDeck() {
        assertThrows(EmptyDeckException.class, () -> deck.removeTopCard());
    }

    @Test
    void testRemoveBottomCard() {
        deck.addCard(skipCard);
        deck.addCard(attackCard);
        Card removed = deck.removeBottomCard();
        assertEquals(attackCard, removed);
        assertEquals(1, deck.size());
    }

    @Test
    void testRemoveBottomCardEmptyDeck() {
        assertThrows(EmptyDeckException.class, () -> deck.removeBottomCard());
    }

    @Test
    void testSwitchTopAndBottomHalf() {
        deck.addCards(skipCard, 3);
        deck.addCards(attackCard, 3);
        List<Card> beforeSwitch = new ArrayList<>(deck.getCards());
        
        deck.switchTopAndBottomHalf();
        List<Card> afterSwitch = deck.getCards();
        
        assertEquals(beforeSwitch.size(), afterSwitch.size());
        assertNotEquals(beforeSwitch, afterSwitch);
    }

    @Test
    void testSwitchTopAndBottomHalfOddSize() {
        deck.addCards(skipCard, 3);
        deck.addCards(attackCard, 2);
        List<Card> beforeSwitch = new ArrayList<>(deck.getCards());
        
        deck.switchTopAndBottomHalf();
        List<Card> afterSwitch = deck.getCards();
        
        assertEquals(beforeSwitch.size(), afterSwitch.size());
        assertNotEquals(beforeSwitch, afterSwitch);
    }

    @Test
    void testSwitchTopAndBottomHalfSmallDeck() {
        deck.addCard(skipCard);
        deck.switchTopAndBottomHalf();
        assertEquals(1, deck.size());
    }

    @Test
    void testClear() {
        // Test clearing a deck with cards
        deck.addCards(skipCard, 3);
        assertEquals(3, deck.size(), "Deck should have 3 cards before clearing");
        
        deck.clear();
        assertTrue(deck.isEmpty(), "Deck should be empty after clearing");
        assertEquals(0, deck.size(), "Deck size should be 0 after clearing");
    }

    @Test
    void testClearUninitializedDeck() {
        // Create a deck with null cards list to simulate uninitialized state
        Deck uninitializedDeck = new Deck();
        try {
            java.lang.reflect.Field cardsField = Deck.class.getDeclaredField("cards");
            cardsField.setAccessible(true);
            cardsField.set(uninitializedDeck, null);
        } 
        catch (Exception e) {
            throw new RuntimeException("Failed to set cards field to null", e);
        }
        
        // Test that clearing an uninitialized deck throws IllegalStateException
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> uninitializedDeck.clear(),
            "Should throw IllegalStateException when clearing uninitialized deck"
        );
        
        assertEquals("Deck is not initialized", exception.getMessage(),
            "Exception message should indicate deck is not initialized");
    }

    @Test
    void testGetCardCounts() {
        deck.addCards(skipCard, 2);
        deck.addCards(attackCard, 3);
        deck.addCards(seeTheFutureCard, 1);
        
        Map<String, Integer> counts = deck.getCardCounts();
        
        assertEquals(2, counts.get("SkipCard"));
        assertEquals(3, counts.get("AttackCard"));
        assertEquals(1, counts.get("See_the_futureCard"));
    }

    @Test
    void testAddExplodingKittens() {
        deck.addExplodingKittens(3);
        assertEquals(3, deck.size());
        Map<String, Integer> counts = deck.getCardCounts();
        
        assertEquals(3, counts.get("Exploding_kittenCard"));
    }

    @Test
    void testAddExplodingKittensInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> deck.addExplodingKittens(-1));
    }

    @Test
    void testGetCardTypeKey() {
        assertEquals("CatCard_TACOCAT", invokeGetCardTypeKey(new CatCard(CatType.TACOCAT)));
        assertEquals("DefuseCard", invokeGetCardTypeKey(new DefuseCard()));
        assertEquals("AttackCard", invokeGetCardTypeKey(new AttackCard()));
        assertEquals("SkipCard", invokeGetCardTypeKey(new SkipCard()));
        assertEquals("ShuffleCard", invokeGetCardTypeKey(new ShuffleCard()));
        assertEquals("See_the_futureCard", invokeGetCardTypeKey(new SeeTheFutureCard()));
        assertEquals("NopeCard", invokeGetCardTypeKey(new NopeCard()));
        assertEquals("Exploding_kittenCard", invokeGetCardTypeKey(new ExplodingKittenCard()));
        assertEquals("UnknownCard", invokeGetCardTypeKey(new Card(CardType.FAVOR) {
            public Card clone() { 
                return this; 
            }
            public void effect(java.util.List<Player> t, Deck d) {
                // Empty effect
            }
        }));
    }

    private String invokeGetCardTypeKey(Card card) {
        try {
            java.lang.reflect.Method m = Deck.class.getDeclaredMethod("getCardTypeKey", Card.class);
            m.setAccessible(true);
            return (String) m.invoke(deck, card);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testValidateCatCards() {
        // ✅ Test case 1: should be valid
        Deck validDeck = new Deck();
        validDeck.initializeBaseDeck(2);
        assertTrue(validDeck.validateDeck(2)); // ✅ 一定会通过
    
        // ❌ Test case 2: should fail - only 3 cat cards each
        Deck invalidDeck = new Deck();
        invalidDeck.addCards(new DefuseCard(), 3);
        invalidDeck.addCards(new AttackCard(), 2);
        invalidDeck.addCards(new SkipCard(), 2);
        invalidDeck.addCards(new ShuffleCard(), 2);
        invalidDeck.addCards(new SeeTheFutureCard(), 2);
        invalidDeck.addCards(new NopeCard(), 4);
        invalidDeck.addCards(new WatermelonCatCard(), 3);
        invalidDeck.addCards(new BeardCatCard(), 3);
        invalidDeck.addCards(new HairyPotatoCatCard(), 3);
        invalidDeck.addCards(new RainbowCatCard(), 3);
        invalidDeck.addCards(new TacoCatCard(), 3);
        assertFalse(invalidDeck.validateDeck(2));
    
        // ❌ Test case 3: missing TacoCat
        Deck missingTypeDeck = new Deck();
        missingTypeDeck.addCards(new DefuseCard(), 3);
        missingTypeDeck.addCards(new AttackCard(), 2);
        missingTypeDeck.addCards(new SkipCard(), 2);
        missingTypeDeck.addCards(new ShuffleCard(), 2);
        missingTypeDeck.addCards(new SeeTheFutureCard(), 2);
        missingTypeDeck.addCards(new NopeCard(), 4);
        missingTypeDeck.addCards(new WatermelonCatCard(), 4);
        missingTypeDeck.addCards(new BeardCatCard(), 4);
        missingTypeDeck.addCards(new HairyPotatoCatCard(), 4);
        missingTypeDeck.addCards(new RainbowCatCard(), 4);
        assertFalse(missingTypeDeck.validateDeck(2));
    }
    
    @Test
    void testValidateDeckWithInvalidPlayerCount() {
        assertFalse(invokeValidateDeck(1));
        assertFalse(invokeValidateDeck(5));
    }

    @Test
    void testValidateDeckWithInvalidDefuseCount() {
        // Initialize deck with 2 players (should have 3 defuse cards)
        deck.initializeBaseDeck(2);
        // Remove one defuse card to make it invalid
        deck.removeTopCard();
        assertFalse(invokeValidateDeck(2));
    }

    @Test
    void testValidateDeckWithInvalidMainCards() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("DefuseCard", 3);
        counts.put("AttackCard", 2);
        counts.put("SkipCard", 2);
        counts.put("ShuffleCard", 2);
        counts.put("See_the_futureCard", 2);
        counts.put("NopeCard", 4);
        for (CatType type : CatType.values()) {
            counts.put("CatCard_" + type.name(), 5);
        }

        // Test with valid counts first
        assertTrue(invokeValidateMainCards(counts));

        // Test invalid AttackCard count
        counts.put("AttackCard", 1);
        assertFalse(invokeValidateMainCards(counts));
        counts.put("AttackCard", 2); // Reset

        // Test invalid SkipCard count
        counts.put("SkipCard", 1);
        assertFalse(invokeValidateMainCards(counts));
        counts.put("SkipCard", 2); // Reset

        // Test invalid ShuffleCard count
        counts.put("ShuffleCard", 1);
        assertFalse(invokeValidateMainCards(counts));
        counts.put("ShuffleCard", 2); // Reset

        // Test invalid SeeTheFutureCard count
        counts.put("See_the_futureCard", 1);
        assertFalse(invokeValidateMainCards(counts));
        counts.put("See_the_futureCard", 2); // Reset

        // Test invalid NopeCard count
        counts.put("NopeCard", 3);
        assertFalse(invokeValidateMainCards(counts));
    }

    @Test
    void testValidateDeckWithInvalidCatCards() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("DefuseCard", 3);
        counts.put("AttackCard", 3);
        counts.put("SkipCard", 3);
        counts.put("ShuffleCard", 4);
        counts.put("SeeTheFutureCard", 4);
        counts.put("NopeCard", 4);

        // Test with invalid cat card count
        for (CatType type : CatType.values()) {
            counts.put("CatCard_" + type.name(), 4); // Should be 5
        }
        assertFalse(invokeValidateCatCards(counts));

        // Test with missing cat type
        counts.remove("CatCard_TACOCAT");
        assertFalse(invokeValidateCatCards(counts));
    }

    @Test
    void testValidateDeckWithValidDeck() {
        // Initialize the deck with valid cards
        deck.initializeBaseDeck(2);
        assertTrue(invokeValidateDeck(2));
    }

    @Test
    void testValidateDeckMainCardsBranch() {
        // Initialize deck with 2 players
        deck.initializeBaseDeck(2);
        
        // Remove one attack card to make validateMainCards return false
        for (int i = 0; i < deck.size(); i++) {
            if (deck.getCards().get(i) instanceof AttackCard) {
                deck.getRealCards().remove(i);
                break;
            }
        }
        
        // This should trigger the validateMainCards branch in validateDeck
        assertFalse(deck.validateDeck(2));
    }

    @Test
    void testInitializeBaseDeckBoundaryConditions() {
        // Test valid boundaries
        assertDoesNotThrow(() -> deck.initializeBaseDeck(2)); 
        assertDoesNotThrow(() -> deck.initializeBaseDeck(3)); 
        assertDoesNotThrow(() -> deck.initializeBaseDeck(4)); 
        
        // Test invalid boundaries
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(1)); 
        assertThrows(IllegalArgumentException.class, () -> deck.initializeBaseDeck(5)); 
        
        // Verify deck is initialized correctly for valid player counts
        deck.initializeBaseDeck(2);
        assertFalse(deck.isEmpty());
        assertEquals(3, deck.getCardCounts().get("DefuseCard"));  // 5-2 = 3 defuse cards
        
        deck.clear();
        deck.initializeBaseDeck(4);
        assertFalse(deck.isEmpty());
        assertEquals(1, deck.getCardCounts().get("DefuseCard"));  // 5-4 = 1 defuse card
    }

    @Test
    void testInitializeBaseDeckClearsExistingCards() {
        // Setup: Add some cards to the deck
        deck.addCard(skipCard);
        deck.addCard(skipCard);
        deck.addCard(skipCard);
        assertEquals(3, deck.size(), "Deck should have 3 cards initially");
        
        // Initialize deck with 2 players
        deck.initializeBaseDeck(2);
        
        // Verify that old cards were cleared and new cards were added
        assertNotEquals(3, deck.size(), "Deck size should have changed");
        Map<String, Integer> counts = deck.getCardCounts();
        assertEquals(2, counts.get("SkipCard"), "Deck should have 3 cards initially");
        
        // Verify new cards were added
        assertEquals(3, counts.get("DefuseCard"));  // 5-2 = 3 defuse cards
        assertEquals(2, counts.get("AttackCard"));
        assertEquals(2, counts.get("SkipCard"));
    }

    @Test
    void testValidateDeckBoundaryConditions() {
        // Initialize deck for player count validation
        deck.initializeBaseDeck(2);
        // Test player count boundaries
        assertFalse(deck.validateDeck(1), "Player count 1 should be invalid");
        assertTrue(deck.validateDeck(2), "Player count 2 should be valid");
        deck.initializeBaseDeck(4);
        assertTrue(deck.validateDeck(4), "Player count 4 should be valid");
        
        // Test invalid defuse count
        deck.clear();
        deck.initializeBaseDeck(2); 
        deck.getCards().removeIf(card -> card instanceof DefuseCard);  
        deck.addCards(defuseCard, 2);  
        assertFalse(deck.validateDeck(2), "Invalid defuse count should fail validation");
        
        // Test invalid main cards
        deck.clear();
        deck.initializeBaseDeck(2);  
        deck.getCards().removeIf(card -> card instanceof AttackCard);  
        deck.addCards(attackCard, 1); 
        assertFalse(deck.validateDeck(2), "Invalid main cards should fail validation");
        
        // Test invalid cat cards
        deck.clear();
        deck.initializeBaseDeck(2);  
        deck.getCards().removeIf(card -> card instanceof CatCard);  
        deck.addCards(new CatCard(CatType.TACOCAT), 1);  
        assertFalse(deck.validateDeck(2), "Invalid cat cards should fail validation");
        
        // Test valid deck
        deck.clear();
        deck.initializeBaseDeck(2);  
        assertTrue(deck.validateDeck(2), "Valid deck should pass validation");
    }

    @Test
    void testValidateDeckPrintsDebugInfo() throws UnsupportedEncodingException {
        // 保存原始的 System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8.name()));

        try {
            // Test invalid defuse count
            deck.clear();
            deck.initializeBaseDeck(2);
            deck.getRealCards().removeIf(card -> card instanceof DefuseCard);
            assertFalse(deck.validateDeck(2));
            assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("DEBUG: Card counts"));
            assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Invalid defuse count"));
            
            // 清空输出
            outContent.reset();
            
            // Test invalid main cards
            deck.clear();
            deck.initializeBaseDeck(2);
            deck.getRealCards().removeIf(card -> card instanceof AttackCard);
            deck.addCard(shuffleCard);  // 使用 shuffleCard 字段
            assertFalse(deck.validateDeck(2));
            assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("DEBUG: Card counts"));
            assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Invalid main cards"));
            
            // 清空输出
            outContent.reset();
            
            // Test invalid cat cards
            deck.clear();
            deck.initializeBaseDeck(2);
            deck.getRealCards().removeIf(card -> card instanceof CatCard);
            deck.addCard(catCard);
            assertFalse(deck.validateDeck(2));
            assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("DEBUG: Card counts"));
            assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("Invalid cat cards"));
        } 
        finally {
            System.setOut(originalOut);
        }
    }

    private boolean invokeValidateDeck(int playerCount) {
        try {
            java.lang.reflect.Method m = Deck.class.getDeclaredMethod("validateDeck", int.class);
            m.setAccessible(true);
            return (boolean) m.invoke(deck, playerCount);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean invokeValidateDefuse(Map<String, Integer> counts, int playerCount) {
        try {
            java.lang.reflect.Method m = Deck.class.getDeclaredMethod("validateDefuse", Map.class, int.class);
            m.setAccessible(true);
            return (boolean) m.invoke(deck, counts, playerCount);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean invokeValidateMainCards(Map<String, Integer> counts) {
        try {
            java.lang.reflect.Method m = Deck.class.getDeclaredMethod("validateMainCards", Map.class);
            m.setAccessible(true);
            return (boolean) m.invoke(deck, counts);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean invokeValidateCatCards(Map<String, Integer> counts) {
        try {
            java.lang.reflect.Method m = Deck.class.getDeclaredMethod("validateCatCards", Map.class);
            m.setAccessible(true);
            return (boolean) m.invoke(deck, counts);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCardCloneCatchBlockCoverageInCard() {
        Card card = new Card(CardType.ATTACK) {
            @Override
            public void effect(List<Player> turnOrder, Deck gameDeck) {}

            @Override
            protected Card doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("Forced failure");
            }
        };

        AssertionError error = assertThrows(AssertionError.class, card::clone);
        assertEquals("Card cloning failed", error.getMessage());
        assertTrue(error.getCause() instanceof CloneNotSupportedException);
    }

    @Test
    void testAddExplodingKittensBoundaryConditions() {
        // Test valid boundaries
        assertDoesNotThrow(() -> deck.addExplodingKittens(0));  
        assertDoesNotThrow(() -> deck.addExplodingKittens(1));  
        assertDoesNotThrow(() -> deck.addExplodingKittens(100));  
        
        // Test invalid boundaries
        assertThrows(IllegalArgumentException.class, () -> deck.addExplodingKittens(-1));  
    }
}
