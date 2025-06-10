package explodingkittens;

import explodingkittens.controller.GameController;
import explodingkittens.controller.GameSetupController;
import explodingkittens.exceptions.GameOverException;
import explodingkittens.service.DealService;
import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.util.I18nUtil;

public class Main {
    /**
     * The main entry point of the Exploding Kittens game.
     * Initializes the game setup and starts the main game loop.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize internationalization
            I18nUtil.initialize();
            System.out.println(I18nUtil.getMessage("ui.welcome"));

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
            System.out.println(I18nUtil.getMessage("game.end") + ": " + ge.getMessage());
        } 
        catch (Exception e) {
            System.out.println(I18nUtil.getMessage("error.fatal") + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

