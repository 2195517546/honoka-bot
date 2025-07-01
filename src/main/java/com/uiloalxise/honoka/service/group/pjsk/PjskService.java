package com.uiloalxise.honoka.service.group.pjsk;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;

/**
 * @author Uiloalxise
 * @ClassName PjskService
 * @Description TODO
 */
public interface PjskService {

    /**
     * pjsk随机表情包带文字
     * @param command - 群消息命令
     */
    void pjskRandomEmoticon(GroupMsgCommand command);
}
