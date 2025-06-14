package explodingkittens.controller;

import explodingkittens.model.*;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.exceptions.GameOverException;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerGameplayTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        // 保存原始输出流
        originalOut = System.out;
        originalErr = System.err;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // 恢复原始输出流
        System.setOut(originalOut);
        System.setErr(originalErr);
        // 重置游戏上下文
        GameContext.setTurnOrder(List.of());
        GameContext.setGameDeck(new Deck());
        GameContext.setGameOver(false);
    }

    @Test
    void testGameControllerWithManualSetup() {
        // 1. 准备玩家
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        GameContext.setTurnOrder(Arrays.asList(p1, p2));

        // 2. 准备游戏牌组（确保有足够的牌）
        Deck deck = new Deck();
        for (int i = 0; i < 10; i++) {
            deck.addCard(new BasicCard(CardType.SKIP));
        }
        GameContext.setGameDeck(deck);

        // 3. 准备输入动作：抽牌、跳过等
        StringBuilder actionInput = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            actionInput.append("1\n"); // 假设1是抽牌动作
        }
        System.setIn(new ByteArrayInputStream(actionInput.toString().getBytes(StandardCharsets.UTF_8)));

        // 4. 创建游戏控制器并启动游戏
        GameController gameCtrl = new GameController(new ConsoleGameView());

        try {
            gameCtrl.start();
        } catch (GameOverException e) {
            // 游戏正常结束
            System.err.println("[Game Over] Caught during test: " + e.getMessage());
        }

        // 5. 验证输出
        String output = outContent.toString();
        System.out.println("[DEBUG OUTPUT]");
        System.out.println(output);

        // 验证关键输出
        assertTrue(output.contains("Alice") || output.contains("Bob"), "Should show player names");
        assertTrue(output.contains("draw") || output.contains("skip"), "Should show player actions");
    }

    @Test
    void testGameControllerWithExplodingKitten() {
        // 1. 准备玩家
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        GameContext.setTurnOrder(Arrays.asList(p1, p2));

        // 2. 准备游戏牌组（包含一张炸猫牌）
        Deck deck = new Deck();
        deck.addCard(new ExplodingKittenCard());
        for (int i = 0; i < 5; i++) {
            deck.addCard(new BasicCard(CardType.SKIP));
        }
        GameContext.setGameDeck(deck);

        // 3. 准备输入动作
        String actionInput = "1\n"; // 抽牌
        System.setIn(new ByteArrayInputStream(actionInput.getBytes(StandardCharsets.UTF_8)));

        // 4. 创建游戏控制器并启动游戏
        GameController gameCtrl = new GameController(new ConsoleGameView());

        try {
            gameCtrl.start();
        } catch (GameOverException e) {
            // 游戏正常结束
            System.err.println("[Game Over] Caught during test: " + e.getMessage());
        }

        // 5. 验证输出
        String output = outContent.toString();
        System.out.println("[DEBUG OUTPUT]");
        System.out.println(output);

        // 验证关键输出
        assertTrue(output.contains("Exploding") || output.contains("eliminated"), 
            "Should show exploding kitten or player elimination");
    }
} 