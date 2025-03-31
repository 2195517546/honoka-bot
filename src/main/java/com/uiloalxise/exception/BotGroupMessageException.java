package com.uiloalxise.exception;

import com.uiloalxise.pojo.entity.commands.BotCommand;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import lombok.Getter;

/**
 * @author Uiloalxise
 * @ClassName BotGroupMessageException
 * @Description TODO
 */
@Getter
public class BotGroupMessageException extends RuntimeException {


    private final GroupMsgCommand command;


    public BotGroupMessageException(String message, GroupMsgCommand command) {
        super(message);
        this.command = command;
    }

    public BotGroupMessageException(String message, Throwable cause, GroupMsgCommand command) {
        super(message,cause);
        this.command = command;
    }
}
