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
     * @param data 总消息处理下的d
     */
    void rename(JSONObject data);

    /**
     * 决斗发起或者接受
     * @param data 总消息处理下的d
     */
    void duel(JSONObject data);

    /**
     * 决斗资料查询
     * @param data 总消息处理下的d
     */
    void information(JSONObject data);

    /**
     * 决斗返回kd的排行榜
     * @param data 总消息处理下的d
     */
    void rankKD(JSONObject data);

    /**
     * 决斗注册账户
     * @param data 总消息处理下的d
     */
    void registerAccount(JSONObject data);

    /**
     * ！！！危险操作
     * 全部名字审核通过
     */
    void processAll();

    /**
     * 决斗菜单
     * @param data
     */
    void menu(JSONObject data);
}
