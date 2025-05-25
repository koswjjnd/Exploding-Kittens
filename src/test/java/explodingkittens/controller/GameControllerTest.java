package explodingkittens.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatType;
import explodingkittens.view.GameView;
import explodingkittens.exceptions.GameOverException;
import java.util.ArrayList;
import java.util.List;

class GameControllerTest {
    @Mock
    private GameView mockView;
    
    private GameController gameController;
    private List<Player> testPlayers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(mockView);
        
        // Create test players
        testPlayers = new ArrayList<>();
        testPlayers.add(new Player("Player1"));
        testPlayers.add(new Player("Player2"));
    }

    /**
     * Initializes a deck with the specified number of cat cards.
     * @param numCards number of cards to add to the deck
     * @return the initialized deck
     */
    private Deck initializeDeck(int numCards) {
        Deck deck = new Deck();
        for (int i = 0; i < numCards; i++) {
            deck.addCard(new CatCard(CatType.TACOCAT));
        }
        return deck;
    }

    /**
     * Sets up the game context with players and deck.
     * @param players list of players
     * @param numCards number of cards to add to the deck
     */
    private void setupGameContext(List<Player> players, int numCards) {
        GameContext.setTurnOrder(players);
        GameContext.setGameDeck(initializeDeck(numCards));
        GameContext.setGameOver(false);
    }

    @Test
    void testStartGame() throws GameOverException {
        // Setup
        when(mockView.promptPlayerAction(any())).thenReturn("draw");
        
        // Set up game context
        setupGameContext(testPlayers, 10);
        
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
        verify(mockView, atLeastOnce()).displayCurrentPlayer(any());
    }

    @Test
    void testStartGameWithException() {
        // Setup
        setupGameContext(testPlayers, 10);
        when(mockView.promptPlayerAction(any())).thenThrow(new RuntimeException("Test exception"));
        
        // Execute and verify
        assertThrows(GameOverException.class, () -> gameController.start());
    }

    @Test
    void testHandlePlayerTurn() throws GameOverException {
        // Setup
        Player player = testPlayers.get(0);
        when(mockView.promptPlayerAction(any())).thenReturn("draw");
        
        // Set up game context
        setupGameContext(testPlayers, 10);
        
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
        setupGameContext(singlePlayer, 10);
        when(mockView.promptPlayerAction(any())).thenReturn("draw");
        
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
        setupGameContext(testPlayers, 10);
        
        // Set up mock view
        when(mockView.promptPlayerAction(any())).thenReturn("draw");
        
        // Execute
        gameController.start();
        
        // Verify
        verify(mockView).displayPlayerEliminated(player);
    }
} 