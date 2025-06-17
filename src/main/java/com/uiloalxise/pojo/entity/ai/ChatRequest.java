package com.uiloalxise.pojo.entity.ai;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName ChatRequest
 * @Description TODO
 */
@Data
public class ChatRequest implements Serializable {
    private String model;
    private AiMessage[] messages;

    public ChatRequest(String model, String role,String content) {
        this.model = model;
        AiMessage messages = AiMessage.builder()
                .role(role)
                .content(content)
                .build();
        this.messages = new AiMessage[]{messages};
    }
}
