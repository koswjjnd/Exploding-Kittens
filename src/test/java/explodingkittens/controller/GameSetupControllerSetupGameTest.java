package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.service.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import java.util.List;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.exceptions.InvalidDeckException;

@ExtendWith(MockitoExtension.class)
class GameSetupControllerSetupGameTest {
    @Mock
    private GameSetupView view;
    @Mock
    private PlayerService playerService;
    @Mock
    private DealService dealService;
    private GameSetupController controller;

    @BeforeEach
    void setUp() {
        controller = new GameSetupController(view, playerService, dealService);
        // Reset GameContext before each test
        GameContext.reset();
    }

    @Test
    void testSetupGameSuccessWithTwoPlayers() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        // Execute
        controller.setupGame();
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(2, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
        
        // Verify service calls
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), eq(5));
    }

    @Test
    void testSetupGameSuccessWithThreePlayers() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(3);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        when(view.promptNickname(3)).thenReturn("P3");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        // Execute
        controller.setupGame();
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(3, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
        
        // Verify service calls
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), eq(5));
    }

    @Test
    void testSetupGameSuccessWithFourPlayers() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(4);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        when(view.promptNickname(3)).thenReturn("P3");
        when(view.promptNickname(4)).thenReturn("P4");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        // Execute
        controller.setupGame();
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(4, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
        
        // Verify service calls
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), eq(5));
    }

    @Test
    void testSetupGameInvalidPlayerCountOne() {
        // Setup
        when(view.promptPlayerCount()).thenReturn(1);
        
        // Execute and verify
        assertThrows(InvalidPlayerCountException.class, () -> controller.setupGame());
        
        // Verify GameContext is not initialized
        assertNull(GameContext.getTurnOrder());
        assertNull(GameContext.getGameDeck());
    }

    @Test
    void testSetupGameInvalidPlayerCountFive() {
        // Setup
        when(view.promptPlayerCount()).thenReturn(5);
        
        // Execute and verify
        assertThrows(InvalidPlayerCountException.class, () -> controller.setupGame());
        
        // Verify GameContext is not initialized
        assertNull(GameContext.getTurnOrder());
        assertNull(GameContext.getGameDeck());
    }

    @Test
    void testSetupGameEmptyNickname() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("", "P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Nickname cannot be empty"))
            .when(playerService).createPlayer("");
        
        // Execute
        controller.setupGame();
        
        // Verify error handling
        verify(view).showError("Nickname cannot be empty");
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(2, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
    }

    @Test
    void testSetupGameWhitespaceNickname() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn(" ", "P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Nickname cannot be whitespace"))
            .when(playerService).createPlayer(" ");
        
        // Execute
        controller.setupGame();
        
        // Verify error handling
        verify(view).showError("Nickname cannot be whitespace");
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(2, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
    }

    @Test
    void testSetupGameNullNickname() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn(null, "P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Nickname cannot be null"))
            .when(playerService).createPlayer(null);
        
        // Execute
        controller.setupGame();
        
        // Verify error handling
        verify(view).showError("Nickname cannot be null");
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(2, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
    }

    @Test
    void testSetupGameDeckPreparationFailure() throws InvalidNicknameException {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidDeckException("Failed to prepare deck"))
            .when(dealService).dealDefuses(any(), anyList());
        
        // Execute and verify
        assertThrows(InvalidDeckException.class, () -> controller.setupGame());
        
        // Verify GameContext is not initialized
        assertNull(GameContext.getTurnOrder());
        assertNull(GameContext.getGameDeck());
    }
} 