package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupMsgHandleService
 * @Description QQ机器人群消息总处理服务
 */
public interface QQBotGroupMsgHandleService {

   /**
    * 作为群聊处理的总方法
    * @param json
    */
   void msgHandle(JSONObject json);
}
