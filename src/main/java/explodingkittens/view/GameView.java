package explodingkittens.view;

import explodingkittens.model.Player;
import explodingkittens.model.Card;
import java.util.List;

/**
 * Interface for the game view that handles all user interactions and display.
 */
public interface GameView {
    /**
     * Displays the current player.
     * @param player the current player
     */
    void displayCurrentPlayer(Player player);

    /**
     * Displays a player's hand.
     * @param player the player whose hand to display
     */
    void displayPlayerHand(Player player);

    /**
     * Displays a player's hand without card indices (for other players' hands).
     * @param player the player whose hand to display
     */
    void displayOtherPlayerHand(Player player);

    /**
     * Displays a player's hand for card selection.
     * @param player the player whose hand to display
     * @param hand the list of cards to display
     */
    void displayHandForSelection(Player player, List<Card> hand);

    /**
     * Prompts the player for their action.
     * @param player the current player
     * @return the action chosen by the player ("draw" or "play")
     */
    String promptPlayerAction(Player player);

    /**
     * Displays when a player is eliminated.
     * @param player the eliminated player
     */
    void displayPlayerEliminated(Player player);

    /**
     * Displays the winner of the game.
     * @param winner the winning player
     */
    void displayWinner(Player winner);

    /**
     * Displays the game over message.
     */
    void displayGameOver();

    /**
     * Displays the result of drawing a card.
     * @param card the card that was drawn
     */
    void displayDrawResult(Card card);

    /**
     * Prompts the player to choose a position to insert the defuse card.
     * @param deckSize the size of the deck
     * @return the position chosen by the player
     */
    int promptDefusePosition(int deckSize);

    /**
     * Prompts the player to choose a card to play.
     * @param player the player
     * @param hand the player's hand
     * @return the card chosen by the player
     */
    Card promptPlayCard(Player player, List<Card> hand);

    /**
     * Displays when a player plays a card.
     * @param player the player who played the card
     * @param card the card that was played
     */
    void displayPlayedCard(Player player, Card card);

    /**
     * Prompts the player to select a card to play.
     * @param player The current player
     * @param hand The player's hand
     * @return The selected card, or null if player chooses to end turn
     */
    Card selectCardToPlay(Player player, List<Card> hand);

    /**
     * Shows an error message to the player.
     * @param message The error message
     */
    void showError(String message);

    /**
     * Shows that a card has been played.
     * @param player The player who played the card
     * @param card The card that was played
     */
    void showCardPlayed(Player player, Card card);

    /**
     * Checks if any player wants to play a Nope card.
     * @param player The player who played the card
     * @param card The card that was played
     * @return true if a Nope card was played, false otherwise
     */
    boolean checkForNope(Player player, Card card);

    /**
     * Shows that a card was noped.
     * @param player The player whose card was noped
     * @param card The card that was noped
     */
    void showCardNoped(Player player, Card card);

    /**
     * Shows that a card was drawn.
     * @param player The player who drew the card
     * @param card The card that was drawn
     */
    void showCardDrawn(Player player, Card card);

    /**
     * Confirms if the player wants to use their defuse card.
     * @param player The player who drew the exploding kitten
     * @return true if player wants to use defuse, false otherwise
     */
    boolean confirmDefuse(Player player);

    /**
     * Prompts the player to select a position to insert the exploding kitten.
     * @param deckSize the size of the deck
     * @return The selected position (0-based)
     */
    int selectExplodingKittenPosition(int deckSize);

    /**
     * Displays when a player uses a defuse card.
     * @param player the player who used the defuse card
     */
    void displayDefuseUsed(Player player);

    /**
     * Displays when a defuse card is successfully used and the exploding kitten is inserted.
     * @param player the player who used the defuse card
     * @param position the position where the exploding kitten was inserted
     */
    void displayDefuseSuccess(Player player, int position);

    /**
     * Prompts the player to play a Nope card.
     * @param player The player who played the card
     * @param card The card that was played
     * @return true if a Nope card was played, false otherwise
     */
    boolean promptPlayNope(Player player, Card card);
    
    /**
     * Displays when a Nope card was played.
     * @param player The player who played the Nope card
     */
    void displayPlayedNope(Player player);

    /**
     * Displays the current player's turn.
     * @param player the player whose turn it is
     */
    void showCurrentPlayerTurn(Player player);

    /**
     * 显示猫卡效果
     * @param effectType 效果类型（"steal" 或 "request"）
     * @param sourcePlayer 源玩家
     * @param targetPlayer 目标玩家
     */
    void displayCatCardEffect(String effectType, Player sourcePlayer, Player targetPlayer);

    /**
     * 显示卡牌被偷取
     * @param sourcePlayer 偷取卡牌的玩家
     * @param targetPlayer 被偷取卡牌的玩家
     * @param card 被偷取的卡牌
     */
    void displayCardStolen(Player sourcePlayer, Player targetPlayer, Card card);

    /**
     * 显示卡牌被请求
     * @param sourcePlayer 请求卡牌的玩家
     * @param targetPlayer 被请求卡牌的玩家
     * @param card 被请求的卡牌
     */
    void displayCardRequested(Player sourcePlayer, Player targetPlayer, Card card);

    /**
     * 选择目标玩家
     * @param availablePlayers 可用的目标玩家列表
     * @return 选择的目标玩家
     */
    Player selectTargetPlayer(List<Player> availablePlayers);

    /**
     * 从玩家手牌中选择一张卡牌
     * @param targetPlayer 目标玩家
     * @param hand 手牌列表
     * @return 选择的卡牌
     */
    Card selectCardFromPlayer(Player targetPlayer, List<Card> hand);
} 