package com.uiloalxise.honoka.task;

import com.uiloalxise.honoka.mapper.user.BotUserMapper;
import com.uiloalxise.honoka.service.group.GroupBotUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName RegisterTask
 * @Description 定时注册任务的task
 */
@Component
@Slf4j
public class RegisterTask {

    @Resource
    GroupBotUserService groupBotUserService;

    @Scheduled(cron = "0 0/6 * * * ?")
    public void processRecord() {
        log.info("开始执行定时(6分钟)自动注册任务:");
        groupBotUserService.autoRegisterBotUser();
    }
}
