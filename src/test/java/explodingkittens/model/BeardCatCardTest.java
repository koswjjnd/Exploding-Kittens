package com.example.courseproject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class BeardCatCardTest {
    private BeardCatCard card;
    private List<Player> turnOrder;
    private Deck gameDeck;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        card = new BeardCatCard();
        turnOrder = new ArrayList<>();
        gameDeck = new Deck();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

} 