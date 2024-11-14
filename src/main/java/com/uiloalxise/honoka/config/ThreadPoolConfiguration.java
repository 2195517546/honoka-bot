package com.uiloalxise.honoka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Uiloalxise
 * @ClassName ThreadPoolConfiguration
 * @Description 线程池配置类
 */
@Configuration
@EnableAsync // 允许使用异步方法
@Slf4j
public class ThreadPoolConfiguration {

    @Bean("taskExecutor")
    public Executor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        threadPoolTaskExecutor.setCorePoolSize(3);
        // 设置最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(3);
        // 设置工作队列大小
        threadPoolTaskExecutor.setQueueCapacity(100);
        //设置空闲线程死亡时间
        threadPoolTaskExecutor.setKeepAliveSeconds(300);
        // 设置拒绝策略.当工作队列已满,线程数为最大线程数的时候,接收新任务抛出RejectedExecutionException异常
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 初始化线程池
        threadPoolTaskExecutor.initialize();

        log.info("线程池配置设置完毕");

        return threadPoolTaskExecutor;
    }
}
