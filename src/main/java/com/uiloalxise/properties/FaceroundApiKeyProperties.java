package com.uiloalxise.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName FaceroundApiKeyProperties
 * @Description 脸圆云api的配置属性
 */
@Component
@ConfigurationProperties(prefix = "honoka.api-key")
@Data
public class FaceroundApiKeyProperties {
    private String secret;
}
