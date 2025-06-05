// package explodingkittens.controller;

// import explodingkittens.model.*;
// import explodingkittens.view.GameView;
// import explodingkittens.exceptions.GameOverException;
// import org.junit.jupiter.api.*;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// class GameControllerTest {

//     /* ---------- mocks & sut ---------- */
//     private GameView view;
//     private GameController gameController;

//     /* ---------- shared fixtures ---------- */
//     private List<Player> players;
//     private Deck deck;

//     /* ---------- test lifecycle ---------- */
//     @BeforeEach
//     void setUp() {
//         // 1. mock view
//         view = mock(GameView.class);

//         // 2. system-under-test
//         gameController = new GameController(view);

//         // 3. minimal deck（无炸猫，避免提前结束）
//         deck = new Deck();
//         deck.addCard(new BasicCard(CardType.SKIP));
//         deck.addCard(new BasicCard(CardType.SKIP));

//         // 4. 创建两个玩家
//         players = new ArrayList<>();
//         players.add(new Player("P1"));
//         players.add(new Player("P2"));

//         // 5. 写入 GameContext
//         resetGameContext(players, deck, false);
//     }

//     @AfterEach
//     void tearDown() {
//         resetGameContext(new ArrayList<>(), new Deck(), false);
//     }

//     /* ===================================================================== */
//     /*                                TESTS                                  */
//     /* ===================================================================== */

//     /** 验证正常运行至少展示一次当前玩家 */
//     @Test
//     void testGameRunsAndShowsCurrentPlayer() throws GameOverException {
//         /* 每次 prompt 后立即结束游戏以防死循环 */
//         when(view.promptPlayerAction(any())).thenAnswer(inv -> {
//             GameContext.setGameOver(true);
//             return "draw";
//         });

//         gameController.start();

//         verify(view, atLeastOnce()).displayCurrentPlayer(any());
//     }

//     /** 验证内部异常被包装成 GameOverException 抛出 */
//     @Test
//     void testGameOverExceptionPropagation() {
//         when(view.promptPlayerAction(any()))
//                 .thenThrow(new RuntimeException("boom"));

//         assertThrows(GameOverException.class, () -> gameController.start());
//     }

//     /** P2 在自己的回合被淘汰，应调用 displayPlayerEliminated */
//     @Test
//     void testPlayerElimination() throws GameOverException {
//         /* 当 currentPlayer == P2 时将其置死并结束游戏 */
//         when(view.promptPlayerAction(any())).thenAnswer(inv -> {
//             Player cur = inv.getArgument(0);
//             if ("P2".equals(cur.getName())) {
//                 cur.setAlive(false);           // 淘汰
//                 GameContext.setGameOver(true); // 结束
//             }
//             return "draw";
//         });

//         gameController.start();

//         verify(view).displayPlayerEliminated(players.get(1));
//     }

//     /** 只剩一名玩家时应调用 displayWinner */
//     @Test
//     void testShowWinnerAtGameOver() throws GameOverException {
//         /* 只有一个玩家即可直接结束并判定胜者 */
//         List<Player> solo = List.of(new Player("Solo"));
//         resetGameContext(new ArrayList<>(solo), deck, false);

//         // 让 prompt 立即把 gameOver 置 true
//         when(view.promptPlayerAction(any())).thenAnswer(inv -> {
//             GameContext.setGameOver(true);
//             return "draw";
//         });

//         gameController.start();

//         verify(view).displayWinner(solo.get(0));
//     }

//     /* ===================================================================== */
//     /*                             helper methods                            */
//     /* ===================================================================== */

//     private static void resetGameContext(List<Player> turnOrder, Deck deck, boolean over) {
//         GameContext.setTurnOrder(turnOrder);
//         GameContext.setGameDeck(deck);
//         GameContext.setGameOver(over);
//     }
// }
