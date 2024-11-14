package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.Session;

import java.util.concurrent.Future;

/**
 * @author Uiloalxise
 * @ClassName QQBotHandleService
 * @Description TODO
 */
public interface QQBotHandleService {

    /**
     * 消息的总处理器，直接调用
     *
     * @param json 一个对象
     * @param session ws的session
     * @return 一个异步的JSON对象用于查看处理的结果
     */
    Future<JSONObject> summaryHandle(JSONObject json, Session session);
}
