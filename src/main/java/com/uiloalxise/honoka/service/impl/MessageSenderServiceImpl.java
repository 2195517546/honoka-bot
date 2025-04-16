package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.BotCommandConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.exception.BotGroupMessageException;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import com.uiloalxise.pojo.entity.QQMediaFile;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.utils.QQBotUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @author Uiloalxise
 * @ClassName MessageSenderServiceImpl
 * @Description TODO
 */
@Service
@Async
@Slf4j
public class MessageSenderServiceImpl implements MessageSenderService {

    @Resource(name = "groupWebClient")
    private WebClient groupWebClient;

    @Resource
    private QQBotUtil qqBotUtil;

    /**
     * 群消息发送
     *
     * @param command 消息
     */
    @Override
    public void groupTextMessageSender(GroupMsgCommand command,String text) {
        this.groupTextMessageSender(command,text,1);
    }

    /**
     * 带seq的群文本消息发送
     *
     * @param command
     * @param text
     * @param seq
     */
    @Override
    public void groupTextMessageSender(GroupMsgCommand command, String text, Integer seq) {
        String targetUri = command.getGroupId() + QQBotConstant.MESSAGES_URI;

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(text)
                .msgType(0)
                .eventId(QQBotConstant.GROUP_AT_MESSAGE_CREATE)
                .msgId(command.getMessageId())
                .msgSeq(seq)
                .build();

        groupWebClient.post()
                .uri(targetUri)
                .headers(httpHeaders -> httpHeaders.addAll(getBotHeader()))
                .bodyValue(qqGroupsMsg)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(this::onSuccess, error -> onError(error, command));
    }

    /**
     * 带seq的图片消息发送
     *
     * @param command
     * @param url
     * @param text
     * @param seq
     */
    @Override
    public void groupPictureMessageSender(GroupMsgCommand command, String url, String text, Integer seq) {
        QQMediaFile qqMediaFile = QQMediaFile.builder().url(url)
            .fileType(1)
            .srvSendMsg(false)
            .build();

        log.info("qqMediaFile:{}",JSONObject.toJSONString(qqMediaFile));

        groupWebClient.post()
                .uri(command.getGroupId() + QQBotConstant.FILES_URI)
                .headers(httpHeaders -> httpHeaders.addAll(getBotHeader()))
                .bodyValue(qqMediaFile)
                .retrieve()
                .bodyToMono(JSONObject.class)
                .subscribe(resp ->{

                    log.info(resp.toString());
                    QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                    .content(text)
                    .msgType(7)
                    .eventId(QQBotConstant.GROUP_AT_MESSAGE_CREATE)
                    .media(resp)
                    .msgId(command.getMessageId())
                    .msgSeq(seq)
                    .build();
                        groupWebClient.post()
                                .uri(command.getGroupId() + QQBotConstant.MESSAGES_URI)
                                .headers(httpHeaders -> httpHeaders.addAll(getBotHeader()))
                                .bodyValue(qqGroupsMsg)
                                .retrieve()
                                .bodyToMono(String.class)
                                .subscribe(this::onSuccess, error -> onError(error, command));
                    }
                , error -> onError(error, command)
                );
    }


    private void onError(Throwable error,GroupMsgCommand command)
    {
        log.error("webclient 发送群消息时失败:{}",error.getMessage());
        if(!command.getCommandType().equals(QQBotConstant.ERROR_TYPE))
        {
            command.setCommandType(QQBotConstant.ERROR_TYPE);
            groupTextMessageSender(command,"果果执行，你的命令时被神秘的力量阻止了！\n请联系管理员报错！！！");
        }
    }

    private void onSuccess(Object response)
    {
        log.info("消息发送成功:{}",response);
    }



    private HttpHeaders getBotHeader()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Authorization", qqBotUtil.getAuthorization());
        return headers;
    }


}
