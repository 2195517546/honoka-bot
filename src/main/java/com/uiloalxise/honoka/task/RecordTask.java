package com.uiloalxise.honoka.task;

import com.uiloalxise.honoka.service.QQBotRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Uiloalxise
 * @ClassName RecordTask
 * @Description record定时任务
 */
@Component
@Slf4j
public class RecordTask {

    @Resource
    private QQBotRecordService qqBotRecordService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void processRecord() {
        log.info("开始执行定时(5分钟)存用户和群id任务:");
        qqBotRecordService.save();
    }
}
