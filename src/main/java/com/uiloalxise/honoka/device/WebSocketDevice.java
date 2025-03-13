package com.uiloalxise.honoka.device;

import com.uiloalxise.utils.QQBotUtil;
import com.uiloalxise.honoka.ws.QQBotClient;
import jakarta.annotation.Resource;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author Uiloalxise
 * @ClassName WebSocketDevice
 * @Description ws启动设备Runner
 */
@Component
@Order(5)
@Slf4j
public class WebSocketDevice implements CommandLineRunner {

    @Resource
    QQBotUtil qqBotUtil;


    /**
     * 首次启动ws客户端用
     * @param args 无需自己填
     */
    @Override
    public void run(String... args){
        Thread thread = new Thread(() -> {

            log.info("开始===调用websocket接口");
            try {
                WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
                URI uri;
                uri = new URI(qqBotUtil.getWebsocket().getString("url"));
                Session session = webSocketContainer.connectToServer(QQBotClient.class, uri);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            log.info("启动websocket线程结束");

        });
        thread.start();




    }
}
