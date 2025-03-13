package com.uiloalxise.honoka.service;

import com.uiloalxise.pojo.dto.DuelPlayerDTO;

/**
 * @author Uiloalxise
 * @ClassName QQBotDuelTransactionService
 * @Description QQ决斗事务服务类
 */
public interface QQBotDuelTransactionService {
    /**
     * 更新输赢数据
     * @param winner 胜利者
     * @param loser 败者
     * @param cost 花费
     */
    void updateDuelResults(DuelPlayerDTO winner, DuelPlayerDTO loser, Long cost);

    /**
     * 更新名字数据
     * @param duelPlayerDTO 用户dto
     */
    void updateName(DuelPlayerDTO duelPlayerDTO);

    /**
     * 添加新数据
     */
    void register(DuelPlayerDTO duelPlayerDTO);

    /**
     * 一键审核所有名字
     */
    void processAll();
}
