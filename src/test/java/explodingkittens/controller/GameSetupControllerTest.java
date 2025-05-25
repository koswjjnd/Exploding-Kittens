package explodingkittens.controller;
import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.SkipCard;
import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.service.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.atLeastOnce;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import explodingkittens.exceptions.InvalidDeckException;

@ExtendWith(MockitoExtension.class)
class GameSetupControllerTest {
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
    void testSetupGameWithValidPlayerCount() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        // Execute
        controller.setupGame();
        
        // Verify GameContext was properly initialized
        assertNotNull(GameContext.getTurnOrder());
        assertNotNull(GameContext.getGameDeck());
        assertFalse(GameContext.isGameOver());
    }

    @Test
    void testSetupGameWithInvalidPlayerCount() {
        // Setup
        when(view.promptPlayerCount()).thenReturn(1);
        
        // Execute and verify
        assertThrows(InvalidPlayerCountException.class, () -> controller.setupGame());
    }

    @Test
    void testSetupGameWithInvalidNickname() 
            throws InvalidNicknameException, InvalidDeckException, InvalidPlayerCountException {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("Player1");
        when(view.promptNickname(2)).thenReturn("Invalid", "Player2");  // First invalid, then valid
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("Player1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("Player2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Invalid nickname"))
            .when(playerService).createPlayer("Invalid");
        
        // Execute
        controller.setupGame();
        
        // Verify error handling
        verify(view).showError("Invalid nickname");
        
        // Verify GameContext state
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(2, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
    }

    @Test
    void testSetupGameWithDealServiceError() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        doThrow(new InvalidDeckException())
            .when(dealService).dealDefuses(any(Deck.class), anyList());
        
        // Execute and verify
        assertThrows(InvalidDeckException.class, () -> controller.setupGame());
    }

    @Test
    void testSetupGameInitializesGameContextCorrectly() throws Exception {
        // Setup
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(anyInt())).thenReturn("Player1", "Player2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        // Execute
        controller.setupGame();
        
        // Verify GameContext was initialized with correct values
        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();
        
        assertNotNull(turnOrder);
        assertEquals(2, turnOrder.size());
        assertNotNull(gameDeck);
        assertFalse(GameContext.isGameOver());
    }
}