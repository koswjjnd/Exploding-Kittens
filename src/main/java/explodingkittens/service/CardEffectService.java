package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.controller.GameContext;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;

import java.util.List;

/**
 * Service class that handles the effects of different cards in the game.
 * This service is responsible for executing the effects of cards when they are played.
 */
public class CardEffectService {
    
    /**
     * Handles the effect of a card when it is played.
     *
     * @param card The card to handle the effect for
     * @param ctx The game context class
     * @throws IllegalArgumentException if either card or ctx is null
     * @throws IllegalStateException if the game context is not properly initialized
     */
    public void handleCardEffect(Card card, Class<GameContext> ctx) {
        if (card == null || ctx == null) {
            throw new IllegalArgumentException("Card and GameContext cannot be null");
        }

        List<Player> turnOrder = GameContext.getTurnOrder();
        Deck gameDeck = GameContext.getGameDeck();

        if (turnOrder == null || gameDeck == null) {
            throw new IllegalStateException("Game context is not properly initialized");
        }

        card.effect(turnOrder, gameDeck);
    }

    /**
     * Handles the effect of a card when it is played.
     *
     * @param card The card to handle the effect for
     * @param player The player who played the card
     * @param ctx The game context class
     * @throws IllegalArgumentException if either card or ctx is null
     * @throws IllegalStateException if the game context is not properly initialized
     */
    public void executeCardEffect(Card card, Player player, GameContext ctx) {
       
    }
}