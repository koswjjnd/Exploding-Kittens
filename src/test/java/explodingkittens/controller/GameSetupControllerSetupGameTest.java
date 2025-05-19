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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    }

    @Test
    void testSetupGameSuccessWithTwoPlayers() throws InvalidPlayerCountException, 
            InvalidNicknameException, InvalidDeckException {
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        controller.setupGame();
        
        verifySetupGameSuccess(2);
    }

    @Test
    void testSetupGameSuccessWithThreePlayers() throws InvalidPlayerCountException, 
            InvalidNicknameException, InvalidDeckException {
        when(view.promptPlayerCount()).thenReturn(3);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        when(view.promptNickname(3)).thenReturn("P3");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        controller.setupGame();
        
        verifySetupGameSuccess(3);
    }

    @Test
    void testSetupGameSuccessWithFourPlayers() throws InvalidPlayerCountException, 
            InvalidNicknameException, InvalidDeckException {
        when(view.promptPlayerCount()).thenReturn(4);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        when(view.promptNickname(3)).thenReturn("P3");
        when(view.promptNickname(4)).thenReturn("P4");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);
        
        controller.setupGame();
        
        verifySetupGameSuccess(4);
    }

    @Test
    void testSetupGameInvalidPlayerCountOne() throws InvalidPlayerCountException, 
            InvalidNicknameException {
        when(view.promptPlayerCount()).thenReturn(1);
        doThrow(new InvalidPlayerCountException("Invalid count"))
            .when(playerService).validateCount(1);
        
        assertThrows(InvalidPlayerCountException.class, () -> {
            controller.setupGame();
        });
        
        verifySetupGameFailure();
    }

    @Test
    void testSetupGameInvalidPlayerCountFive() throws InvalidPlayerCountException, 
            InvalidNicknameException {
        when(view.promptPlayerCount()).thenReturn(5);
        doThrow(new InvalidPlayerCountException("Invalid count"))
            .when(playerService).validateCount(5);
        
        assertThrows(InvalidPlayerCountException.class, () -> {
            controller.setupGame();
        });
        
        verifySetupGameFailure();
    }

    @Test
    void testSetupGameEmptyNickname() throws InvalidPlayerCountException, 
            InvalidNicknameException {
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("", "P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Nickname cannot be empty"))
            .when(playerService).createPlayer("");
        
        controller.setupGame();
        
        verify(view).showError("Nickname cannot be empty");
        verify(playerService).createPlayer("P1");
        verify(playerService).createPlayer("P2");
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), anyInt());
    }

    @Test
    void testSetupGameWhitespaceNickname() throws InvalidPlayerCountException, 
            InvalidNicknameException {
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn(" ", "P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Nickname cannot be whitespace"))
            .when(playerService).createPlayer(" ");
        
        controller.setupGame();
        
        verify(view).showError("Nickname cannot be whitespace");
        verify(playerService).createPlayer("P1");
        verify(playerService).createPlayer("P2");
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), anyInt());
    }

    @Test
    void testSetupGameNullNickname() throws InvalidPlayerCountException, 
            InvalidNicknameException {
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn(null, "P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidNicknameException("Nickname cannot be null"))
            .when(playerService).createPlayer(null);
        
        controller.setupGame();
        
        verify(view).showError("Nickname cannot be null");
        verify(playerService).createPlayer("P1");
        verify(playerService).createPlayer("P2");
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), anyInt());
    }

    @Test
    void testSetupGameDeckPreparationFailure() throws InvalidPlayerCountException, 
            InvalidNicknameException, InvalidDeckException {
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        doThrow(new InvalidDeckException("Failed to prepare deck"))
            .when(dealService).dealDefuses(any(), anyList());
        
        assertThrows(InvalidDeckException.class, () -> {
            controller.setupGame();
        });
        
        verifySetupGameFailure();
    }

    @Test
    void testSetupGameTurnOrderInitializationFailure() throws InvalidPlayerCountException, 
            InvalidNicknameException, InvalidDeckException {
        when(view.promptPlayerCount()).thenReturn(2);
        when(view.promptNickname(1)).thenReturn("P1");
        when(view.promptNickname(2)).thenReturn("P2");
        Player mockPlayer = mock(Player.class);
        when(playerService.createPlayer("P1")).thenReturn(mockPlayer);
        when(playerService.createPlayer("P2")).thenReturn(mockPlayer);
        
        try (MockedStatic<GameContext> mockedStatic = mockStatic(GameContext.class)) {
            mockedStatic.when(() -> GameContext.setTurnOrder(anyList()))
                .thenThrow(new IllegalArgumentException("Turn order cannot contain null players"));
            
            assertThrows(IllegalArgumentException.class, () -> {
                controller.setupGame();
            });
            
            verify(view).promptPlayerCount();
            verify(playerService).validateCount(2);
            verify(view).promptNickname(1);
            verify(view).promptNickname(2);
            verify(playerService).createPlayer("P1");
            verify(playerService).createPlayer("P2");
            verify(dealService).dealDefuses(any(), anyList());
            verify(dealService).dealInitialHands(any(), anyList(), anyInt());
            mockedStatic.verify(() -> GameContext.setTurnOrder(anyList()));
        }
    }

    private void verifySetupGameSuccess(int playerCount) throws InvalidPlayerCountException, 
            InvalidNicknameException {
        verify(view).promptPlayerCount();
        verify(playerService).validateCount(playerCount);
        for (int i = 1; i <= playerCount; i++) {
            verify(view).promptNickname(i);
            verify(playerService).createPlayer("P" + i);
        }
        verify(dealService).dealDefuses(any(), anyList());
        verify(dealService).dealInitialHands(any(), anyList(), anyInt());
    }

    private void verifySetupGameFailure() throws InvalidPlayerCountException, 
            InvalidNicknameException {
        verify(view).promptPlayerCount();
        verify(playerService).validateCount(anyInt());
    }
} 