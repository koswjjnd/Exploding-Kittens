package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.controller.GameContext;

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
     */
    public void handleCardEffect(Card card, Class<GameContext> ctx) {
        if (card == null || ctx == null) {
            throw new IllegalArgumentException("Card and GameContext cannot be null");
        }
        // TODO: Implement card effect handling
    }
} 