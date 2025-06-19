package com.uiloalxise.pojo.entity.ai;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Uiloalxise
 * @ClassName ChatRequest
 * @Description TODO
 */
@Data
public class ChatRequest implements Serializable {
    private String model;
    private AiMessage[] messages;
    private Option options;

    public ChatRequest(String model, String role,String content) {
        this.model = model;
        AiMessage messages = AiMessage.builder()
                .role(role)
                .content(content)
                .build();
        this.messages = new AiMessage[]{messages};
        this.options = Option.builder()
                .temperature(BigDecimal.valueOf(0.7))
                .num_predict(512)
                .num_ctx(2048)
                .build();
    }
}
