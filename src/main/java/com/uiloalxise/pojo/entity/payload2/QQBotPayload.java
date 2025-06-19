package com.uiloalxise.pojo.entity.payload2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQBotPayload
 * @Description qq机器人消息载荷的实体类
 */

@Data
public class QQBotPayload implements Serializable {
    @JsonProperty("self_id")
    private String selfId;

    @JsonProperty("user_id")
    private String userId;

    private Long time;
    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("message_seq")
    private Long messageSeq;

    @JsonProperty("real_id")
    private Long realId;

    @JsonProperty("real_seq")
    private String realSeq;

    @JsonProperty("message_type")
    private String messageType;

    private Sender sender;

    @JsonProperty("raw_message")
    private String rawMessage;

    private Integer font;

    @JsonProperty("sub_type")
    private String subType;

    private Message[] message;

    @JsonProperty("message_format")
    private String messageFormat;

    @JsonProperty("post_type")
    private String postType;

    @JsonProperty("group_id")
    private Long groupId;
}
