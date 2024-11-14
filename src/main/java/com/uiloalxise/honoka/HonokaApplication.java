package com.uiloalxise.honoka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableCaching //开启缓存注解
@SpringBootApplication
@EnableTransactionManagement //事务管理
@EnableScheduling //开启定时task
public class HonokaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HonokaApplication.class, args);
    }

}
