package explodingkittens.controller;

import explodingkittens.model.Player;
import explodingkittens.model.Deck;
import explodingkittens.model.Card;
import explodingkittens.model.CatCard;
import explodingkittens.model.CatCard.CatCardEffect;
import explodingkittens.model.ExplodingKittenCard;
import explodingkittens.view.GameView;
import explodingkittens.exceptions.GameOverException;
import java.util.List;
import java.util.stream.Collectors;
import explodingkittens.service.TurnService;
import explodingkittens.service.CardEffectService;
import explodingkittens.controller.GameContext;

/**
 * GameController manages the main game loop and game state for Exploding Kittens.
 * It handles player turns, card actions, and game progression.
 */
public class GameController {
    private final GameView view;
    private final TurnService turnService;

    /**
     * Constructs a GameController with the given view.
     * @param view the view to interact with the user
     */
    public GameController(GameView view) {
        this.view = view;
        this.turnService = new TurnService(view, new CardEffectService());
    }

    /**
     * Starts the game by running the main game loop.
     * @throws GameOverException if the game ends unexpectedly
     */
    public void start() throws GameOverException {
        try {
            while (true) {
                Player currentPlayer = GameContext.getCurrentPlayer();
                if (currentPlayer == null) {
                    throw new GameOverException("No current player found");
                }

                view.displayCurrentPlayer(currentPlayer);
                turnService.takeTurn(currentPlayer);
                
                // 检查是否只剩一名玩家
                List<Player> alivePlayers = GameContext.getTurnOrder().stream()
                    .filter(Player::isAlive)
                    .collect(Collectors.toList());
                
                if (alivePlayers.size() == 1) {
                    Player winner = alivePlayers.get(0);
                    view.displayWinner(winner);
                    return;
        }
    }
        } catch (Exception e) {
            throw new GameOverException("Game ended unexpectedly: " + e.getMessage());
        }
    }
}