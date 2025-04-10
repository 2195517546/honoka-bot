package com.uiloalxise.pojo.entity.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        return "https://q.qlogo.cn/qqapp/102098744/" + getAuthorId() + "/640";
    }
}
