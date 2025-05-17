package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.Player;
import explodingkittens.model.Card;
import explodingkittens.model.DefuseCard;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidPlayersListException;
import explodingkittens.exceptions.EmptyPlayersListException;
import java.util.List;

/**
 * Service class for dealing cards to players.
 */
public class DealService {
    /**
     * Deals defuse cards to players.
     *
     * @param deck     the deck to deal from
     * @param players  the list of players to deal to
     * @throws InvalidDeckException      if the deck is invalid
     * @throws EmptyDeckException        if the deck is empty
     * @throws InvalidPlayersListException if the players list is null
     * @throws EmptyPlayersListException  if the players list is empty
     */
    public void dealDefuses(Deck deck, List<Player> players) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        if (players == null) {
            throw new InvalidPlayersListException();
        }
        if (players.isEmpty()) {
            throw new EmptyPlayersListException();
        }

        for (Player player : players) {
            Card card = deck.removeTopCard();
            if (card instanceof DefuseCard) {
                player.receiveCard(card);
            }
            else {
                throw new InvalidDeckException();
            }
        }
    }

    /**
     * Deals initial hands to players.
     *
     * @param deck     the deck to deal from
     * @param players  the list of players to deal to
     * @param n        the number of cards to deal to each player
     * @throws InvalidDeckException      if the deck is invalid
     * @throws EmptyDeckException        if the deck is empty
     * @throws InvalidPlayersListException if the players list is null
     * @throws EmptyPlayersListException  if the players list is empty
     */
    public void dealInitialHands(Deck deck, List<Player> players, int n) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        if (players == null) {
            throw new InvalidPlayersListException();
        }
        if (players.isEmpty()) {
            throw new EmptyPlayersListException();
        }
        if (deck.isEmpty()) {
            throw new EmptyDeckException();
        }

        for (Player player : players) {
            for (int i = 0; i < n; i++) {
                Card card = deck.removeTopCard();
                player.receiveCard(card);
            }
        }
    }
} 