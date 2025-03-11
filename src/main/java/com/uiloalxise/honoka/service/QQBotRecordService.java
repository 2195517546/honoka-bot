package com.uiloalxise.honoka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Uiloalxise
 * @ClassName QQBotRecordService
 * @Description TODO
 */

public interface QQBotRecordService {

    void record(String groupOpenId);

    void save();
}
