package com.uiloalxise.honoka.ws;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.utils.QQBotUtil;
import com.uiloalxise.honoka.service.QQBotHandleService;

import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.ExecutionException;

/**
 * @author Uiloalxise
 * @ClassName QQBotClient
 * @Description WS客户端
 */

@ClientEndpoint
@Component
@Slf4j
public class QQBotClient {

    //不知道为什么ws不能直接注入bean只能用如下解决方案
    private static QQBotUtil qqBotUtil;

    @Autowired
    public void qqBotClient(QQBotUtil qqBotUtil) {
        QQBotClient.qqBotUtil = qqBotUtil;
    }

    private static QQBotHandleService qqBotHandleService;

    @Autowired
    public void setQqBotHandleService(QQBotHandleService qqBotHandleService) {
        QQBotClient.qqBotHandleService = qqBotHandleService;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("websocket链接打开!Connected to qq-bot server");

        log.info("qq-bot总处理服务,是否未挂起：{}",qqBotHandleService == null);
    }

    @OnMessage
    @Async("taskExecutor")
    public void onMessage(Session session,String message) throws ExecutionException, InterruptedException {
        log.info("收到QQ消息：{}",message);

        JSONObject msg = JSONObject.parseObject(message);

        JSONObject result = qqBotHandleService.summaryHandle(msg,session).get();

        if(result != null) {
            try {
                session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                log.info("已经发送返回消息：{}",result);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }

    }

    @OnClose
    public void onClose() {
        log.info("QQBotWebsocket链接关闭!Close to server");
        new Thread(() -> {

            log.info("开始===尝试重启websocket");
            try {
                WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
                URI uri = null;
                uri = new URI(qqBotUtil.getWebsocket().getString("url"));
                webSocketContainer.connectToServer(QQBotClient.class, uri);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            log.info("重新启动websocket线程结束");

        }).start();
    }

    @OnError
    public void onError(Throwable throwable,Session session)
    {
        log.info("QQBotWebsocket链接错误");
        log.error("error:",throwable);
    }

}




