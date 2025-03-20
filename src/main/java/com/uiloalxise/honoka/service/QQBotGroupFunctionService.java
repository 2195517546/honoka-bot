package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupFunctionService
 * @Description QQ机器人可调用的功能总服务类
 */
public interface QQBotGroupFunctionService {
    //以下是主要功能

    /**
     * 随机图片，目前只支持宛图
     * @param data
     */
    void randomPic(JSONObject data);

    /**
     * 随机钉言钉语
     * @param data 总消息处理下的d
     */
    void randomDingTalk(JSONObject data);

    /**
     * 群规
     * @param data 总消息处理下的d
     */
    void groupRule(JSONObject data);

    /**
     * pjsk谱面查询
     * @param data 总消息处理下的d
     */
    void pjskPaneInfo(JSONObject data);

    /**
     * pjsk音乐查询
     * @param data 总消息处理下的d
     */
    void pjskMusicInfo(JSONObject data);

    /**
     * 帮助菜单
     * @param data 总消息处理下的d
     */
    void helpMenu(JSONObject data);

    /**
     * 决斗
     * @param data 总消息处理下的d
     */
    void honokaDuel(JSONObject data);

    //以下是临时趣味功能
    void check749(JSONObject data);

    //以下是默认功能

    /**
     * 默认回复
     * @param data 总消息处理下的d
     */
    void defaultMessage(JSONObject data);

    /**
     * 测试功能
     * 目前的测试：---
     *
     * @param data 总消息处理下的d
     */
    void testFunction(JSONObject data);

    /**
     * 主动测试功能
     * 目前的测试:---
     *
     * @param groupOpenId 群openId
     */
    void testFunction(String groupOpenId);

    /**
     * 一键刷屏
     * @param data 总消息处理下的d
     */
    void spamFunction(JSONObject data);

}
