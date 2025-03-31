package com.uiloalxise.pojo.entity.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQBotPayloadD
 * @Description 消息payload的D
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQBotPayloadD implements Serializable {
    private String id;
    private String content;
    private String timestamp;
    private QQBotPayloadDAuthor author;

    @JsonProperty("message_scene")
    private QQBotPayloadDScene messageScene;

    @JsonProperty("group_id")
    private String groupId;

    @JsonProperty("group_openid")
    private String groupOpenid;

    @JsonProperty("message_type")
    private Integer messageType;

    @JsonProperty("plain_token")
    private String plainToken;

    @JsonProperty("event_ts")
    private String eventTs;
}
