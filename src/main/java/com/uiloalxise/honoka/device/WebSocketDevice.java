package com.uiloalxise.honoka.device;

import com.uiloalxise.utils.QQBotUtil;
import com.uiloalxise.honoka.ws.QQBotClient;
import jakarta.annotation.Resource;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.net.URI;

/**
 * @author Uiloalxise
 * @ClassName WebSocketDevice
 * @Description TODO
 */
@Component
@Order(5)
@Slf4j
public class WebSocketDevice implements CommandLineRunner {

    @Resource
    QQBotUtil qqBotUtil;


    /**
     * 首次启动ws客户端用
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(() -> {

            log.info("开始===调用websocket接口");
            try {
                WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
                URI uri = null;
                uri = new URI(qqBotUtil.getWebsocket().getString("url"));
                webSocketContainer.connectToServer(QQBotClient.class, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.info("启动websocket线程结束");

        });
        thread.start();




    }
}
