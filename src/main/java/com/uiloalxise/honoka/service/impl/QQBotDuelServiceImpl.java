package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.constants.RegexConstant;
import com.uiloalxise.honoka.mapper.QQDuelMapper;
import com.uiloalxise.honoka.service.MsgGeneratorService;
import com.uiloalxise.honoka.service.QQBotDuelService;
import com.uiloalxise.pojo.dto.DuelPlayerDTO;
import com.uiloalxise.pojo.entity.DuelPlayer;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

    @Resource
    private MsgGeneratorService msgGeneratorService;

    @Resource
    private QQDuelMapper qqDuelMapper;

    /**
     * 决斗发起或者接受
     *
     * @param data
     * @param seq
     */
    @Override
    public void duel(JSONObject data, Integer seq) {
 /*
        //发送消息准备
        String url = QQBotConstant.OPENAPI_URL + "/v2/groups/" + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();

        //返回result声明
        String result = "";

        //验证注册
        DuelPlayerDTO duelPlayerDTO = DuelPlayerDTO.builder().
                openId(getOpenId(data)).
                build();
        DuelPlayer duelPlayer = qqDuelMapper.selectDuelPlayerByOpenId(duelPlayerDTO);
        //判断是否注册
        if (duelPlayer == null)
        {
            result = "你还没有注册决斗，无法进行决斗！请输入注册命令进行注册/决斗注册";
        }else {
            //获取数据
            String command = data.getString("content");
            command = command.trim();
            Pattern duelPattern = Pattern.compile(RegexConstant.DUEL_REGEX);
            Matcher duelMatcher = duelPattern.matcher(command);

            Long moneyNeed = null;

            if (duelMatcher.find()) {
                moneyNeed = Long.valueOf(duelMatcher.group(2));
            }



            //构造群iD用于匹配决斗串key
            String groupId = "[Duel:" + data.getString("group_openid") + "]";
            //通过groupId获取duelId，如果没有则说明是发起决斗
            String duelId =(String) redisTemplate.opsForValue().get(groupId);

            String player1 = null,player2 = null;
            String winner = null,loser = null;


            //判断duelId
            if (duelId == null) {
                //如果duelId不存在，则加入一个member的ID
                //duelId = data.getJSONObject("author").getString("id");
                duelId = "{open_id:" + getOpenId(data) + ",name:" + duelPlayer.getUserName() + ",cost:" + moneyNeed +"}";

                //key:groupdID,value: {open_id:xx,name:xx,cost:123}
                redisTemplate.opsForValue().set(groupId, duelId);
                //设置存在时长，过期则删除
                redisTemplate.expire(groupId,300, TimeUnit.SECONDS);

                winner = BotMsgConstant.DUEL_LAUNCH_MSG;
            }
            else
            {
                JSONObject duelJson = JSONObject.parseObject(duelId);
                //如果duelId存在,则取出其值并且删除
                player1 = duelJson.getString("name");
//            player2 = data.getJSONObject("author").getString("member_openid");

                player2 = duelPlayer.getUserName();

                //删除原先数据
                redisTemplate.delete(groupId);



                Random r = new Random();
                int ranInt = r.nextInt(100) + 1;

                log.info("player1:" + player1 + " player2:" + player2 + " ranInt:" + ranInt);
                if (ranInt >= 50) {
                    winner =  player1 ;
                    loser = player2;
                }else
                {
                    winner = player2 ;
                    loser = player1;
                }
            }



            if (winner.contains(BotMsgConstant.DUEL_LAUNCH_MSG))
            {
                result += player1 + winner;
            }else {
                String msg = msgGeneratorService.randomDuelResult();
                msg = msg.replaceAll("\\$winner\\$",winner);
                if (loser != null) {
                    msg = msg.replaceAll("\\$loser\\$",loser);
                }
                result += msg;
            }
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
*/
    }

    /**
     * 重命名决斗名字
     *
     * @param data
     * @param seq
     */
    @Override
    @Transactional
    public void rename(JSONObject data, Integer seq) {
        String command = data.getString("content");
        command = command.trim();
        Pattern duelPattern = Pattern.compile(RegexConstant.DUEL_RENAME_REGEX);
        Matcher duelMatcher = duelPattern.matcher(command);

        String content = null;
        String newName = null;
        if (duelMatcher.find()) {
            newName = duelMatcher.group(2);
            DuelPlayerDTO duelPlayerDTO = DuelPlayerDTO.builder()
                    .openId(getOpenId(data))
                    .userName(newName)
                    .build();
            qqDuelMapper.updateDuelPlayer(duelPlayerDTO);
            content = "改名申请已提交请等待审核";

        }else{
            content = "格式出错请重新尝试";
        }

        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();





        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(content)
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
     * 资料查询
     *
     * @param data
     * @param seq
     */
    @Override
    public void information(JSONObject data, Integer seq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();
        String content = null;
        DuelPlayer duelPlayer = qqDuelMapper.selectDuelPlayerByOpenId(DuelPlayerDTO.builder().
                openId(getOpenId(data)).
                build());

        if (duelPlayer != null) {
            content = String.format("""
                    你的信息如下：
                    名号:%s
                    目前持有金币:%d
                    胜场:%d
                    败场:%d
                    """,duelPlayer.getUserName(),
                    duelPlayer.getMoney(),
                    duelPlayer.getWin(),
                    duelPlayer.getLost()
                    );
        }else{
            content = "你还没有信息，请输入注册/决斗注册，进行注册";
        }

        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(content)
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
     * 根据KD排行
     * @param data
     * @param seq
     */
    @Override
    public void rankKD(JSONObject data, Integer seq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();

        List<DuelPlayer> listByKd = qqDuelMapper.rankDuelPlayerByKD();

        StringBuilder content = new StringBuilder("决斗KD榜\n");

        Integer rank = 1;

        for (DuelPlayer player:listByKd)
        {


            content.append("排名[").append(rank).append("]-用户名[").append(player.getUserName()).append("]-KD[").append(player.getWin()).append("/").append(player.getLost()).append("]\n");
        }


        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(content.toString())
                .msgType(0)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .msgId(id)
                .msgSeq(seq)
                .build();

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

    @Override
    @Transactional
    public void registerAccount(JSONObject data, Integer seq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();

        String content = null;

        DuelPlayerDTO duelPlayerDTO = DuelPlayerDTO.builder().
                openId(getOpenId(data)).
                userName("").
                money(100L).
                win(0).
                lost(0).
                build();

        DuelPlayer duelPlayer = qqDuelMapper.selectDuelPlayerByOpenId(duelPlayerDTO);

        if (duelPlayer == null) {
            duelPlayerDTO.setUserName("决斗士" + System.currentTimeMillis());
            qqDuelMapper.addDuelPlayer(duelPlayerDTO);
            content = "注册成功，获得初始金币100，输入/决斗改名[新名字]更改";
        }else{
            content = "你已经注册过了，用户[" + duelPlayer.getUserName() + "]，你无需再注册！";
        }


        QQGroupsMsg qqGroupsMsg = QQGroupsMsg.builder()
                .content(content)
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
     * 获取openid
     * @param data
     * @return
     */
    private String getOpenId(JSONObject data) {
        return data.getJSONObject("author").getString("member_openid");
    }
}
