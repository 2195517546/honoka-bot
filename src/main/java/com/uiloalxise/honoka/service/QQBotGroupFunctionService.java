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
     * @param data
     */
    void randomDingTalk(JSONObject data);

    /**
     * 群规
     * @param data
     */
    void groupRule(JSONObject data);

    /**
     * pjsk谱面查询
     * @param data
     */
    void pjskPaneInfo(JSONObject data);

    /**
     * pjsk音乐查询
     * @param data
     */
    void pjskMusicInfo(JSONObject data);

    /**
     * 帮助菜单
     * @param data
     */
    void helpMenu(JSONObject data);

    /**
     * 决斗
     * @param data
     */
    void honokaDuel(JSONObject data);

    //以下是临时趣味功能
    void check749(JSONObject data);

    //以下是默认功能

    /**
     * 默认回复
     * @param data
     */
    void defaultMessage(JSONObject data);

    /**
     * 测试功能
     * 目前的测试：
     * 从接口处生成任意图片
     * @param data
     */
    void testFunction(JSONObject data);

    /**
     * 一键刷屏
     * @param data
     */
    void spamFunction(JSONObject data);

}
