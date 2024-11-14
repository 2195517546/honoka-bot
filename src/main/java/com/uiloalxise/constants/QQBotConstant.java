package com.uiloalxise.constants;

/**
 * @author Uiloalxise
 * @ClassName QQBotConstant
 * @Description QQBot ws连接相关的常量
 */
public class QQBotConstant {
    //监听的消息常熟
    public final static int INTENTS = 1 | 1 << 25;
    public static long hearBeatInterval;
    public final static String OPENAPI_URL = "https://api.sgroup.qq.com";
    public final static String GROUP_SUFFIX = "/v2/groups/";

}
