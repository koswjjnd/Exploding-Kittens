package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.service.CardEffectService;
import explodingkittens.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurnServiceTest {

    private TurnService turnService;
    private MockedStatic<GameContext> mockedGameContext;

    @Mock
    private Player player;

    @Mock
    private GameContext gameContext;

    @Mock
    private Deck deck;

    @Mock
    private Card card;

    @Mock
    private GameView view;

    @Mock
    private CardEffectService cardEffectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        turnService = new TurnService(view, cardEffectService);
        mockedGameContext = mockStatic(GameContext.class);
        mockedGameContext.when(GameContext::getGameDeck).thenReturn(deck);
    }

    @Test
    void takeTurn_NullPlayer_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            turnService.takeTurn(null, gameContext));
    }
    @Test
    void takeTurn_EmptyHand_OnlyDrawsCard() throws EmptyDeckException {
        when(player.getHand()).thenReturn(new ArrayList<>());
        when(deck.drawOne()).thenReturn(card);
        
        turnService.takeTurn(player, gameContext);
        
        verify(player).receiveCard(card);
        verify(view, never()).selectCardToPlay(any(), any());
    }

   
} 
