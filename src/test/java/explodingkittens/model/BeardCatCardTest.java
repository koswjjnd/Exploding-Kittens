package com.example.courseproject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import explodingkittens.model.BeardCatCard;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.CatType;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.model.RainbowCatCard;
import explodingkittens.model.exceptions.CatCardEffect;
import explodingkittens.model.input.MockInputHandler;

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

    @Test
    void testConstructor() {
        assertNotNull(card);
        assertEquals(CatType.BEARD_CAT, card.getCatType());
        assertEquals(CardType.CAT_CARD, card.getType());
    }

    @Test
    void testEffectWithTwoBeardCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.addCardToHand(card);
        player1.addCardToHand(new BeardCatCard());
        player1.setTurnsLeft(1);
        player2.addCardToHand(new Card(CardType.DEFUSE));
        card.setInputHandler(new MockInputHandler());
        assertThrows(CatCardEffect.class, () -> card.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithDifferentCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.addCardToHand(card);
        player1.addCardToHand(new RainbowCatCard());
        player1.setTurnsLeft(1);
        card.setInputHandler(new MockInputHandler());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, gameDeck));
    }

    @Test
    void testEffectWithThreeBeardCatCards() {
        turnOrder.add(player1);
        turnOrder.add(player2);
        player1.addCardToHand(card);
        player1.addCardToHand(new BeardCatCard());
        player1.addCardToHand(new BeardCatCard());
        player1.setTurnsLeft(1);
        card.setInputHandler(new MockInputHandler());
        assertThrows(IllegalStateException.class, () -> card.effect(turnOrder, gameDeck));
    }
} 