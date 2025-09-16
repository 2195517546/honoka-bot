package com.uiloalxise.honoka.service;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;

/**
 * @author Uiloalxise
 * @ClassName MessageSenderService
 * @Description TODO
 */
public interface MessageSenderService {

    /**
     * 群文本消息发送
     */
    void groupTextMessageSender(GroupMsgCommand command,String text);


    /**
     * 带seq的群文本消息发送
     * @param command
     * @param text
     * @param seq
     */
    void groupTextMessageSender(GroupMsgCommand command,String text,Integer seq);

    /**
     * 带seq的图片消息发送
     * @param command
     * @param text
     * @param url
     * @param seq
     */
    void groupPictureMessageSender(GroupMsgCommand command,String url,String text,Integer seq);


    /**
     * 带seq的音频消息发送
     * @param command
     * @param text
     * @param url
     * @param seq
     */
    void groupSoundMessageSender(GroupMsgCommand command,String url,String text,Integer seq);
}
