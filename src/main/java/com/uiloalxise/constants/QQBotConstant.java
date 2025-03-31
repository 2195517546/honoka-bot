package com.uiloalxise.constants;

/**
 * @author Uiloalxise
 * @ClassName QQBotConstant
 * @Description QQBot ws连接相关的常量
 */
public class QQBotConstant {
    //监听的消息常熟
    public final static String OPENAPI_URL = "https://api.sgroup.qq.com";
    public final static String GROUP_SUFFIX = "/v2/groups/";
    public final static String OPENAPI_GROUP_URL_PREFIX = OPENAPI_URL + GROUP_SUFFIX;
    public final static String MESSAGES_URI = "/messages";
    public final static String FILES_URI = "/files";

    public final static String GROUP_AT_MESSAGE_CREATE = "GROUP_AT_MESSAGE_CREATE";
    public final static String C2C_MESSAGE_CREATE = "C2C_MESSAGE_CREATE";

    //以下是命令相关常量
    //命令头以'/'开头
    public final static String COMMAND_START = "/";
    //命令类型命令
    public final static Integer COMMAND_TYPE = 1;
    //聊天类型命令
    public final static Integer CHAT_TYPE = 0;



}
