package com.uiloalxise.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName QQBotProperties
 * @Description QQ机器人参数配置导入
 */
@Component
@ConfigurationProperties(prefix = "honoka.qq-bot")
@Data
public class QQBotProperties {
    private String appId;
    private String appSecret;
    private String token;
    private String qqNumber;
    private String localVersion;
}
