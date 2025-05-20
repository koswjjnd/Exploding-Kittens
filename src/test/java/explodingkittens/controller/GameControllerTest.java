package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.view.GameView;
import explodingkittens.exceptions.GameOverException;
import explodingkittens.exceptions.InvalidPlayerCountException;
import explodingkittens.exceptions.InvalidNicknameException;
import java.util.ArrayList;
import java.util.List;

class GameControllerTest {
    @Mock
    private GameView mockView;
    
    @Mock
    private GameSetupController mockSetupController;
    
    private GameController gameController;
    private List<Player> testPlayers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(mockView, mockSetupController);
        
        // Create test players
        testPlayers = new ArrayList<>();
        testPlayers.add(new Player("Player1"));
        testPlayers.add(new Player("Player2"));
    }

    @Test
    void testStartGame() throws GameOverException, 
            InvalidPlayerCountException, InvalidNicknameException {
        // Setup
        doNothing().when(mockSetupController).setupGame();
        when(mockView.promptPlayerAction(any())).thenReturn("draw");
        
        // Set up game context with two players
        List<Player> players = new ArrayList<>();
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        players.add(player1);
        players.add(player2);
        GameContext.setTurnOrder(players);
        GameContext.setGameDeck(new Deck());
        
        // Make second player not alive after first turn
        doAnswer(invocation -> {
            if (GameContext.getCurrentPlayer() == player2) {
                player2.setAlive(false);
            }
            return "draw";
        }).when(mockView).promptPlayerAction(any());
        
        // Execute
        gameController.start();
        
        // Verify
        verify(mockSetupController).setupGame();
        verify(mockView, atLeastOnce()).displayCurrentPlayer(any());
    }

    @Test
    void testStartGameWithException() throws InvalidPlayerCountException, InvalidNicknameException {
        // Setup
        doThrow(new RuntimeException("Setup failed")).when(mockSetupController).setupGame();
        
        // Execute and verify
        assertThrows(GameOverException.class, () -> gameController.start());
    }

    @Test
    void testHandlePlayerTurn() throws GameOverException {
        // Setup
        Player player = testPlayers.get(0);
        when(mockView.promptPlayerAction(any())).thenReturn("draw");
        
        // Set up game context
        GameContext.setTurnOrder(testPlayers);
        GameContext.setGameDeck(new Deck());
        GameContext.setGameOver(false);  // Ensure game is not over initially
        
        // Make second player not alive after first turn
        doAnswer(invocation -> {
            if (GameContext.getCurrentPlayer() == testPlayers.get(1)) {
                testPlayers.get(1).setAlive(false);
            }
            return "draw";
        }).when(mockView).promptPlayerAction(any());
        
        // Execute
        gameController.start();
        
        // Verify
        verify(mockView).displayPlayerHand(player);
        verify(mockView).promptPlayerAction(player);
    }

    @Test
    void testHandleGameOver() throws GameOverException {
        // Setup
        Player winner = testPlayers.get(0);
        List<Player> singlePlayer = new ArrayList<>();
        singlePlayer.add(winner);
        GameContext.setTurnOrder(singlePlayer);
        GameContext.setGameDeck(new Deck());
        
        // Execute
        gameController.start();
        
        // Verify
        verify(mockView).displayWinner(winner);
    }

    @Test
    void testPlayerElimination() throws GameOverException {
        // Setup
        Player player = testPlayers.get(0);
        player.setAlive(false);
        GameContext.setTurnOrder(testPlayers);
        
        // Execute
        gameController.start();
        
        // Verify
        verify(mockView).displayPlayerEliminated(player);
    }
} 