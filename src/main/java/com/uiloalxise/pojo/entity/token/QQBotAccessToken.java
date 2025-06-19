package com.uiloalxise.pojo.entity.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQBotAccessToken
 * @Description qq机器人api的访问令牌实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQBotAccessToken implements Serializable {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private String expiresIn;
}
