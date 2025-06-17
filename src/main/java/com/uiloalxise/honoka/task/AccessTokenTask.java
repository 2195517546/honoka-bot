package com.uiloalxise.honoka.task;

import com.uiloalxise.honoka.utils.QQBotUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName AccessTokenTask
 * @Description TODO
 */
@Component
@Slf4j
public class AccessTokenTask {

    @Resource
    private QQBotUtil qqBotUtil;

    @Scheduled(fixedRate = 60000)
    public void process() {
        qqBotUtil.freshAccessToken();
    }
}
