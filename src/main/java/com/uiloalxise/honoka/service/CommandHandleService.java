package com.uiloalxise.honoka.service;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;

/**
 * @author Uiloalxise
 * @ClassName CommandHandleService
 * @Description TODO
 */
public interface CommandHandleService {
    /**
     * 通过payload的d创建群消息command实体
     * @param data payload的d
     * @return command实体
     */
    GroupMsgCommand groupMsgCommandCreator(QQBotPayloadD data);

    /**
     * 命令类群消息处理
     * @param groupMsgCommand 群消息实体
     */
    void groupCommandHandle(GroupMsgCommand groupMsgCommand);

    /**
     * 非命令类群消息处理
     * @param groupMsgCommand 群消息实体
     */
    void groupChatHandle(GroupMsgCommand groupMsgCommand);

}
