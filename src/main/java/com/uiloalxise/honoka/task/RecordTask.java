package com.uiloalxise.honoka.task;

import com.uiloalxise.honoka.service.QQBotRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName RecordTask
 * @Description TODO
 */
@Component
@Slf4j
public class RecordTask {

    @Resource
    private QQBotRecordService qqBotRecordService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void processRecord() {
        log.info("开始执行定时(30分钟)存群组任务:");
        qqBotRecordService.save();
    }
}
