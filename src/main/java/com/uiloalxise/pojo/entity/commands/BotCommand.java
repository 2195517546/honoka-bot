package com.uiloalxise.pojo.entity.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.uiloalxise.constants.QQBotConstant.AVATAR_URL_PREFIX;

/**
 * @author Uiloalxise
 * @ClassName BotCommand
 * @Description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotCommand {
    private String messageId;
    private String content;
    private Integer commandType;
    private String authorId;


    public String getAvatarUrl()
    {
        return AVATAR_URL_PREFIX + getAuthorId() + "/640";
    }
}
