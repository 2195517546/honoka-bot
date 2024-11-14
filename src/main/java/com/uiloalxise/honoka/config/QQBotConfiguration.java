package com.uiloalxise.honoka.config;

import com.uiloalxise.properties.QQBotProperties;
import com.uiloalxise.utils.QQBotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Uiloalxise
 * @ClassName QQBotConfiguration
 * @Description TODO
 */
@Configuration
@EnableConfigurationProperties(QQBotProperties.class)
@Slf4j
public class QQBotConfiguration {
    @Bean("qq-bot-util")
    @ConditionalOnMissingBean
    public QQBotUtil qQBotUtil(QQBotProperties qqBotProperties) {
        log.info("开始创建qq机器人工具类对象：{}",qqBotProperties);
        return new QQBotUtil(qqBotProperties.getAppId(),
                qqBotProperties.getAppSecret(),
                qqBotProperties.getToken(),
                qqBotProperties.getQqNumber()
        );
    }
}
