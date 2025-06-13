package explodingkittens.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import explodingkittens.controller.CatCardStealInputHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyInt;

class FeralCatCardTest {
    private FeralCatCard feralCatCard;
    private List<Player> turnOrder;
    @Mock private Player currentPlayer;
    @Mock private Player targetPlayer;
    private List<Card> currentPlayerHand;
    private List<Card> targetPlayerHand;
    @Mock private Deck gameDeck;

    @Mock
    private CatCardStealInputHandler inputHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feralCatCard = new FeralCatCard();
        turnOrder = new ArrayList<>();
        currentPlayerHand = new ArrayList<>();
        targetPlayerHand = new ArrayList<>();
        
        when(currentPlayer.getHand()).thenReturn(currentPlayerHand);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(currentPlayer.getName()).thenReturn("Current");
        when(targetPlayer.getName()).thenReturn("Target");
        when(currentPlayer.getLeftTurns()).thenReturn(1);
        
        turnOrder.add(currentPlayer);
        turnOrder.add(targetPlayer);
        
        CatCard.setInputHandler(inputHandler);
    }

    @Test
    void testFeralCatCardCreation() {
        assertEquals(CatType.FERAL_CAT, feralCatCard.getCatType());
        assertEquals(CardType.CAT_CARD, feralCatCard.getType());
    }

    @Test
    void testFindCatCardPairWithValidCards() {
        // Add a TacoCat and a FeralCat to the hand
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());

        CatCard[] pair = feralCatCard.findCatCardPair(currentPlayerHand);
        assertTrue(pair[0] instanceof TacoCatCard);
        assertTrue(pair[1] instanceof FeralCatCard);
    }
    
    @Test
    void testFindCatCardPairWithNoOtherCatCard() {
        // Add only FeralCat cards
        currentPlayerHand.add(new FeralCatCard());
        currentPlayerHand.add(new FeralCatCard());

        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testFindCatCardPairWithNoFeralCatCard() {
        // Add only other cat cards
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new TacoCatCard());

        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testFindCatCardPairWithEmptyHand() {
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.findCatCardPair(currentPlayerHand);
        });
    }

    @Test
    void testEffectWithValidCards() {
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        targetPlayerHand.add(new SkipCard());

        // 设置输入处理器行为
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(0);
        when(targetPlayer.isAlive()).thenReturn(true);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(targetPlayer.getName()).thenReturn("Target");

        // 执行效果
        CatCard.CatCardEffect effect = assertThrows(CatCard.CatCardEffect.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });

        // 验证效果
        assertTrue(effect.getFirstCard() instanceof TacoCatCard);
        assertTrue(effect.getSecondCard() instanceof FeralCatCard);
        assertEquals("Target", effect.getTargetPlayerName());
        assertEquals(0, effect.getTargetCardIndex());
    }

    @Test
    void testEffectWithNoInputHandler() {
        // 清除输入处理器
        CatCard.setInputHandler(null);
        
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when no input handler is set");
    }

    @Test
    void testEffectWithNoTurnsLeft() {
        // 设置玩家没有回合
        when(currentPlayer.getLeftTurns()).thenReturn(0);
        
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when player has no turns left");
    }

    @Test
    void testEffectWithNoAvailableTargets() {
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        
        // 移除目标玩家
        turnOrder.remove(targetPlayer);

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when no valid target players available");
    }

    @Test
    void testEffectWithDeadTargetPlayer() {
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        
        // 设置目标玩家已死亡
        when(targetPlayer.isAlive()).thenReturn(false);

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player is dead");
    }

    @Test
    void testEffectWithEmptyTargetHand() {
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        
        // 设置目标玩家手牌为空
        when(targetPlayer.getHand()).thenReturn(new ArrayList<>());

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when target player has no cards");
    }

    @Test
    void testEffectWithInvalidCardPair() {
        // 只添加一张卡牌
        currentPlayerHand.add(new TacoCatCard());

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        }, "Should throw exception when no valid card pair is found");
    }

    @Test
    void testEffectWithInvalidTargetSelection() {
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        targetPlayerHand.add(new SkipCard());

        // 设置输入处理器返回无效目标
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(null);
        when(targetPlayer.isAlive()).thenReturn(true);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);

        // 验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });
        assertEquals("Invalid target player selection", exception.getMessage());
    }

    @Test
    void testEffectWithInvalidCardIndex() {
        // 设置手牌
        currentPlayerHand.add(new TacoCatCard());
        currentPlayerHand.add(new FeralCatCard());
        targetPlayerHand.add(new SkipCard());

        // 设置输入处理器返回无效的卡牌索引
        when(inputHandler.selectTargetPlayer(anyList())).thenReturn(targetPlayer);
        when(inputHandler.selectCardIndex(anyInt())).thenReturn(1); // 索引超出范围
        when(targetPlayer.isAlive()).thenReturn(true);
        when(targetPlayer.getHand()).thenReturn(targetPlayerHand);
        when(targetPlayer.getName()).thenReturn("Target");

        // 验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feralCatCard.effect(turnOrder, gameDeck);
        });
        assertEquals("Invalid card index selection", exception.getMessage());
    }
} 

    


