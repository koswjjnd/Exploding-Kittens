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
            // Main game loop
            while (!GameContext.isGameOver()) {
                Player current = GameContext.getCurrentPlayer();
                view.displayCurrentPlayer(current);

                // --- 多子回合循环 ---
                while (current.getLeftTurns() > 0 && !GameContext.isGameOver()) {
                    turnService.takeTurn(current);   // 执行一次子回合
                }

                // 轮到下一位玩家
                GameContext.nextTurn();
            }
            
            // Game over - determine and display winner
            handleGameOver();
        } 
        catch (Exception e) {
            throw new GameOverException("Game ended unexpectedly: " + e.getMessage(), e);
        }
    }


    /**
     * Handles the end of the game.
     */
    private void handleGameOver() {
        if (GameContext.getTurnOrder().size() == 1) {
            view.displayWinner(GameContext.getTurnOrder().get(0));
        } 
        else {
            view.displayGameOver();
        }
    }

    // /**
    //  * Handles the effect of a cat card.
    //  * @param card The cat card to handle
    //  * @param turnOrder The current turn order
    //  * @param gameDeck The game deck
    //  */
    // private void handleCatCard(CatCard card, List<Player> turnOrder, Deck gameDeck) {
    //     try {
    //         card.effect(turnOrder, gameDeck);
    //     } 
    //     catch (CatCardEffect effect) {
    //         // Check for Nope cards
    //         if (!checkForNope(turnOrder)) {
    //             // No Nope cards played, execute the effect
    //             Player currentPlayer = turnOrder.get(0);
                
    //             // Remove the cat card pair
    //             currentPlayer.removeCard(effect.getFirstCard());
    //             currentPlayer.removeCard(effect.getSecondCard());
                
    //             // Get and remove the target card
    //             Card cardToSteal = effect.getTargetPlayerHand().get(effect.getTargetCardIndex());
    //             Player targetPlayer = turnOrder.stream()
    //                 .filter(p -> p.getName().equals(effect.getTargetPlayerName()))
    //                 .findFirst()
    //                 .orElseThrow(() -> new IllegalStateException("Target player not found"));
    //             if (targetPlayer.removeCard(cardToSteal)) {
    //                 currentPlayer.receiveCard(cardToSteal);
    //             }
    //         }
    //     }
    // }
}