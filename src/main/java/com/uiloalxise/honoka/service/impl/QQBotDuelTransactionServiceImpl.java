package com.uiloalxise.honoka.service.impl;

import com.uiloalxise.honoka.mapper.QQDuelMapper;
import com.uiloalxise.honoka.service.QQBotDuelTransactionService;
import com.uiloalxise.pojo.dto.DuelPlayerDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务管理类
 * @author Uiloalxise
 * @ClassName QQBotDuelTransactionServiceImpl
 * @Description QQ决斗事务服务类实现
 */
@Service
@Transactional
@Async
@Slf4j
public class QQBotDuelTransactionServiceImpl implements QQBotDuelTransactionService {

    @Resource
    private QQDuelMapper qqDuelMapper;

    @Override
    public void updateDuelResults(DuelPlayerDTO winner, DuelPlayerDTO loser, Long cost) {
        try {
            winner.setMoney(winner.getMoney() + cost);
            winner.setWin(winner.getWin() + 1);
            loser.setMoney(loser.getMoney() - cost);
            loser.setLost(loser.getLost() + 1);
            qqDuelMapper.updateDuelPlayerByOpenIdExceptName(winner);
            qqDuelMapper.updateDuelPlayerByOpenIdExceptName(loser);
            log.info("数据更新完毕");
        }catch (Exception e) {
            log.error("数据更新错误:{}",e.getMessage());
        }
    }

    @Override
    public void updateName(DuelPlayerDTO duelPlayerDTO) {
        try {
            qqDuelMapper.updateDuelPlayerOnlyNameByOpenId(duelPlayerDTO);
            log.info("数据更新完毕");
        }catch (Exception e) {
            log.error("数据更新错误:{}",e.getMessage());
        }
    }

    /**
     * 添加新数据
     *
     * @param duelPlayerDTO dto参数，注册用
     */
    @Override
    public void register(DuelPlayerDTO duelPlayerDTO) {
        try {
            qqDuelMapper.addDuelPlayer(duelPlayerDTO);
            log.info("数据添加完毕");
        }catch (Exception e) {
            log.error("数据添加错误:{}",e.getMessage());
        }
    }

    /**
     * 一键审核所有名字
     */
    @Override
    public void processAll() {
        try {
            qqDuelMapper.processAll();
            log.info("全部过审完毕");
        }
        catch (Exception e) {
            log.info("审核失败！:{}",e.getMessage());
        }
    }


}
