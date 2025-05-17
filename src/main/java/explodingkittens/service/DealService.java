package explodingkittens.service;

import explodingkittens.model.Deck;
import explodingkittens.model.DefuseCard;
import explodingkittens.model.Card;
import explodingkittens.model.Player;
import explodingkittens.exceptions.InvalidDeckException;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InsufficientDefuseCardsException;
import explodingkittens.exceptions.TooManyPlayersException;
import explodingkittens.exceptions.InvalidPlayersListException;
import explodingkittens.exceptions.EmptyPlayersListException;
import explodingkittens.exceptions.TooFewPlayersException;
import explodingkittens.exceptions.NoDefuseCardsException;
import java.util.List;
import java.util.Map;

public class DealService {
    private final DrawService drawService;
    
    public DealService() {
        this.drawService = new DrawService();
    }
    
    public DealService(DrawService drawService) {
        this.drawService = drawService;
    }
    
    /**
     * Deals defuse cards to players.
     * @param deck the deck to deal from
     * @param players the list of players to deal to
     * @throws InvalidDeckException if the deck is null
     * @throws EmptyDeckException if the deck is empty
     * @throws TooManyPlayersException if there are more than 4 players
     * @throws InvalidPlayersListException if the players list is null
     * @throws EmptyPlayersListException if the players list is empty
     * @throws TooFewPlayersException if there are fewer than 2 players
     */
    public void dealDefuses(Deck deck, List<Player> players) {
        if (deck == null) throw new InvalidDeckException();
        if (players == null) throw new InvalidPlayersListException();
        if (players.isEmpty()) throw new EmptyPlayersListException();
        if (players.size() < 2) throw new TooFewPlayersException();
        if (players.size() > 4) throw new TooManyPlayersException();
        if (deck.isEmpty()) throw new EmptyDeckException();
        
        players.forEach(player -> player.receiveCard(new DefuseCard()));
    }

    /**
     * Deals initial hands to players.
     * @param deck the deck to deal from
     * @param players the list of players to deal to
     * @throws InvalidDeckException if the deck is null
     * @throws EmptyDeckException if the deck is empty
     * @throws TooManyPlayersException if there are more than 4 players
     * @throws TooFewPlayersException if there are fewer than 2 players
     */
    public void dealInitialHands(Deck deck, List<Player> players) {
        if (deck == null) {
            throw new InvalidDeckException();
        }
        if (deck.isEmpty()) {
            throw new EmptyDeckException();
        }
        if (players.size() > 4) {
            throw new TooManyPlayersException();
        }
        if (players.size() < 2) {
            throw new TooFewPlayersException();
        }

        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                Card card = drawService.drawCard(deck);
                player.receiveCard(card);
            }
        }
    }
} 