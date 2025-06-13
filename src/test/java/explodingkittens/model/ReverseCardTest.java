package explodingkittens.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import explodingkittens.controller.GameContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyInt;

@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
class ReverseCardTest {
    private Player player1;
    private Player player2;
    private Player player3;
    private Deck deck;
    private List<Player> turnOrder;
    private ReverseCard card;
    private MockedStatic<GameContext> mockedGameContext;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);
        deck = mock(Deck.class);
        turnOrder = new ArrayList<>();
        card = new ReverseCard();
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
        mockedGameContext = Mockito.mockStatic(GameContext.class);
        
        // Redirect System.out with explicit charset
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
        System.setOut(originalOut);
    }

    @Test
    void testConstructor() {
        ReverseCard newCard = new ReverseCard();
        assertNotNull(newCard);
        assertEquals(CardType.REVERSE, newCard.getType());
    }

    @Test
    void testEffectWithNullTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            card.effect(null, deck);
        });
        assertEquals("", outContent.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testEffectWithEmptyTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            card.effect(new ArrayList<>(), deck);
        });
        assertEquals("", outContent.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testEffectWithSinglePlayer() {
        // Setup
        turnOrder.add(player1);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        
        // Mock GameContext
        mockedGameContext.when(() -> GameContext.getCurrentPlayer()).thenReturn(player1);
        List<Player> mockTurnOrder = List.of(player1);
        mockedGameContext.when(() -> GameContext.getTurnOrder()).thenReturn(mockTurnOrder);
        mockedGameContext.when(() -> GameContext.isGameOver()).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenAnswer(invocation -> {
            List<Player> newOrder = invocation.getArgument(0);
            turnOrder.clear();
            turnOrder.addAll(newOrder);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(1, turnOrder.size());
        assertEquals(player1, turnOrder.get(0));
        verify(player1).setLeftTurns(0);
        
        // Verify GameContext interactions
        List<Player> actualTurnOrder = GameContext.getTurnOrder();
        assertEquals(mockTurnOrder, actualTurnOrder);
        boolean gameOver = GameContext.isGameOver();
        assertFalse(gameOver);
        
        // Verify output
        String expectedOutput = String.format("%nTurn order after Reverse card:%n1. Player1%n%n");
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .replaceAll("\\r\\n", "\n");
        assertEquals(expectedOutput.replaceAll("\\r\\n", "\n"), actualOutput);
    }

    @Test
    void testEffectWithMultiplePlayers() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        turnOrder.add(player3);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        when(player3.isAlive()).thenReturn(true);
        when(player3.getName()).thenReturn("Player3");
        
        // Mock GameContext
        mockedGameContext.when(() -> GameContext.getCurrentPlayer()).thenReturn(player1);
        List<Player> mockTurnOrder = List.of(player1, player2, player3);
        mockedGameContext.when(() -> GameContext.getTurnOrder()).thenReturn(mockTurnOrder);
        mockedGameContext.when(() -> GameContext.isGameOver()).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenAnswer(invocation -> {
            List<Player> newOrder = invocation.getArgument(0);
            turnOrder.clear();
            turnOrder.addAll(newOrder);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(3, turnOrder.size());
        assertSame(player3, turnOrder.get(0));
        assertSame(player2, turnOrder.get(1));
        assertSame(player1, turnOrder.get(2));
        verify(player1).setLeftTurns(0);
        
        // Verify GameContext interactions
        List<Player> actualTurnOrder = GameContext.getTurnOrder();
        assertEquals(mockTurnOrder, actualTurnOrder);
        boolean gameOver = GameContext.isGameOver();
        assertFalse(gameOver);
        
        // Verify output
        String expectedOutput = String.format(
            "%nTurn order after Reverse card:%n1. Player3%n2. Player2%n3. Player1%n%n"
        );
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .replaceAll("\\r\\n", "\n");
        assertEquals(expectedOutput.replaceAll("\\r\\n", "\n"), actualOutput);
    }

    @Test
    void testEffectWithAttackedPlayer() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(2);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(() -> GameContext.getCurrentPlayer()).thenReturn(player1);
        List<Player> mockTurnOrder = List.of(player1, player2);
        mockedGameContext.when(() -> GameContext.getTurnOrder()).thenReturn(mockTurnOrder);
        mockedGameContext.when(() -> GameContext.isGameOver()).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenAnswer(invocation -> {
            List<Player> newOrder = invocation.getArgument(0);
            turnOrder.clear();
            turnOrder.addAll(newOrder);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(2, turnOrder.size());
        assertSame(player2, turnOrder.get(0));
        assertSame(player1, turnOrder.get(1));
        verify(player1).decrementLeftTurns();
        
        // Verify GameContext interactions
        List<Player> actualTurnOrder = GameContext.getTurnOrder();
        assertEquals(mockTurnOrder, actualTurnOrder);
        boolean gameOver = GameContext.isGameOver();
        assertFalse(gameOver);
        
        // Verify output
        String expectedOutput = String.format("%nTurn order after Reverse card:%n1. Player2%n2. Player1%n%n");
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .replaceAll("\\r\\n", "\n");
        assertEquals(expectedOutput.replaceAll("\\r\\n", "\n"), actualOutput);
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    @Test
    void testEffectWithEliminatedPlayer() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(false);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(() -> GameContext.getCurrentPlayer()).thenReturn(player1);
        List<Player> mockTurnOrder = List.of(player1, player2);
        mockedGameContext.when(() -> GameContext.getTurnOrder()).thenReturn(mockTurnOrder);
        mockedGameContext.when(() -> GameContext.isGameOver()).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenAnswer(invocation -> {
            List<Player> newOrder = invocation.getArgument(0);
            turnOrder.clear();
            turnOrder.addAll(newOrder);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        assertEquals(2, turnOrder.size());
        assertSame(player2, turnOrder.get(0));
        assertSame(player1, turnOrder.get(1));
        verify(player1).setLeftTurns(0);
        
        // Verify GameContext interactions
        List<Player> actualTurnOrder = GameContext.getTurnOrder();
        assertEquals(mockTurnOrder, actualTurnOrder);
        boolean gameOver = GameContext.isGameOver();
        assertFalse(gameOver);
        
        // Verify output
        String expectedOutput = String.format(
            "%nTurn order after Reverse card:%n1. Player2 (Eliminated)%n2. Player1%n%n"
        );
        String actualOutput = outContent.toString(StandardCharsets.UTF_8)
            .replaceAll("\\r\\n", "\n");
        assertEquals(expectedOutput.replaceAll("\\r\\n", "\n"), actualOutput);
    }

    @Test
    void testEffectWithRecovery() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(true);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(true);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(() -> GameContext.getCurrentPlayer()).thenReturn(player1);
        List<Player> mockTurnOrder = List.of(player1, player2);
        mockedGameContext.when(() -> GameContext.getTurnOrder()).thenReturn(mockTurnOrder);
        mockedGameContext.when(() -> GameContext.isGameOver()).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenThrow(new RuntimeException("Test error"));
        mockedGameContext.when(() -> GameContext.setCurrentPlayerIndex(anyInt())).thenAnswer(invocation -> {
            int index = invocation.getArgument(0);
            return null;
        });
        
        // Execute
        card.effect(turnOrder, deck);
        
        // Verify
        verify(player1).setLeftTurns(0);
        mockedGameContext.verify(() -> GameContext.setCurrentPlayerIndex(0));
        
        // Verify GameContext interactions
        List<Player> actualTurnOrder = GameContext.getTurnOrder();
        assertEquals(mockTurnOrder, actualTurnOrder);
        boolean gameOver = GameContext.isGameOver();
        assertFalse(gameOver);
        
        // Verify output
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error during reverse operation: Test error"));
        assertTrue(output.contains("Attempting to recover..."));
        assertTrue(output.contains("Recovered: Next player will be Player1"));
    }

    @Test
    void testEffectWithNoValidPlayers() {
        // Setup
        turnOrder.add(player1);
        turnOrder.add(player2);
        when(player1.getLeftTurns()).thenReturn(1);
        when(player1.isAlive()).thenReturn(false);
        when(player1.getName()).thenReturn("Player1");
        when(player2.isAlive()).thenReturn(false);
        when(player2.getName()).thenReturn("Player2");
        
        // Mock GameContext
        mockedGameContext.when(() -> GameContext.getCurrentPlayer()).thenReturn(player1);
        List<Player> mockTurnOrder = List.of(player1, player2);
        mockedGameContext.when(() -> GameContext.getTurnOrder()).thenReturn(mockTurnOrder);
        mockedGameContext.when(() -> GameContext.isGameOver()).thenReturn(false);
        mockedGameContext.when(() -> GameContext.setTurnOrder(anyList())).thenThrow(new RuntimeException("Test error"));
        
        // Execute and verify
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            card.effect(turnOrder, deck);
        });
        assertEquals("No valid players found after reverse operation", exception.getMessage());
        
        // Verify GameContext interactions
        List<Player> actualTurnOrder = GameContext.getTurnOrder();
        assertEquals(mockTurnOrder, actualTurnOrder);
        boolean gameOver = GameContext.isGameOver();
        assertFalse(gameOver);
        
        // Verify output
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error during reverse operation: Test error"));
        assertTrue(output.contains("Attempting to recover..."));
    }
} 