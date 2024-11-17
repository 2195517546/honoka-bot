package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.constants.RegexConstant;
import com.uiloalxise.honoka.service.QQBotDuelService;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import com.uiloalxise.utils.QQBotUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Uiloalxise
 * @ClassName QQBotDuelServiceImpl
 * @Description TODO
 */
@Slf4j
@Service
@Async
public class QQBotDuelServiceImpl implements QQBotDuelService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private QQBotUtil qqBotUtil;

    /**
     * 决斗发起或者接受
     *
     * @param data
     * @param seq
     */
    @Override
    public void duel(JSONObject data, Integer seq) {

        //获取数据
        String command = data.getString("content");
        command = command.trim();
        Pattern duelPattern = Pattern.compile(RegexConstant.DUEL_REGEX);
        Matcher duelMatcher = duelPattern.matcher(command);

        String nickName = null;

        if (duelMatcher.find()) {
            nickName = duelMatcher.group(2);
        }



        //构造群iD用于匹配决斗串key
        String groupId = "[Duel:" + data.getString("group_openid") + "]";

        String duelId =(String) redisTemplate.opsForValue().get(groupId);
        String player1 = null,player2 = null;

        //发送消息准备
        String url = QQBotConstant.OPENAPI_URL + "/v2/groups/" + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();

        String playResult = null;
        String result = "";

        //判断duelId
        if (duelId == null) {
            //如果duelId不存在，则加入一个member的ID
            //duelId = data.getJSONObject("author").getString("id");
            duelId = nickName;
            player1 = duelId;

            //key:groupdID,value:duelId
            redisTemplate.opsForValue().set(groupId, duelId);

            //设置存在时长，过期则删除
            redisTemplate.expire(groupId,300, TimeUnit.SECONDS);

            playResult ="提出了举报";
        }
        else
        {
            //如果duelId存在,则取出其值并且删除
            player1 = duelId;
//            player2 = data.getJSONObject("author").getString("member_openid");

            player2 = nickName;

            //删除原先数据
            redisTemplate.delete(groupId);



            Random r = new Random();
            int ranInt = r.nextInt(100) + 1;

            log.info("player1:" + player1 + " player2:" + player2 + " ranInt:" + ranInt);
            if (ranInt >= 50) {
                playResult =  player1 ;
            }else
            {
                playResult = player2 ;
            }
        }

        if (playResult.contains("提出了举报"))
        {
            result += player1 + playResult;
        }else {
            result += playResult + "获得了胜利,有点强";
        }

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(result)
                .msgType(0)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .msgId(id)
                .msgSeq(seq)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());

    }

    /**
     * 重命名决斗名字
     *
     * @param data
     * @param seq
     */
    @Override
    public void rename(JSONObject data, Integer seq) {

    }



    /**
     * 资料查询
     *
     * @param data
     * @param seq
     */
    @Override
    public void information(JSONObject data, Integer seq) {

    }
}
