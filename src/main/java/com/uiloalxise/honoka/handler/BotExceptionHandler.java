package com.uiloalxise.honoka.handler;

import com.uiloalxise.exception.BotGroupMessageException;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * @author Uiloalxise
 * @ClassName BotExceptionHandler
 * @Description TODO
 */
@Component
@Slf4j
public class BotExceptionHandler
{

    @Resource
    MessageSenderService messageSenderService;


    @ExceptionHandler
    public void botGroupMessageException(BotGroupMessageException ex) {
        log.error("发生错误:{}",ex.getMessage());
        messageSenderService.groupTextMessageSender(ex.getCommand(),"果果执行，你的命令时被神秘的力量阻止了！\n请联系管理员报错！！！");
    }
}
