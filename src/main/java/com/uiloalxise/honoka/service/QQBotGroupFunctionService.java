package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupFunctionService
 * @Description TODO
 */
public interface QQBotGroupFunctionService {
    //以下是主要功能

    /**
     * 随机图片，目前只支持宛图
     * @param data
     * @param msgSeq
     */
    void randomPic(JSONObject data,Integer msgSeq);

    /**
     * 随机钉言钉语
     * @param data
     * @param msgSeq
     */
    void randomDingTalk(JSONObject data,Integer msgSeq);

    /**
     * 群规
     * @param data
     * @param msgSeq
     */
    void groupRule(JSONObject data,Integer msgSeq);

    /**
     * pjsk谱面查询
     * @param data
     * @param msgSeq
     */
    void pjskPaneInfo(JSONObject data,Integer msgSeq);

    /**
     * pjsk音乐查询
     * @param data
     * @param msgSeq
     */
    void pjskMusicInfo(JSONObject data,Integer msgSeq);

    /**
     * 帮助菜单
     * @param data
     * @param msgSeq
     */
    void helpMenu(JSONObject data,Integer msgSeq);

    /**
     * 决斗
     * @param data
     * @param msgSeq
     */
    void honokaDuel(JSONObject data,Integer msgSeq);

    //以下是临时趣味功能
    void check749(JSONObject data,Integer msgSeq);

    //以下是默认功能

    /**
     * 默认回复
     * @param data
     * @param msgSeq
     */
    void defaultMessage(JSONObject data,Integer msgSeq);

    /**
     * 测试功能
     * 目前的测试：
     * 从接口处生成任意图片
     * @param data
     * @param msgSeq
     */
    void testFunction(JSONObject data,Integer msgSeq);

}
