package com.uiloalxise.pojo.entity.commands;

import lombok.*;

/**
 * @author Uiloalxise
 * @ClassName GroupMsgCommand
 * @Description 群消息命令实体，有发一条群消息的所有参数以及命令内容
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMsgCommand extends BotCommand{
    /*
     groupId
     msgId
     authorId
     content
     commandType - 指明是指令还是语擦聊天消息
     */
    private String groupId;
    private String messageId;
    private String authorId;
    private String content;
    private Integer commandType;


}
