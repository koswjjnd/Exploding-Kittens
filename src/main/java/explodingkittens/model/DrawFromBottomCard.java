package explodingkittens.model;

import java.util.List;
import explodingkittens.controller.GameContext;
import explodingkittens.service.TurnService;
import explodingkittens.service.CardEffectService;
import explodingkittens.view.ConsoleGameView;
import explodingkittens.view.GameView;

/**
 * Represents the Draw From Bottom card.
 * Ends your turn by drawing the bottom card from the draw pile.
 * If played as a defense to an Attack Card, each card ends 1 attack turn.
 */
public class DrawFromBottomCard extends Card {
    private final GameView view;

    public DrawFromBottomCard() {
        super(CardType.DRAW_FROM_BOTTOM);
        this.view = new ConsoleGameView();
    }

    /**
     * Creates a new DrawFromBottomCard with the specified view.
     * @param view The view to use for displaying game information
     */
    public DrawFromBottomCard(GameView view) {
        super(CardType.DRAW_FROM_BOTTOM);
        this.view = view;
    }

    /**
     * Executes the effect of the Draw From Bottom card.
     * Draws the bottom card from the deck and gives it to the current player.
     *
     * @param turnOrder The list of players in turn order (current player is first)
     * @param deck The game deck
     * @throws IllegalStateException if the deck is empty
     */
    @Override
    public void effect(List<Player> turnOrder, Deck deck) {
        if (turnOrder == null || turnOrder.isEmpty()) {
            throw new IllegalArgumentException("Turn order cannot be null or empty");
        }
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        if (deck.getCards().isEmpty()) {
            throw new IllegalStateException(
                "Cannot draw from an empty deck"
            );
        }
        Player currentPlayer = turnOrder.get(0);
        
        // Remove and get the bottom card using removeBottomCard()
        Card bottomCard = deck.removeBottomCard();

        // Display the drawn card using the view
        view.displayCardDrawnFromBottom(bottomCard);

        // Check if the drawn card is an Exploding Kitten
        if (bottomCard instanceof ExplodingKittenCard) {
            // Handle the Exploding Kitten
            TurnService turnService = new TurnService(
                new ConsoleGameView(), 
                new CardEffectService(new ConsoleGameView())
                );
            turnService.handleExplodingKitten(currentPlayer, (ExplodingKittenCard) bottomCard);
        } 
        else {
            // Add the card to the player's hand
            currentPlayer.receiveCard(bottomCard);
        }

        // End current player's turn by setting leftTurns to 0
        // Let TurnService handle the turn end logic
        currentPlayer.setLeftTurns(0);
    }
}