package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupFunctionService
 * @Description TODO
 */
public interface QQBotGroupFunctionService {
    //以下是主要功能
    void randomPic(JSONObject data,Integer msgSeq);
    void randomDingTalk(JSONObject data,Integer msgSeq);
    void groupRule(JSONObject data,Integer msgSeq);
    void pjskPaneInfo(JSONObject data,Integer msgSeq);
    void pjskMusicInfo(JSONObject data,Integer msgSeq);

    void helpMenu(JSONObject data,Integer msgSeq);

    //以下是临时趣味功能
    void check749(JSONObject data,Integer msgSeq);

    //以下是默认功能
    void defaultMessage(JSONObject data,Integer msgSeq);
    void testFunction(JSONObject data,Integer msgSeq);

}
