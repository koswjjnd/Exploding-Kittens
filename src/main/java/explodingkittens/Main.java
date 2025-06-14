package explodingkittens;

import explodingkittens.controller.GameController;
import explodingkittens.controller.GameSetupController;
import explodingkittens.exceptions.GameOverException;
import explodingkittens.service.DealService;
import explodingkittens.model.PlayerService;
import explodingkittens.view.GameSetupView;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.util.I18nUtil;
import java.util.Locale;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Main {

    /**
     * The main entry point of the Exploding Kittens game.
     * Initializes the game setup and starts the main game loop.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0 && "--test-throw".equals(args[0])) {
                throw new GameOverException("Test game over");
            }
            if (args.length > 0 && "--throw-runtime".equals(args[0])) {
                throw new RuntimeException("Test runtime exception");
            }
            // Initialize I18n with default locale (English)
            I18nUtil.initialize();
            
            // Language selection
            System.out.println(I18nUtil.getMessage("select.language"));
            System.out.println("1. English");
            System.out.println("2. 中文");
            Scanner mainScanner = new Scanner(System.in, StandardCharsets.UTF_8);
            String langChoice = mainScanner.nextLine();
            if ("2".equals(langChoice)) {
                I18nUtil.setLocale(Locale.CHINESE);
            }
            else {
                I18nUtil.setLocale(Locale.ENGLISH);
            }

            System.out.println(I18nUtil.getMessage("ui.welcome"));

            /* ---------- Step 1 : initial ---------- */
            GameSetupView setupView   = new GameSetupView(mainScanner);
            PlayerService playerSvc   = new PlayerService();
            DealService   dealSvc     = new DealService();
            GameSetupController setup = new GameSetupController(setupView, playerSvc, dealSvc, mainScanner);

            setup.setupGame();               

            /* ---------- Step 2 : main loop ---------- */
            ConsoleGameView gameView = new ConsoleGameView(new Scanner(System.in, StandardCharsets.UTF_8));
            GameController  gameCtrl = new GameController(gameView);
            System.out.println("DEBUG: before gameCtrl.start()");
            gameCtrl.start();
            System.out.println("DEBUG: after gameCtrl.start()");

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

