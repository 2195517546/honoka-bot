package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.pojo.entity.payload.QQBotPayload;
import com.uiloalxise.honoka.service.QQBotGroupMsgHandleService;
import com.uiloalxise.honoka.service.QQBotHandleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Uiloalxise
 * @ClassName QQBotHandleServiceImpl
 * @Description QQ机器人消息总处理类
 */
@Service
@Slf4j
@Async
public class QQBotHandleServiceImpl implements QQBotHandleService {
    @Resource
    private QQBotGroupMsgHandleService qqBotGroupMsgHandleService;


    /**
     * 总处理器，直接调用<br>
     *
     * @param payload - qq事件payload
     * @return null 则无结果
     */
    @Override
    public Future<?> summaryHandle(QQBotPayload payload)
    {


        CompletableFuture<String> result = new CompletableFuture<>();

        String type = payload.getT();
        JSONObject json = JSONObject.from(payload);

        if (QQBotConstant.GROUP_AT_MESSAGE_CREATE.equals(type)) {


            qqBotGroupMsgHandleService.msgHandle(payload);

            result.complete("qq群消息完成处理");
        }




        return result;
    }

}
