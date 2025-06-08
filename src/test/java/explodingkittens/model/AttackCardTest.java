package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.service.NopeService;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyInt;

/**
 * Test class for AttackCard.
 */
class AttackCardTest {
    
    @Mock
    private Player currentPlayer;
    
    @Mock
    private Player nextPlayer;
    
    @Mock
    private Deck gameDeck;
    
    @Mock
    private NopeService nopeService;
    
    @Mock
    private GameView gameView;

    private AttackCard attackCard;
    private List<Player> turnOrder;
    private MockedStatic<GameContext> mockedGameContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        attackCard = new AttackCard();
        attackCard.setServices(nopeService, gameView);
        turnOrder = new ArrayList<>();
        turnOrder.add(currentPlayer);
        turnOrder.add(nextPlayer);
        
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
        mockedGameContext = Mockito.mockStatic(GameContext.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedGameContext != null) {
            mockedGameContext.close();
        }
    }

    /**
     * BVA Test Case 1: turnOrder = empty list
     * Expected: IllegalArgumentException
     */
    @Test
    void testEffectWithEmptyTurnOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            attackCard.effect(new ArrayList<>(), gameDeck);
        });
    }

    /**
     * BVA Test Case 2: turnOrder = multiple players, current player's left turns = 0
     * Expected: Next player's left turns = 2
     */
    @Test
    void testEffectWithZeroLeftTurns() {
        // Setup
        when(currentPlayer.getLeftTurns()).thenReturn(0);
        when(currentPlayer.isAlive()).thenReturn(true);
        when(currentPlayer.getName()).thenReturn("CurrentPlayer");
        when(nextPlayer.isAlive()).thenReturn(true);
        when(nextPlayer.getName()).thenReturn("NextPlayer");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(currentPlayer);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.movePlayerToEnd(currentPlayer)
        ).thenAnswer(invocation -> null);
        
        // Execute
        attackCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(currentPlayer).setLeftTurns(0);
        verify(nextPlayer).setLeftTurns(2);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(currentPlayer));
    }

    /**
     * BVA Test Case 3: turnOrder = multiple players, current player's left turns = 1
     * Expected: Next player's left turns = 3
     */
    @Test
    void testEffectWithOneLeftTurn() {
        // Setup
        when(currentPlayer.getLeftTurns()).thenReturn(1);
        when(currentPlayer.isAlive()).thenReturn(true);
        when(currentPlayer.getName()).thenReturn("CurrentPlayer");
        when(nextPlayer.isAlive()).thenReturn(true);
        when(nextPlayer.getName()).thenReturn("NextPlayer");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(currentPlayer);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.movePlayerToEnd(currentPlayer)
        ).thenAnswer(invocation -> null);
        
        // Execute
        attackCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(currentPlayer).setLeftTurns(0);
        verify(nextPlayer).setLeftTurns(3);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(currentPlayer));
    }

    /**
     * BVA Test Case 4: turnOrder = multiple players, current player's left turns = 2
     * Expected: Next player's left turns = 4
     */
    @Test
    void testEffectWithTwoLeftTurns() {
        // Setup
        when(currentPlayer.getLeftTurns()).thenReturn(2);
        when(currentPlayer.isAlive()).thenReturn(true);
        when(currentPlayer.getName()).thenReturn("CurrentPlayer");
        when(nextPlayer.isAlive()).thenReturn(true);
        when(nextPlayer.getName()).thenReturn("NextPlayer");
        
        // Mock GameContext
        mockedGameContext.when(GameContext::getCurrentPlayer).thenReturn(currentPlayer);
        mockedGameContext.when(GameContext::getTurnOrder).thenReturn(turnOrder);
        mockedGameContext.when(GameContext::isGameOver).thenReturn(false);
        mockedGameContext.when(() -> GameContext.movePlayerToEnd(currentPlayer)
        ).thenAnswer(invocation -> null);
        
        // Execute
        attackCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(currentPlayer).setLeftTurns(0);
        verify(nextPlayer).setLeftTurns(4);
        mockedGameContext.verify(() -> GameContext.movePlayerToEnd(currentPlayer));
    }

    @Test
    void testEffectWithNopeCard() {
        // Setup
        when(currentPlayer.getLeftTurns()).thenReturn(1);
        when(nopeService.isNegatedByPlayers(attackCard)).thenReturn(true);
        
        // Execute
        attackCard.effect(turnOrder, gameDeck);
        
        // Verify
        verify(gameView).showCardNoped(currentPlayer, attackCard);
        verify(currentPlayer, never()).setLeftTurns(anyInt());
        verify(nextPlayer, never()).setLeftTurns(anyInt());
    }
} 