package com.uiloalxise.honoka.config;

import com.uiloalxise.pojo.entity.SessionKeeper;
import jakarta.websocket.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebSocket配置类
 * @author liumo
 */
@Configuration
public class WebSocketConfiguration {

    /*

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
     */

    @Bean
    public SessionKeeper sessionKeeper()
    {
        return new SessionKeeper();
    }

}
