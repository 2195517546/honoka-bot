package com.uiloalxise.honoka.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupMsgHandleService
 * @Description TODO
 */
public interface QQBotGroupMsgHandleService {

   /**
    * 作为群聊处理的总方法
    * @param json
    * @param seq
    */
   void msgHandle(JSONObject json,Integer seq);
}
