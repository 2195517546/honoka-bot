package com.uiloalxise.honoka.service.impl;


import com.alibaba.fastjson2.JSONObject;

import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.honoka.service.CommandHandleService;
import com.uiloalxise.honoka.service.QQBotGroupMsgHandleService;
import com.uiloalxise.honoka.service.QQBotGroupFunctionService;
import com.uiloalxise.honoka.service.QQBotRecordService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.payload.QQBotPayload;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;
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


    @Resource
    private QQBotRecordService qqBotRecordService;

    @Resource
    private CommandHandleService commandHandleService;

    /**
     * @param payload 直接传入
     */
    @Override
    @Async
    public void msgHandle(QQBotPayload payload) {
        String payloadType = payload.getT();
        QQBotPayloadD data = payload.getD();

        if (payloadType.equals(QQBotConstant.GROUP_AT_MESSAGE_CREATE))
        {
            GroupMsgCommand command = commandHandleService.groupMsgCommandCreator(data);
            log.info("groupMsgCommand:{}", command);
            if (command.getCommandType().equals(QQBotConstant.COMMAND_TYPE))
            {
                commandHandleService.groupCommandHandle(command);
            }else
            {
                commandHandleService.groupChatHandle(command);
            }
        }
    }

    /**
     * 消息总处理
     * @param json - 总消息处理用的完整消息
     */
    @Override
    @Async
    public void msgHandle(JSONObject json) {
        JSONObject data = json.getJSONObject("d");
        String content = data.getString("content");
        String groupId = data.getString("group_openid");


        qqBotRecordService.record(groupId);

        try {
            if (content.contains("tttest"))
            {
                qqBotGroupFunctionService.testFunction(data);
                return;
            }

            if (content.contains("一键刷屏"))
            {
                qqBotGroupFunctionService.spamFunction(data);
                return;
            }

            if (content.contains("来点"))
            {
                qqBotGroupFunctionService.randomPic(data);
                return;
            }


            if (content.contains("钉言钉语")) {
                qqBotGroupFunctionService.randomDingTalk(data);
                return;
            }

            if (content.contains("群规")) {
                qqBotGroupFunctionService.groupRule(data);
                return;
            }

            if (content.contains("/谱面") || content.contains("/pjskp")) {
                qqBotGroupFunctionService.pjskPaneInfo(data);
                return;
            }

            if (content.contains("/歌曲") || content.contains("/pjskm")) {
                qqBotGroupFunctionService.pjskMusicInfo(data);
                return;
            }

            if (content.contains("查卡749"))
            {
                qqBotGroupFunctionService.check749(data);
                return;
            }

            //决斗发起，决斗改名，查询决斗次数
            if (content.contains("/决斗"))
            {
                qqBotGroupFunctionService.honokaDuel(data);
                return;
            }


            if (content.contains("/help")) {
                qqBotGroupFunctionService.helpMenu(data);
                return;
            }

            qqBotGroupFunctionService.defaultMessage(data);
        }catch (Exception e)
        {
            log.error(e.getMessage(),e);
            qqBotGroupFunctionService.defaultMessage(data);
        }
    }



}
