package explodingkittens.model;

import java.util.List;

/**
 * Represents an Exploding Kitten card in the game.
 * When a player draws this card, they must either:
 * 1. Use a Defuse card to survive and place this card back in the deck
 * 2. Be eliminated from the game if they don't have a Defuse card
 */
public class ExplodingKittenCard extends Card {
    /**
     * Constructor for ExplodingKittenCard.
     */
    public ExplodingKittenCard() {
        super(CardType.EXPLODING_KITTEN);
    }

    /**
     * The effect of the ExplodingKittenCard.
     * @param turnOrder The order of players in the game.
     * @param gameDeck The deck of cards in the game.
     */
    @Override
    public void effect(List<Player> turnOrder, Deck gameDeck) {
        // 如果当前玩家没有拆弹卡，则将其标记为死亡
        Player currentPlayer = turnOrder.get(0);
        if (!currentPlayer.hasDefuse()) {
            currentPlayer.setAlive(false);
        }
    }
} 