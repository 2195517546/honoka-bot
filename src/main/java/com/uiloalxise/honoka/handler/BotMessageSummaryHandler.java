package com.uiloalxise.honoka.handler;

import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.pojo.entity.payload.QQBotPayload;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Uiloalxise
 * @ClassName BotMessageSummaryHandler
 * @Description QQ机器人消息总处理类
 */
@Component
@Slf4j
@Async
public class BotMessageSummaryHandler {
    @Resource
    private BotGroupMsgHandler groupMsgHandler;


    /**
     * 总处理器，直接调用<br>
     *
     * @param payload - qq事件payload
     * @return null 则无结果
     */
    public Future<?> summaryHandle(QQBotPayload payload)
    {
        CompletableFuture<String> result = new CompletableFuture<>();
        String type = payload.getT();
        if (QQBotConstant.GROUP_AT_MESSAGE_CREATE.equals(type)) {
            groupMsgHandler.msgHandle(payload);
            result.complete("qq群消息完成处理");
        }
        return result;
    }

}
