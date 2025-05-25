package explodingkittens.view;

import explodingkittens.model.Card;
import explodingkittens.model.AttackCard;
import explodingkittens.model.SkipCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeeTheFutureViewTest {

    private SeeTheFutureView view;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        view = new SeeTheFutureView();
        System.setOut(new PrintStream(outContent)); // 拦截 System.out
    }

    @Test
    public void testDisplayEmptyList() {
        view.display(List.of());
        String output = outContent.toString();

        // 检查有标题
        assertTrue(output.contains("You see the future cards"));
        // 检查没有具体卡牌类型打印
        assertTrue(!output.contains("- "));
    }

    @Test
    public void testDisplayOneCard() {
        List<Card> cards = List.of(new AttackCard());
        view.display(cards);

        String output = outContent.toString();

        assertTrue(output.contains("You see the future cards"));
        assertTrue(output.contains("ATTACK"));
    }

    
}
