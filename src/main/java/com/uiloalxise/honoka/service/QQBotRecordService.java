package com.uiloalxise.honoka.service;


/**
 * @author Uiloalxise
 * @ClassName QQBotRecordService
 * @Description TODO
 */

public interface QQBotRecordService {

    /**
     * 记录一条群id
     * @param groupOpenId 群id
     */
    void record(String groupOpenId);

    /**
     * 保存已经记录的群id到数据库
     */
    void save();
}
