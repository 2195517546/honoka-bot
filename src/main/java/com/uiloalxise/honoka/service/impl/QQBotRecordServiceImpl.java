package com.uiloalxise.honoka.service.impl;

import com.uiloalxise.honoka.mapper.QQGroupsMapper;
import com.uiloalxise.honoka.mapper.QQUserMapper;
import com.uiloalxise.honoka.service.QQBotRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName QQBotRecordServiceImpl
 * @Description QQ机器人使用记录的服务类impl
 */

@Service
@Transactional
@Async
@Slf4j
public class QQBotRecordServiceImpl implements QQBotRecordService {


    @Resource(name = "groupsRecord")
    private Set<String> groupsRecord;

    @Resource(name = "usersRecord")
    private Set<String> usersRecord;

    @Resource
    private QQGroupsMapper qqGroupsMapper;

    @Resource
    private QQUserMapper qqUserMapper;

    /**
     * @param groupOpenId 群号（openId
     */
    @Override
    public void recordGroup(String groupOpenId) {
        log.info("groupOpenId  :{},是否收纳,contains:{}", groupOpenId,groupsRecord.contains(groupOpenId));

        groupsRecord.add(groupOpenId);
    }

    /**
     * 记录一条用户id
     *
     * @param openId 用户id
     */
    @Override
    public void recordUser(String openId) {
        log.info("收纳一条用户openid:{}", openId);
        usersRecord.add(openId);
    }

    /**
     * 把群号保存到数据库
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

        if (!usersRecord.isEmpty()) {
            qqUserMapper.insertUsersOpenId(usersRecord.stream().toList());
            log.info("定时插入传成功，已保存用户数:{}",usersRecord.size());
            usersRecord.clear();
        }else{
            log.info("userRecord为空，无需保存用户");
        }
    }
}
