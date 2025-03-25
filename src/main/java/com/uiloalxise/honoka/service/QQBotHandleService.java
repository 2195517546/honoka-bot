package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.pojo.entity.payload.QQBotPayload;
import jakarta.websocket.Session;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.Future;

/**
 * @author Uiloalxise
 * @ClassName QQBotHandleService
 * @Description QQ机器人消息总处理服务
 */
public interface QQBotHandleService {

    /**
     * 消息的总处理器，直接调用
     *
     * @param payload 一个payload
     * @return 一个异步的JSON对象用于查看处理的结果
     */
    Future<?> summaryHandle(QQBotPayload payload);
}
