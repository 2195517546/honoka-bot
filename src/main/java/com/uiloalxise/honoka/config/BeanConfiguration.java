package com.uiloalxise.honoka.config;

import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.utils.PJSKUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName BeanConfiguration
 * @Description 常用bean配置
 */
@Configuration
@Slf4j
public class BeanConfiguration {
    @Bean("pjsk-util")
    @ConditionalOnMissingBean
    public PJSKUtil pjskUtil() {
        log.info("开始创建qq机器人工具类对象。");
        return new PJSKUtil();
    }

    @Bean("restTemplate")
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        log.info("开始创建restTemplate对象");
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(name = "groupWebClient")
    public WebClient groupWebClient(){
        log.info("开始创建GroupWebclient对象");
        return WebClient.builder()
                .baseUrl(QQBotConstant.OPENAPI_GROUP_URL_PREFIX)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "apiWebClient")
    public WebClient apiWebClient(){
        log.info("开始创建ApiWebClient对象");
        return WebClient.builder()
                .baseUrl(WebConstant.WEBSITE_API_URL)
                .build();
    }


    @Bean(name = "groupsRecord")
    @ConditionalOnMissingBean(name = "groupsRecord")
    public Set<String> groupsRecord() {
        log.info("开始创建GroupsRecord群openid记录器");
        return new HashSet<>();
    }

    @Bean(name = "usersRecord")
    @ConditionalOnMissingBean(name = "usersRecord")
    public Set<String> usersRecord() {
        log.info("开始创建UserRecord用户openid记录器");
        return new HashSet<>();
    }
}
