package com.uiloalxise.honoka.service;


/**
 * @author Uiloalxise
 * @ClassName QQBotRecordService
 * @Description qq机器人记录用户群聊等数据的服务
 */

public interface QQBotRecordService {

    /**
     * 记录一条群id
     * @param groupOpenId 群id
     */
    void recordGroup(String groupOpenId);

    /**
     * 记录一条用户id
     * @param openId 用户id
     */
    void recordUser(String openId);

    /**
     * 保存已经记录的群id到数据库
     */
    void save();
}
