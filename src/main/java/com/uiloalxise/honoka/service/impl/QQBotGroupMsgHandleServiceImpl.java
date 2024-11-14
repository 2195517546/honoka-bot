package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import com.uiloalxise.utils.QQBotUtil;
import com.uiloalxise.honoka.service.QQBotGroupMsgHandleService;
import com.uiloalxise.honoka.service.QQBotGroupFunctionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Uiloalxise
 * @ClassName QQBotGroupMsgHandleServiceImpl
 * @Description TODO
 */
@Service
@Slf4j
public class QQBotGroupMsgHandleServiceImpl implements QQBotGroupMsgHandleService {

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private QQBotGroupFunctionService qqBotGroupFunctionService;

    @Resource
    private RestTemplate restTemplate;



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

            if (content.contains("来点宛图")) {
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
//        if (content.contains("/我要举报"))
//        {
//            xiaoyiDuel(data);
//            return;
//        }

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

    private void xiaoyiDuel(JSONObject data) {
        String groupId = "[Duel:" + data.getString("group_openid") + "]";
        String duelId =(String) redisTemplate.opsForValue().get(groupId);
        String player1 = null,player2 = null;

        //发送消息准备
        String url = QQBotConstant.OPENAPI_URL + "/v2/groups/" + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = getHeader();

        String result = null;

        //判断duelId
        if (duelId == null) {
            //如果duelId不存在，则加入一个member的ID
            duelId = data.getJSONObject("author").getString("id");
            player1 = duelId;
            redisTemplate.opsForValue().set(groupId, duelId);
            redisTemplate.expire(groupId,300, TimeUnit.SECONDS);

            result ="提出了举报";
        }
        else
        {
            //如果duelId存在,则取出其值并且删除
            player1 = duelId;
            player2 = data.getJSONObject("author").getString("member_openid");

            //删除原先数据
            redisTemplate.delete(groupId);

            Random r = new Random();
            int ranInt = r.nextInt(2) + 1;
            if (ranInt == 1) {
                result =  player1 ;
            }else
            {
                result = player2 ;
            }
        }



        JSONObject innerMarkdown = new JSONObject();
        JSONArray params = new JSONArray();




        if (result.contains("提出了举报"))
        {
            innerMarkdown.put("custom_template_id", "102098744_1729422278");
            JSONObject p1 = new JSONObject();
            JSONArray p1v = new JSONArray();
            p1v.add("有人");
            p1.put("key", "player_1");
            p1.put("values",p1v);

            JSONObject p2 = new JSONObject();
            JSONArray p2v = new JSONArray();
            p2v.add(" ");
            p2.put("key", "player_2");
            p2.put("values", p2v);

            JSONObject msg1 = new JSONObject();
            JSONArray msg1v = new JSONArray();
            msg1v.add("发起决斗");
            msg1.put("key", "message1");
            msg1.put("values", msg1v);


            JSONObject msg2 = new JSONObject();
            JSONArray msg2v = new JSONArray();
            msg2v.add(" ");
            msg2.put("key", "message2");
            msg2.put("values",msg2v);

            JSONObject res = new JSONObject();
            JSONArray resv = new JSONArray();
            resv.add(" ");
            res.put("key","result");
            res.put("values",resv);

            params.add(p1);
            params.add(p2);
            params.add(msg1);
            params.add(msg2);
            params.add(res);

            innerMarkdown.put("params", params);
        }
        else {

            innerMarkdown.put("custom_template_id", "102098744_1729770877");
            JSONObject p1 = new JSONObject();
            JSONArray p1v = new JSONArray();
            p1v.add(player1);

            p1.put("key", "player_1");
            p1.put("values",p1v);

            JSONObject p2 = new JSONObject();
            JSONArray p2v = new JSONArray();
            p2v.add(player2);
            p2.put("key", "player_2");
            p2.put("values", p2v);

            JSONObject msg1 = new JSONObject();
            JSONArray msg1v = new JSONArray();
            msg1v.add("对");
            msg1.put("key", "message1");
            msg1.put("values", msg1v);


            JSONObject msg2 = new JSONObject();
            JSONArray msg2v = new JSONArray();
            msg2v.add("发起决斗");
            msg2.put("key", "message2");
            msg2.put("values",msg2v);

            JSONObject res = new JSONObject();
            JSONArray resv = new JSONArray();
            resv.add(result);
            res.put("key","winner");
            res.put("values",resv);

            JSONObject res1 = new JSONObject();
            JSONArray res1v = new JSONArray();
            res1v.add("结果");
            res1.put("key", "result1");
            res1.put("values",res1v);

            JSONObject res2 = new JSONObject();
            JSONArray res2v = new JSONArray();
            res2v.add("赢了");
            res2.put("key", "result2");
            res2.put("values",res2v);

            params.add(p1);
            params.add(p2);
            params.add(msg1);
            params.add(msg2);
            params.add(res);
            params.add(res1);
            params.add(res2);


            innerMarkdown.put("params", params);


        }

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .markdown(innerMarkdown)
                .msgType(2)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .msgId(id)
                .build();


        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());


    }

    /**
     * 获取模板httpHeader
     */
    private HttpHeaders getHeader()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type"," application/json");
        headers.set("Authorization", qqBotUtil.getAuthorization());
        return headers;
    }

}
