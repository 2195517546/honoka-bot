package com.uiloalxise.honoka.service.impl;

import com.uiloalxise.constants.BotCommandConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.honoka.service.CommandHandleService;
import com.uiloalxise.honoka.service.QQBotGroupFunctionService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Uiloalxise
 * @ClassName CommandHandleServiceImpl
 * @Description TODO
 */
@Service
@Slf4j
public class CommandHandleServiceImpl implements CommandHandleService {

    @Resource
    private QQBotGroupFunctionService groupFunctionService;

    /**
     * @param data
     * @return
     */
    @Override
    public GroupMsgCommand groupMsgCommandCreator(QQBotPayloadD data) {
        String content = data.getContent();
        GroupMsgCommand groupMsgCommand = GroupMsgCommand.builder()
                .groupId(data.getGroupId())
                .messageId(data.getId())
                .authorId(data.getAuthor().getId())
                .build();

        if (content.trim().startsWith(QQBotConstant.COMMAND_START)) {
            groupMsgCommand.setCommandType(QQBotConstant.COMMAND_TYPE);
            groupMsgCommand.setContent(content.replaceAll("\\s+","").replace("/",""));

        }else
        {
            groupMsgCommand.setCommandType(QQBotConstant.CHAT_TYPE);
            groupMsgCommand.setContent(content.trim());
        }
        return groupMsgCommand;
    }

    /**
     * 通过群消息命令实体处理COMMAND类型的群消息
     * @param command 群消息命令实体
     */
    @Async
    @Override

    public void groupCommandHandle(GroupMsgCommand command) {
        String content = command.getContent();

        if (content.startsWith(BotCommandConstant.COMMAND_TEST_PREFIX)) {
            groupFunctionService.testFunction(command);
            return;
        }

        if (content.startsWith(BotCommandConstant.COMMAND_RANDOM_PICTURE_SUFFIX1)) {
            groupFunctionService.randomPic(command);
            return;
        }

        return;
    }

    /**
     * @param command
     */
    @Async
    @Override
    public void groupChatHandle(GroupMsgCommand command) {

    }


}
