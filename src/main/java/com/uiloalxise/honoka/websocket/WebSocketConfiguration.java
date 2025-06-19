package com.uiloalxise.honoka.websocket;


import com.uiloalxise.pojo.entity.SessionKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;


/**
 * @author Uiloalxise
 * @ClassName WebSocketConfiguration
 * @Description websocket的相关配置
 */
//@Configuration
public class WebSocketConfiguration extends AbstractWebSocketHandler {
    @Bean
    public WebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketConnectionManager connectionManager(
            WebSocketClient webSocketClient,
            WebSocketClientHandler webSocketHandler) {

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                webSocketClient,
                webSocketHandler,
                "ws://101.35.231.108:3001/");

        manager.setAutoStartup(true);
        return manager;
    }

        @Bean
    public SessionKeeper sessionKeeper()
    {
        return new SessionKeeper();
    }

}
