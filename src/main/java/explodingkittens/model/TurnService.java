package explodingkittens.model;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.service.CardEffectService;
import explodingkittens.view.GameView;
import java.util.List;

public class TurnService {
    private final GameView view;
    private final CardEffectService cardEffectService;

    public TurnService(GameView view, CardEffectService cardEffectService) {
        this.view = view;
        this.cardEffectService = cardEffectService;
    }
    
    /**
     * Executes a player's turn in the game.
     * 
     * @param player The player taking the turn
     * @param ctx The game context
     * @throws IllegalArgumentException if player or ctx is null
     * @throws EmptyDeckException if the deck is empty when trying to draw
     */
    public void takeTurn(Player player, GameContext ctx) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("GameContext cannot be null");
        }

        // Process cards in hand
        List<Card> hand = player.getHand();
        if (!hand.isEmpty()) {
            while (true) {
                // Get player's choice from view
                Card selectedCard = view.selectCardToPlay(player, hand);
                if (selectedCard == null) {
                    break; // Player chooses to end their turn
                }
                
                try {
                    playCard(player, selectedCard, ctx);
                } catch (InvalidCardException e) {
                    view.showError(e.getMessage());
                    continue;
                }
            }
        }

        // Draw phase
        drawCard(player, ctx);
        
        // Update turns
        player.decrementLeftTurns();
    }

    /**
     * Processes a single card play.
     * 
     * @param player The player playing the card
     * @param card The card to play
     * @param ctx The game context
     * @throws InvalidCardException if the card cannot be played
     */
    public void playCard(Player player, Card card, GameContext ctx) throws InvalidCardException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("GameContext cannot be null");
        }

        // Show the card being played
        view.showCardPlayed(player, card);
        
        // Check for Nope cards
        if (view.checkForNope(player, card)) {
            view.showCardNoped(player, card);
            player.getHand().remove(card);
            return;
        }
        
        // Execute card effect
        cardEffectService.executeCardEffect(card, player, ctx);
        
        // Remove card from hand
        player.getHand().remove(card);
    }

    /**
     * Draws a card and processes it.
     * 
     * @param player The player drawing the card
     * @param ctx The game context
     * @throws EmptyDeckException if the deck is empty
     */
    public void drawCard(Player player, GameContext ctx) throws EmptyDeckException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("GameContext cannot be null");
        }

        try {
            Card drawnCard = ctx.getGameDeck().drawOne();
            view.showCardDrawn(player, drawnCard);
            
            if (drawnCard instanceof ExplodingKittenCard) {
                handleExplodingKitten(player, (ExplodingKittenCard) drawnCard, ctx);
            } else {
                player.receiveCard(drawnCard);
            }
        } catch (EmptyDeckException e) {
            throw new EmptyDeckException("Cannot draw card from empty deck");
        }
    }

    /**
     * Handles the case when a player draws an Exploding Kitten.
     */
    private void handleExplodingKitten(Player player, ExplodingKittenCard card, GameContext ctx) {
        if (player.hasDefuse()) {
            if (view.confirmDefuse(player)) {
                player.useDefuse();
                int position = view.selectExplodingKittenPosition();
                GameContext.getGameDeck().insertAt(card, position);
            } else {
                player.setAlive(false);
            }
        } else {
            player.setAlive(false);
        }
    }
}
