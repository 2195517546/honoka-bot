package com.uiloalxise.honoka.service.group;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupFunctionService
 * @Description QQ机器人可调用的功能总服务类
 */
public interface QQBotGroupFunctionService {
    /*
    统一由commandHandleService传入
    规范，只接受1个GroupMsgCommand如下例
    void function(Command command);
    */


    /**
     * 测试功能
     * @param command 命令实体类
     */
    void testFunction(GroupMsgCommand command);

    /**
     * 随机图片功能
     * @param command 命令实体类
     */
    void randomPic(GroupMsgCommand command);
    /**
     * 帮助菜单功能
     * @param command 命令实体类
     */
    void helpMenu(GroupMsgCommand command);
    /**
     * 钉言钉语功能
     * @param command 命令实体类
     */
    void dingTalk(GroupMsgCommand command);
    /**
     * 默认回复功能
     * @param command 命令实体类
     */
    void defaultMessage(GroupMsgCommand command);

    /**
     * 查卡947功能
     * @param command 命令实体类
     */
    void check947(GroupMsgCommand command);

    /**
     * 如果这个功能无法使用就走这一条方法
     * @param command 命令实体类
     */
    void bannedFunction(GroupMsgCommand command);

    /**
     * 签到功能
     * @param command 命令实体类
     */
    void signIn(GroupMsgCommand command);


    //以下是主要功能 ，待修改
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
     * 决斗
     * @param data 总消息处理下的d
     */
    void honokaDuel(JSONObject data);

    //以下是临时趣味功能
    void check749(JSONObject data);

    //以下是默认功能

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
