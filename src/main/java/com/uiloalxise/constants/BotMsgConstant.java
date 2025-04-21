package com.uiloalxise.constants;

/**
 * @author Uiloalxise
 * @ClassName BotMsgConstant
 * @Description QQBOT 某些消息的常量
 */
public class BotMsgConstant {
    public static final String ERROR_UNKNOWN = "未知错误请联系管理员处理";
    public static final Integer ERROR_UNKNOWN_CODE= 0;
    public static final String ERROR_USER_NOT_FOUND = "未查找到该用户,请输入[/注册]以注册";
    public static final Integer ERROR_USER_NOT_FOUND_CODE = 1;
    public static final String ERROR_USER_EXIST = "用户已注册,无需重复注册!";
    public static final Integer ERROR__USER_EXIST_CODE = 2;

    public static final String SUCCESS_REGISTER_SUCCESS = "注册成功!";
    public static final String DEFAULT_USER_NAME = "新用户";

    //签到类
    public static final String ERROR_ALREADY_SIGNED = "您今天已经签到过了哦~";
    public static final String SUCCESS_SIGN_IN = "签到成功！获得{0}元奖励，当前余额：{1}元";
    public static final String ERROR_SIGN_IN_FAILED = "签到失败，请稍后再试";

    public static final String GROUP_RULES_URL = "https://www.faceroundcloud.site/static/group_rule.png";
    public static final String CHECK_CARD_749_URL = "https://www.faceroundcloud.site/static/749_img.jpg";
    public static final String DEFAULT_REPLY_MSG = "请不要ky🤔🤔";
    public static final String DEFAULT_DINGTALK_MSG = "哎呀我服了";
    public static final String DEFAULT_DUEL_MSG = "$winner$获得了胜利，有点强";
    public static final String DUEL_LAUNCH_MSG = "提出了决斗,请各位英雄豪杰在5分钟内接下！";
    public static final String HELP_MENU_MSG = """
                \n
                    菜单
                🤣版本:--0.8.5.1--
                😎本皇功能如下
                🤪来点宛图/邪神🤪
                🤪查卡749🤪
                🤪钉言钉语🤪
                🤤本皇帝处于开发初期阶段有问题BUG请联系管理员及时修复求求你了🤤
                😡官方qq群:916233774，不服就真实管理员吧！😡
                """;

    public static final String MENU_DUEL_MSG = """
            \n决斗菜单
            /决斗[金额] -- 用于发起决斗
            /决斗  -- 用于接受决斗
            /决斗信息 -- 自己的决斗信息
            /决斗排名 -- 决斗KD排行
            本功能目前处于测试阶段
            """;

}
