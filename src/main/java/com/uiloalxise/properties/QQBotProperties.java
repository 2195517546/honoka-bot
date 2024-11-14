package com.uiloalxise.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName QQBotProperties
 * @Description TODO
 */
@Component
@ConfigurationProperties(prefix = "honoka.qq-bot")
@Data
public class QQBotProperties {
    private String appId;
    private String appSecret;
    private String token;
    private String qqNumber;
}
