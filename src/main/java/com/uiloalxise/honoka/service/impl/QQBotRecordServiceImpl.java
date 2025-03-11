package com.uiloalxise.honoka.service.impl;

import com.uiloalxise.honoka.mapper.QQGroupsMapper;
import com.uiloalxise.honoka.service.QQBotRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName QQBotRecordServiceImpl
 * @Description TODO
 */

@Service
@Transactional
@Async
@Slf4j
public class QQBotRecordServiceImpl implements QQBotRecordService {


    @Resource
    private Set<String> groupsRecord;

    @Resource
    private QQGroupsMapper qqGroupsMapper;

    /**
     * @param groupOpenId
     */
    @Override
    public void record(String groupOpenId) {
        log.info("groupOpenId  :{},是否收纳,contains:{}", groupOpenId,groupsRecord.contains(groupOpenId));

        groupsRecord.add(groupOpenId);
    }

    /**
     *
     */
    @Override
    public void save() {
        if (!groupsRecord.isEmpty()) {
            qqGroupsMapper.insertGroupsOpenId(groupsRecord);
            log.info("定时插入成功,已保存群数:{}",groupsRecord.size());
            groupsRecord.clear();
        }
        else {
            log.info("groupsRecord为空，无需保存群组");
        }
    }
}
