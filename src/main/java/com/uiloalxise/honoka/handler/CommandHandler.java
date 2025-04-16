package com.uiloalxise.honoka.handler;

import com.uiloalxise.constants.BotCommandConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.honoka.service.QQBotRecordService;
import com.uiloalxise.honoka.service.group.GroupBotUserService;
import com.uiloalxise.honoka.service.group.QQBotGroupFunctionService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName CommandHandler
 * @Description TODO
 */
@Component
@Slf4j
public class CommandHandler{

    @Resource
    private QQBotRecordService qqBotRecordService;


    @Resource
    private QQBotGroupFunctionService groupFunctionService;

    @Resource
    private GroupBotUserService groupBotUserService;

    /**
     * @param data
     * @return
     */
    public GroupMsgCommand groupMsgCommandCreator(QQBotPayloadD data) {
        String content = data.getContent();
        GroupMsgCommand groupMsgCommand = GroupMsgCommand.builder()
                .groupId(data.getGroupId())
                .messageId(data.getId())
                .authorId(data.getAuthor().getId())
                .build();

        qqBotRecordService.recordUser(groupMsgCommand.getAuthorId());
        qqBotRecordService.recordGroup(groupMsgCommand.getGroupId());


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
    public void groupCommandHandle(GroupMsgCommand command) {
        String content = command.getContent();

        if (commandCheck(content,BotCommandConstant.COMMAND_TEST_PREFIX)) {
            groupFunctionService.testFunction(command);
            return;
        }

        if (commandCheck(content,BotCommandConstant.COMMAND_RANDOM_PICTURE_SUFFIX1)) {
            groupFunctionService.randomPic(command);
            return;
        }

        if (commandCheck(content,BotCommandConstant.COMMAND_HELP)) {
            groupFunctionService.helpMenu(command);
            return;
        }

        if (commandCheck(content,BotCommandConstant.COMMAND_DINGTALK)) {
            groupFunctionService.dingTalk(command);
            return;
        }

        if (commandCheck(content,BotCommandConstant.COMMAND_947)) {
            groupFunctionService.check947(command);
            return;
        }

        if (commandCheck(content,"决斗"))
        {
            groupFunctionService.bannedFunction(command);
            return;
        }

        if (commandCheck(content,"群规"))
        {
            groupFunctionService.bannedFunction(command);
            return;
        }

        //用户命令相关
        if (commandCheck(content,BotCommandConstant.USER_COMMAND_INFO)) {
            groupBotUserService.infoBotUser(command);
            return;
        }
        if (commandCheck(content,BotCommandConstant.USER_COMMAND_REGISTER))
        {
            groupBotUserService.registerBotUser(command);
            return;
        }

        return;
    }


    //单个指令检查
    private boolean commandCheck(String content,String command) {
        return content.startsWith(command);
    }

    //多个指令检查
    private boolean commandCheck(String content,String [] commands) {
        for (String command : commands) {
            if (commandCheck(content, command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 群聊聊天命令处理
     * @param command 命令实体类
     */
    @Async
    public void groupChatHandle(GroupMsgCommand command) {
        groupFunctionService.defaultMessage(command);
    }


}
