package com.uiloalxise.honoka.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.pojo.entity.payload2.QQBotPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author Uiloalxise
 * @ClassName WebSocketClientHandler
 * @Description TODO
 */
@Component
@Slf4j
public class WebSocketClientHandler extends AbstractWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket连接已建立: {}", session.getId());
        // 连接建立后可以发送初始消息
        session.sendMessage(new TextMessage("Hello Server!"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        JSONObject json = JSONObject.parseObject(message.getPayload());

        QQBotPayload payload = json.toJavaObject(QQBotPayload.class);
        log.info( "Received message: {}", payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("传输错误: {}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("WebSocket连接已关闭: {}", closeStatus.toString());
        // 可以在这里实现重连逻辑
    }

}
