package com.uiloalxise.esp32server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.net.*;

@Component
public class Client {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public Client(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void startServer() {
        new Thread(this::runServer).start();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("TCP 服务端已启动，监听端口 1234");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端已连接: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] values = inputLine.split("[,]\\s*"); // 支持逗号加空格
                    if (values.length < 2) continue;

                    float temp = Float.parseFloat(values[0]);
                    float hum = Float.parseFloat(values[1]);
                    System.out.printf("实时数据: %.1f℃ / %.1f%%\n", temp, hum);

                    //发起get请求推送到机器人
                    String url = "http://www.faceroundcloud.site/sensor?tmp=" + temp + "&hum=" + hum;
                    URL requestUrl = new URL(url);

                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

                    // 设置请求方法为GET
                    connection.setRequestMethod("GET");

                    // 获取响应码以确认请求是否成功
                    int responseCode = connection.getResponseCode();
                    System.out.println("GET 请求响应码: " + responseCode);

                    // 关闭连接
                    connection.disconnect();

                    // 广播到 WebSocket 客户端
                    messagingTemplate.convertAndSend("/topic/sensor-updates", "{temperature:" + temp + ", humidity:" + hum + "}");



                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("TCP 服务端异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
