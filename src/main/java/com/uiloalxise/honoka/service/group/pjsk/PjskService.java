package com.uiloalxise.honoka.service.group.pjsk;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;

/**
 * @author Uiloalxise
 * @ClassName PjskService
 * @Description 世界计划相关功能的服务器类
 */
public interface PjskService {

    /**
     * pjsk随机表情包带文字
     * @param command - 群消息命令
     */
    void pjskRandomEmoticon(GroupMsgCommand command);

    void pjskEmoticonMenu(GroupMsgCommand command);

    void pjskEmoticon(GroupMsgCommand command);
}
