package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;

class CatCardTest {
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player currentPlayer;
    private Player targetPlayer;
    private CatCard catCard1;
    private CatCard catCard2;
    private CatCard catCard3;
    private ConsoleCardStealInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        currentPlayer = new Player("Player1");
        targetPlayer = new Player("Player2");
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);

        // Create different types of cat cards
        catCard1 = new CatCard(CatType.TACOCAT);
        catCard2 = new CatCard(CatType.TACOCAT);
        catCard3 = new CatCard(CatType.BEARD_CAT);
    }

    private void setupInputHandler(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        inputHandler = new ConsoleCardStealInputHandler(new java.util.Scanner(System.in));
        CatCard.setInputHandler(inputHandler);
    }

    @Test
    @DisplayName("Test when player has no cat cards")
    void testNoCatCards() {
        assertThrows(IllegalStateException.class, () -> {
            catCard1.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no cat cards");
    }

} 