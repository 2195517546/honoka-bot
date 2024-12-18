package com.uiloalxise.honoka.service.impl;


import com.alibaba.fastjson2.JSONObject;

import com.uiloalxise.honoka.service.QQBotGroupMsgHandleService;
import com.uiloalxise.honoka.service.QQBotGroupFunctionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * @author Uiloalxise
 * @ClassName QQBotGroupMsgHandleServiceImpl
 * @Description QQ机器人群消息总处理服务实现
 */
@Service
@Slf4j
public class QQBotGroupMsgHandleServiceImpl implements QQBotGroupMsgHandleService {

    @Resource
    private QQBotGroupFunctionService qqBotGroupFunctionService;

    /**
     * 消息总处理
     * @param json
     */
    @Override
    @Async
    public void msgHandle(JSONObject json,Integer seq) {
        JSONObject data = json.getJSONObject("d");
        String content = data.getString("content");

        try {
            if (content.contains("tttest"))
            {
                qqBotGroupFunctionService.testFunction(data,seq);
                return;
            }

            if (content.contains("来点"))
            {
                qqBotGroupFunctionService.randomPic(data,seq);
                return;
            }


            if (content.contains("钉言钉语")) {
                qqBotGroupFunctionService.randomDingTalk(data,seq);
                return;
            }

            if (content.contains("群规")) {
                qqBotGroupFunctionService.groupRule(data,seq);
                return;
            }

            if (content.contains("/谱面") || content.contains("/pjskp")) {
                qqBotGroupFunctionService.pjskPaneInfo(data,seq);
                return;
            }

            if (content.contains("/歌曲") || content.contains("/pjskm")) {
                qqBotGroupFunctionService.pjskMusicInfo(data,seq);
                return;
            }

            if (content.contains("查卡749"))
            {
                qqBotGroupFunctionService.check749(data,seq);
                return;
            }

            //决斗发起，决斗改名，查询决斗次数
            if (content.contains("/决斗"))
            {
                qqBotGroupFunctionService.honokaDuel(data,seq);
                return;
            }


            if (content.contains("/help")) {
                qqBotGroupFunctionService.helpMenu(data,seq);
                return;
            }

            qqBotGroupFunctionService.defaultMessage(data,seq);
        }catch (Exception e)
        {
            log.error(e.getMessage(),e);
            qqBotGroupFunctionService.defaultMessage(data,seq);
        }
    }

}
