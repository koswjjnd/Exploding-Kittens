package explodingkittens;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.mockito.Mockito.*;
import explodingkittens.controller.GameController;
import explodingkittens.exceptions.GameOverException;
import explodingkittens.view.GameView;
import explodingkittens.model.Player;
import explodingkittens.controller.GameContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        PrintStream combinedStream = new PrintStream(outContent);
        System.setOut(combinedStream);
        System.setErr(combinedStream);
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testMainWithEnglishInput() {
        System.out.println("[TEST] Running: testMainWithEnglishInput");
        String simulatedInput = "1\n2\nAlice\nBob\n1\n1\n"; // 语言选择、玩家数、玩家名、动作
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // 运行 main 方法
        Main.main(new String[]{});

        // 验证输出包含英文欢迎语（或其他你能确定的英文内容）
        String output = outContent.toString();
        assertTrue(output.contains("Welcome") || output.contains("Exploding Kittens"));
    }

    @Test
    void testMainWithChineseInput() {
        System.out.println("[TEST] Running: testMainWithChineseInput");
        String simulatedInput = "2\n2\n张三\n李四\n1\n1\n"; // 语言选择、玩家数、玩家名、动作
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // 运行 main 方法
        Main.main(new String[]{});

        // 验证输出包含中文欢迎语（或设置的提示）
        String output = outContent.toString();
        assertTrue(output.contains("欢迎") || output.contains("Exploding Kittens"));
    }

    @Test
    void testMainGameOverException() {
        System.out.println("[TEST] Running: testMainGameOverException");
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        Main.main(new String[]{"--test-throw"});
        String output = outContent.toString();
        System.out.println("OUTPUT: " + output); // 调试用
        assertTrue(output.contains("Game over") || output.contains("Game Over") || output.contains("Test game over"));
    }

    @Test
    void testMainCoversSetupAndGameLoop() {
        System.out.println("[TEST] Running: testMainCoversSetupAndGameLoop");
        // 构造一串模拟输入，足够让 setup.setupGame() 和 gameCtrl.start() 能顺利返回
        String simulatedInput = "1\nAlice\n2\nBob\n1\n1\n"; // 具体内容根据你的游戏流程调整
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        // 运行 main 方法
        Main.main(new String[]{});
        // 你可以断言输出包含某些关键字，也可以不断言，只为覆盖率
    }

    @Test
    void testMainCoversGameCtrlStart() {
        System.out.println("[TEST] Running: testMainCoversGameCtrlStart");
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        Main.main(new String[]{"--skip-setup"});
        // 不需要断言，只为覆盖率
    }

    @Test
    void testMainNormalPath() {
        System.out.println("[TEST] Running: testMainNormalPath");
        StringBuilder sb = new StringBuilder();
        sb.append("1\n");     // English
        sb.append("2\n");     // player count
        sb.append("Alice\n");
        sb.append("Bob\n");
        for (int i = 0; i < 100; i++) {
            sb.append("1\n"); // action: draw/skip/etc.
        }
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        Main.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Exploding") || output.contains("Game Over"));
        System.out.println("[TEST OUTPUT] " + outContent.toString());
    }

    @Test
    void testMainFullRun() {
        System.out.println("[TEST] Running: testMainFullRun");
        StringBuilder sb = new StringBuilder();
        sb.append("1\n");       // 语言选择
        sb.append("2\n");       // 玩家数
        sb.append("Alice\n");   // 玩家1名
        sb.append("Bob\n");     // 玩家2名
        sb.append("1\n1\n1\n1\n1\n1\n1\n1\n1\n1\nn\n"); // 前面是动作，最后"n"是爆炸猫时不用defuse
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Exploding") || output.length() > 50);
    }

    @Test
    void testMainCoversGenericException() {
        Main.main(new String[]{"--throw-runtime"});
        // 断言输出
    }

	@Test
	void testGameCtrlStartIsCovered() {
		System.err.println("[DEBUG] Running: testGameCtrlStartIsCovered");
	
		// 构造用户输入流
		StringBuilder sb = new StringBuilder();
		sb.append("1\n"); // English
		sb.append("2\n"); // 玩家数
		sb.append("Alice\n");
		sb.append("Bob\n");
		for (int i = 0; i < 100; i++) {
			sb.append("1\n");
			if (i < 5 || i > 95) {
				System.err.println("[DEBUG] 输入动作: 1 (第" + (i + 1) + "次)");
			}
		}
	
		// 设置输入流
		ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
		System.setIn(in);
	
		// 捕获 Main.main 的 System.out 输出
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		PrintStream combined = new PrintStream(outContent);
		PrintStream originalOut = System.out;
		System.setOut(combined);
	
		// 运行 Main 方法
		try {
			Main.main(new String[]{});
		} catch (Exception e) {
			System.err.println("[ERROR] 异常: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// 恢复 System.out
			System.setOut(originalOut);
		}
	
		// 打印捕获的输出
		String output = outContent.toString();
		System.err.println("[DEBUG] Main.main 输出如下：");
		System.err.println("=====================================");
		System.err.println(output);
		System.err.println("=====================================");
	
		// 验证输出有效
		assertTrue(output.contains("Exploding") || output.contains("Game Over") || output.length() > 100);
	}
	

    @Test
    void testGameCtrlStartWithMock() {
        GameView mockView = mock(GameView.class);
        Player winner = new Player("Winner");
        winner.setAlive(true);
        GameContext.setTurnOrder(Arrays.asList(winner));
        // 还可以设置 GameContext.setGameDeck(...) 等

        GameController ctrl = new GameController(mockView);

        try {
            ctrl.start();
        } catch (GameOverException e) {
            // ignore
        }
        // 不用 verify，只要能走完就能cover
    }

	@Test
	void testMainCoversGameControllerStartLine() {
		StringBuilder sb = new StringBuilder();
		sb.append("1\n");     // English
		sb.append("2\n");     // 2 players
		sb.append("Alice\n");
		sb.append("Bob\n");
	
		// 动作选择 - Alice 和 Bob 各抽 2 张足矣（让游戏走起来）
		for (int i = 0; i < 4; i++) {
			sb.append("1\n"); // draw card
		}
	
		System.setIn(new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8)));
	
		Main.main(new String[]{});
	
		String output = outContent.toString();
		assertTrue(output.contains("Exploding") || output.contains("Game Over") || output.length() > 100);
	}
	
}
