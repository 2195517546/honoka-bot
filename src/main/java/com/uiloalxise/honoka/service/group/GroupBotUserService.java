package com.uiloalxise.honoka.service.group;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.user.BotUser;

/**
 * @author Uiloalxise
 * @ClassName GroupBotUserService
 * @Description TODO
 */
public interface GroupBotUserService {
    /**
     * 获得用户信息
     */
    void infoBotUser(GroupMsgCommand command);

    /**
     * 用户注册
     * @param command 命令实体类
     */
    void registerBotUser(GroupMsgCommand command);

    /**
     * 日常签到功能
     * @param command 命令实体类
     */
    void dailySignInBotUser(GroupMsgCommand command);

    /**
     * 自动用户注册
     */
    void autoRegisterBotUser();
}
