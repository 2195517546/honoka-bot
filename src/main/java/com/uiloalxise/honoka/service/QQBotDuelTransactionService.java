package com.uiloalxise.honoka.service;

import com.uiloalxise.pojo.dto.DuelPlayerDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Uiloalxise
 * @ClassName QQBotDuelTransactionService
 * @Description TODO
 */
public interface QQBotDuelTransactionService {
    /**
     * 更新输赢数据
     * @param winner
     * @param loser
     * @param cost
     */
    void updateDuelResults(DuelPlayerDTO winner, DuelPlayerDTO loser, Long cost);

    /**
     * 更新名字数据
     * @param duelPlayerDTO
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
