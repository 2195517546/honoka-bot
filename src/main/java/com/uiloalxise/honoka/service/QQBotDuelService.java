package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * 决斗服务
 * @author Uiloalxise
 * @ClassName QQBotDuelService
 * @Description TODO
 */
public interface QQBotDuelService {
    /**
     * 重命名决斗名字
     * @param data
     * @param seq
     */
    void rename(JSONObject data,Integer seq);

    /**
     * 决斗发起或者接受
     * @param data
     * @param seq
     */
    void duel(JSONObject data,Integer seq);

    /**
     * 资料查询
     * @param data
     * @param seq
     */
    void information(JSONObject data,Integer seq);
}
