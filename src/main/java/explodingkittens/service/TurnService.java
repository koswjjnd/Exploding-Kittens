package explodingkittens.service;

import explodingkittens.controller.GameContext;
import explodingkittens.exceptions.EmptyDeckException;
import explodingkittens.exceptions.InvalidCardException;
import explodingkittens.model.Card;
import explodingkittens.model.CardType;
import explodingkittens.model.ExplodingKittenCard;
import explodingkittens.model.Player;
import explodingkittens.view.GameView;

import java.util.List;

/**
 * Service that drives a single *sub-turn* for the current player:
 * 1. let player play cards
 * 2. draw a card and handle(exploding or common card)
 * 3. leftTurns--
 *
 * the outer turn loop is handled by GameController.
 */
public class TurnService {

    private final GameView view;
    private final CardEffectService cardEffectService;
    private final NopeService nopeService;

    public TurnService(GameView view, CardEffectService cardEffectService) {
        this.view = view;
        this.cardEffectService = cardEffectService;
        this.nopeService = new NopeService(view);     // NopeService needs view for interaction
    }

    /* ===================================================================== */

    /**
     * execute a sub-turn
     * @param player The player taking the turn
     * @throws IllegalArgumentException if player is null
     */
    public void takeTurn(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        // Display player's hand before their turn
        view.displayPlayerHand(player);

        // First, let player choose to play cards or draw
        String action = view.promptPlayerAction(player);
        if (action.equals("play")) {
            playCardsPhase(player);    // ① play cards (can be multiple)
        }

        // Then, draw cards based on leftTurns
        int drawsLeft = player.getLeftTurns();
        for (int i = 0; i < drawsLeft; i++) {
            drawPhase(player);         // ② draw card/exploding kitten
            if (GameContext.isGameOver()) {
                break;
            }
        }
        
        // Finally, end the sub-turn and adjust player order
        player.setLeftTurns(1);  // Reset leftTurns to 1
        GameContext.movePlayerToEnd(player); // Move current player to end of turn order
        
        // Move to next player's turn
        if (!GameContext.isGameOver()) {
            GameContext.nextTurn();
        }
    }

    /* ===================== ① 出牌阶段 ==================================== */
    /**
     * play cards phase
     * @param player The player taking the turn
     * @throws IllegalArgumentException if player is null
     */
    public void playCardsPhase(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player must not be null");
        }
        while (true) {
            Card chosen = view.selectCardToPlay(player, player.getHand());   // 用最新手牌
            if (chosen == null) {
                break;
            }

            try {
                playCard(player, chosen);
                view.displayPlayerHand(player); // 出牌后刷新手牌显示
            } 
            catch (InvalidCardException ice) {
                view.showError(ice.getMessage());
                // continue to let player choose again
            }
        }
    }

    /**
     * play a card
     * @param player The player taking the turn
     * @param card The card to play
     * @throws IllegalArgumentException if player or card is null
     * @throws InvalidCardException if the card is invalid
     */
    public void playCard(Player player, Card card) throws InvalidCardException {
        if (player == null || card == null) {
            throw new IllegalArgumentException("Player / card must not be null");
        }

        /* —— show played card —— */
        view.showCardPlayed(player, card);

        /* —— Nope check —— */
        boolean noped = nopeService.isNegatedByPlayers(card);
        if (noped) {
            view.showCardNoped(player, card);
            player.removeCard(card);  // the card is played but the effect is invalid
            return;
        }

        /* —— apply effect —— */
        cardEffectService.applyEffect(card, player);

        /* —— remove card from hand —— */
        player.removeCard(card);
    }

    /* ===================== ② 抽牌阶段 ==================================== */
    /**
     * draw phase
     * @param player The player taking the turn
     * @throws IllegalArgumentException if player is null
     * @throws EmptyDeckException if the deck is empty
     */
    public void drawPhase(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player must not be null");
        }
        if (GameContext.getGameDeck() == null) {
            throw new IllegalArgumentException("Game deck is not initialized");
        }
        try {
            Card drawn = GameContext.getGameDeck().drawOne();
            view.showCardDrawn(player, drawn);

            if (drawn instanceof ExplodingKittenCard) {
                handleExplodingKitten(player, (ExplodingKittenCard) drawn);
            } 
            else {
                player.receiveCard(drawn);
            }
        } 
        catch (EmptyDeckException ede) {
            // 按官方规则：抽完牌堆其实游戏就结束；这里抛给上层即可
            throw ede;
        }
    }

    /* ------------ 炸猫处理 ------------ */
    /**
     * handle exploding kitten
     * @param player The player taking the turn
     * @param ek The exploding kitten card
     */
    public void handleExplodingKitten(Player player, ExplodingKittenCard ek) {
        if (player.hasDefuse() && view.confirmDefuse(player)) {
            /* 使用 Defuse → 重新塞回牌堆指定位置 */
            player.useDefuse();
            int pos = view.selectExplodingKittenPosition(GameContext.getGameDeck().size());
            GameContext.getGameDeck().insertAt(ek, pos);
            view.displayDefuseSuccess(player, pos);
        } 
        else {
            /* 没有(或拒绝) Defuse → 淘汰 */
            player.setAlive(false);
            view.displayPlayerEliminated(player);
        }
    }
}
