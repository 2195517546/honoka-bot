package com.uiloalxise.pojo.entity.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQBotPayloadDAuthor
 * @Description QQBot消息载荷的作者信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQBotPayloadDAuthor implements Serializable {
    private String id;

    @JsonProperty("user_openid")
    private String userOpenid;

    @JsonProperty("union_openid")
    private String unionOpenid;
}
