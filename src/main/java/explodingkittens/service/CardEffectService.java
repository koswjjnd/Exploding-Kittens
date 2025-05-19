package explodingkittens.service;

import explodingkittens.model.Card;
import explodingkittens.controller.GameContext;
import explodingkittens.model.CardType;
import explodingkittens.model.Deck;
import explodingkittens.model.Player;

import java.util.List;

/**
 * Service class that handles the effects of different cards in the game.
 * This service is responsible for executing the effects of cards when they are played.
 */
public class CardEffectService {
    
    private final AttackService attackService;
    
    /**
     * Constructs a CardEffectService with the required services.
     * 
     * @param attackService The service for handling attack card effects
     */
    public CardEffectService(AttackService attackService) {
        this.attackService = attackService;
    }
    
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
        
        switch (card.getType()) {
            case ATTACK:
                handleAttack(turnOrder);
                break;
            // TODO: Handle other card types
        }
    }
    
    /**
     * Handles the effect of an attack card.
     * When an attack card is played, the next player gets two additional turns.
     * 
     * @param turnOrder The list of players in the current turn order
     */
    private void handleAttack(List<Player> turnOrder) {
        attackService.handleAttack(turnOrder, 2);
    }
} 