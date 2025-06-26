package com.uiloalxise.honoka.service.group.duel;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.user.BotUser;

import java.math.BigDecimal;

/**
 * @author Uiloalxise
 * @ClassName DuelService
 * @Description TODO
 */
public interface DuelService {
    /**
     * 决斗发起与接受
     * @param command 命令实体类
     */
    void duel(GroupMsgCommand command);

    /**
     * 更新决斗结果
     * @param winner 战斗胜利者
     * @param loser 战斗失败者
     * @param cost 战斗消耗
     */
    void updateDuelResults(BotUser winner, BotUser loser, BigDecimal cost);

    /**
     * 更新用户积分
     * @param botUser - 用户
     * @param cost - 花销
     */
    void updateUserPoints(BotUser botUser, BigDecimal cost);

    /**
     * 增加用户胜利或者输的次数
     * @param botUser - 用户
     * @param result - 结果，true代表赢，false代表输
     */
    void updateUserDuelResult(BotUser botUser,Boolean  result);
}
