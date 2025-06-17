package com.uiloalxise.honoka.config;

import com.uiloalxise.properties.QQBotProperties;
import com.uiloalxise.honoka.utils.QQBotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Uiloalxise
 * @ClassName QQBotConfiguration
 * @Description QQBOT配置类
 */
@Configuration
@EnableConfigurationProperties(QQBotProperties.class)
@Slf4j
public class QQBotConfiguration {
    @Bean("qq-bot-util")
    @ConditionalOnMissingBean
    public QQBotUtil qqBotUtil(QQBotProperties qqBotProperties) {
        log.info("开始创建qq机器人工具类对象：{}",qqBotProperties);
        log.info("当前QQ机器人版本:{}",qqBotProperties.getLocalVersion());
        return new QQBotUtil(qqBotProperties.getAppId(),
                qqBotProperties.getAppSecret(),
                qqBotProperties.getToken(),
                qqBotProperties.getQqNumber(),
                qqBotProperties.getLocalVersion()
        );
    }
}
