package explodingkittens;

import explodingkittens.controller.GameController;
import explodingkittens.controller.GameSetupController;
import explodingkittens.exceptions.GameOverException;
import explodingkittens.service.DealService;
import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.view.ConsoleGameView;

public class Main {
    /**
     * The main entry point of the Exploding Kittens game.
     * Initializes the game setup and starts the main game loop.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            /* ---------- Step 1 : 游戏初始化 ---------- */
            GameSetupView setupView   = new GameSetupView();
            PlayerService playerSvc   = new PlayerService();
            DealService   dealSvc     = new DealService();
            GameSetupController setup = new GameSetupController(setupView, playerSvc, dealSvc);

            setup.setupGame();               // 交互式：人数 → 昵称 → 发牌 → 写入 GameContext

            /* ---------- Step 2 : 进入主循环 ---------- */
            ConsoleGameView gameView = new ConsoleGameView();
            GameController  gameCtrl = new GameController(gameView);
            gameCtrl.start();

        } 
        catch (GameOverException ge) {
            System.out.println("Game ended abnormally: " + ge.getMessage());
        } 
        catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

