package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.pojo.entity.QQBotPayload;
import com.uiloalxise.utils.QQBotUtil;
import com.uiloalxise.honoka.service.QQBotGroupMsgHandleService;
import com.uiloalxise.honoka.service.QQBotHandleService;
import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Uiloalxise
 * @ClassName QQBotHandleServiceImpl
 * @Description QQ机器人消息总处理类
 */
@Service
@Slf4j
@Async
public class QQBotHandleServiceImpl implements QQBotHandleService {

    private static Thread ackThread = null;
    private static String sessionId = null;
    private static Integer sInt = null;

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private QQBotGroupMsgHandleService qqBotGroupMsgHandleService;

    /**
     * 总处理器，直接调用<br>
     *
     * @param json - 代表ws收到的json
     * @return null 则无结果
     */
    @Override
    public Future<JSONObject> summaryHandle(JSONObject json, Session session)
    {

        // 模拟一些异步操作
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        try {


            JSONObject result = null;

            if (json.getString("s") != null) {

                sInt = Integer.valueOf(json.getString("s"));
                log.info("s值改变，当前s:{}", sInt);
            }

            if("11".equals(json.getString("op")))
            {
                log.info("收到QQ ack返回消息");
                return null;
            }

            if ("0".equals(json.getString("op"))) {

                String event = json.getString("t");

                if ("GROUP_AT_MESSAGE_CREATE".equals(event)) {
                    qqBotGroupMsgHandleService.msgHandle(json);
                }

                if ("READY".equals(event)) {
                    readyEventHandle(session);
                }
            }

            //调用登录凭证
            if ("10".equals(json.getString("op"))) {

//            if(sessionId == null) {

                result = buildLoginAuthToken(json);

//            }else
                //{
                //   result = ResumeHandle(session);
                //}
            }

            //处理为null的情况
            log.info("Summary Handle的结果：{}", result);
            future.complete(result);
            return future;
        }catch (Exception e)
        {
            future.completeExceptionally(e);
            return future;
        }



    }


    /**
     * 返回json如下例
     * {
     *   "op": 2,
     *   "d": {
     *     "token": "token string",
     *     "intents": 513,
     *     "shard": [0, 4],
     *     "properties": {
     *       "$os": "linux",
     *       "$browser": "my_library",
     *       "$device": "my_library"
     *     }
     *   }
     * }
     * @param json - ws收到的json
     * @return 返回一个用于登录用的payload
     */
    private JSONObject buildLoginAuthToken(JSONObject json)
    {

        QQBotConstant.hearBeatInterval = json.getJSONObject("d").getLong("heartbeat_interval");

        QQBotPayload payload = QQBotPayload.builder().
        op(2).
        build();


        JSONObject data = new JSONObject();
        data.put("token", qqBotUtil.getAuthorization());
        data.put("intents", QQBotConstant.INTENTS);
        data.put("shards", new int[] {0,1});

        JSONObject properties = new JSONObject();
        properties.put("$os", "linux");
        properties.put("$browser", "my_library");
        properties.put("$device", "my_library");

        data.put("properties", properties);

        payload.setD(data);

        return JSONObject.from(payload);
    }


    //处理readyEvent,一般整个session只有一次
    private void readyEventHandle(Session session)
    {


        //发送初次心跳
        try {
            session.getBasicRemote().sendText(
                    ackBuilder(null).toString()
            );
            log.info("发送初次心跳:{}",session.getId());

        }catch (Exception e){
            log.error(e.getMessage());
        }

        if (ackThread != null && ackThread.isAlive()) {
            ackThread.interrupt();

        }

        ackThread = new Thread(() -> {
            try {
                while(true) {
                    Thread.sleep(QQBotConstant.hearBeatInterval);
                    session.getBasicRemote().sendText(ackBuilder(sInt).
                            toString());

                    log.info("发送心跳成功,s的值为：{},session的Id为：{}", sInt, sessionId);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        });
        sessionId = session.getId();

        ackThread.start();
    }

    /**
     * {
     *   "op": 6,
     *   "d": {
     *     "token": "my_token",
     *     "session_id": "session_id_i_stored",
     *     "seq": sInt
     *   }
     * }
     * @param session 用于重启的session
     */
    private JSONObject resumeHandle(Session session, Integer sInt)
    {

        ackThread.interrupt();

        JSONObject data = new JSONObject();
        data.put("token", qqBotUtil.getAuthorization());
        data.put("session_id", sessionId);
        data.put("seq", sInt);

        try {

            session.getBasicRemote().sendText(ackBuilder(sInt).toString());
            log.info("发送重新连接请求心跳");
        }catch (Exception e){
            log.error(e.getMessage());
        }

        ackThread = new Thread(() -> {
            try {
                while(true) {
                    Thread.sleep(QQBotConstant.hearBeatInterval);
                    session.getBasicRemote().sendText(ackBuilder(sInt).toString());
                    log.info("发送心跳成功");
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        sessionId = session.getId();
        ackThread.start();

        QQBotPayload payload = QQBotPayload.builder().op(6)
                .d(data)
                .build();

        return JSONObject.from(payload);
    }


    /**
     * 构建一个心跳并且返回json
     * @param sInt 可能是static sInt或者null
     * @return 构建一个心跳payload
     */
    private JSONObject ackBuilder(Integer sInt)
    {
        QQBotPayload payload = QQBotPayload.builder().
                op(1).
                d(sInt).
                build();

        log.info("心跳json构建成功：{}",payload);

        return JSONObject.from(payload);
    }

}
