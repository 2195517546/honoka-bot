package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * 决斗服务
 * @author Uiloalxise
 * @ClassName QQBotDuelService
 * @Description QQ决斗服务类
 */
public interface QQBotDuelService {
    /**
     * 决斗重命名昵称
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
     * 决斗资料查询
     * @param data
     * @param seq
     */
    void information(JSONObject data,Integer seq);

    /**
     * 决斗返回kd的排行榜
     * @param data
     * @param seq
     */
    void rankKD(JSONObject data,Integer seq);

    /**
     * 决斗注册账户
     * @param data
     * @param seq
     */
    void registerAccount(JSONObject data,Integer seq);

    /**
     * ！！！危险操作
     * 全部名字审核通过
     */
    void processAll();

    /**
     * 决斗菜单
     * @param data
     * @param seq
     */
    void menu(JSONObject data,Integer seq);
}
