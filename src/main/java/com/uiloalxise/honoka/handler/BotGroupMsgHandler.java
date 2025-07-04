package com.uiloalxise.honoka.handler;


import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.honoka.service.group.QQBotGroupFunctionService;
import com.uiloalxise.honoka.service.QQBotRecordService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.payload.QQBotPayload;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author Uiloalxise
 * @ClassName BotGroupMsgHandler
 * @Description QQ机器人群消息总处理服务实现
 */
@Component
@Async
@Slf4j
public class BotGroupMsgHandler{

    @Resource
    private CommandHandler commandHandler;

    /**
     * @param payload 直接传入
     */
    @Async
    public void msgHandle(QQBotPayload payload) {
        String payloadType = payload.getT();
        QQBotPayloadD data = payload.getD();

        if (payloadType.equals(QQBotConstant.GROUP_AT_MESSAGE_CREATE))
        {
            GroupMsgCommand command = commandHandler.groupMsgCommandCreator(data);
            log.info("收到一条content内容为[{}]\n,命令类型:{}", command.getContent(), command.getCommandType());
            if (command.getCommandType().equals(QQBotConstant.COMMAND_TYPE))
            {
                commandHandler.groupCommandHandle(command);
            }else
            {
                commandHandler.groupChatHandle(command);
            }
        }
    }

}
