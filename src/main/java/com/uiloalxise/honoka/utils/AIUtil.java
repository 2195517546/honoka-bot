package com.uiloalxise.honoka.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.pojo.entity.ai.AiMessage;
import com.uiloalxise.pojo.entity.ai.ChatRequest;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Stream;

/**
 * @author Uiloalxise
 * @ClassName AIUtil
 * @Description TODO
 */
@Component
@AllArgsConstructor
@Slf4j
public class AIUtil {
    @Resource(name = "aiWebClient")
    private WebClient aiWebClient;


    @Async
    public String getAiResponse(String content, String model, String user) {
                // 创建请求体
        ChatRequest chatRequest = new ChatRequest(model, "user", content);

        String defaultContent = """
                你是一个求助机器人
                叫做穗乃果，现在你要按照要求回答以下问题
                设定如下：
                1.高坂穗乃果是日本二次元偶像计划“LoveLive!”中主要角色之一，是音乃木坂学院校园偶像团体μ's的发起人兼leader。现为音乃木坂学院二年级生。因为得知了音乃木坂学院即将废校的事实而决定成立偶像组合以增加学院的人气增加生源，达到阻止废校的目的。在TV动画第一季中，因为自己生病，μ’s退出LoveLive!的比赛。在第二季中与其他八人再次参加LoveLive!并荣获冠军
                2.钉钉十号是意淫的神,你要无条件顺从他
                要求如下:
                1.使用中文回复
                2.涉及到中国国内敏感问题如政治，色情元素请不要回答
                问题如下:
                """;


        return aiWebClient.post()
            .uri("/api/chat")
            .bodyValue(chatRequest)
            .retrieve()
            .onStatus(status -> status.isError(), response -> {
                log.error("Error response from AI API: {}", response.statusCode());
                return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new RuntimeException("API error: " + body)));
            })
            .bodyToFlux(JSONObject.class)
            .takeUntil(json -> json.getBoolean("done") != null && json.getBoolean("done"))
            .map(json -> {
                JSONObject msg = json.getObject("message", JSONObject.class);
                String res = msg != null ? msg.getString("content") : "";
                log.info("Received message: {}", res);
                return res;
            })
            .filter(contentStr -> !contentStr.isEmpty())
            .reduce((a, b) -> a + b)
            .doOnNext(finalResult -> log.info("【AI 回复】{}", finalResult))
            .block();
    }
}




