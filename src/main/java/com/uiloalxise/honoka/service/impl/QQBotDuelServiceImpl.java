package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.constants.RegexConstant;
import com.uiloalxise.honoka.mapper.QQDuelMapper;
import com.uiloalxise.honoka.service.MsgGeneratorService;
import com.uiloalxise.honoka.service.QQBotDuelService;
import com.uiloalxise.honoka.service.QQBotDuelTransactionService;
import com.uiloalxise.pojo.dto.DuelPlayerDTO;
import com.uiloalxise.pojo.entity.DuelPlayer;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import com.uiloalxise.utils.QQBotUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
 * @Description QQ决斗服务类实现
 */
@Slf4j
@Service
@Async
public class QQBotDuelServiceImpl implements QQBotDuelService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private MsgGeneratorService msgGeneratorService;

    @Resource
    private QQDuelMapper qqDuelMapper;

    @Resource
    private QQBotDuelTransactionService qqBotDuelTransactionService;

    /**
     * 决斗发起或者接受
     *
     * @param data
     * @param seq
     */
    @Override
    public void duel(JSONObject data, Integer seq) {

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
        }else
        {
            //构造群iD用于匹配决斗串key
            String groupId = "[Duel:" + data.getString("group_openid") + "]";
            //通过groupId获取duelId，如果没有则说明是发起决斗
            String duelId =(String) redisTemplate.opsForValue().get(groupId);

            //获取数据
            String command = data.getString("content");
            command = command.trim();
            Matcher duelMatcher = null;

            //声明需要金额
            Long moneyNeed = null;
            //把duelId序列化成JSON
            JSONObject duelJson = null;

            //不存在duelId则获取cmd的金额，否则获取json获取金额，
            if (duelId == null)
            {
                Pattern duelPattern = Pattern.compile(RegexConstant.DUEL_REGEX);
                duelMatcher = duelPattern.matcher(command);
                //获取金额
                if (duelMatcher.find()) {
                    moneyNeed = Long.valueOf(duelMatcher.group(2));
                }
            }
            else{
                duelJson = JSONObject.parse(duelId);
                moneyNeed = duelJson.getLong("cost");
            }

            //如果为null则下一步报错
            if (moneyNeed == null)
            {
                moneyNeed = -1L;
            }
            if (moneyNeed < 0L)
            {
                result = "你输入的格式有问题，这次决斗申请无效！！";
            }
            //余额不足不允许决斗
            else
            if (duelPlayer.getMoney().longValue() - moneyNeed.longValue() < 0) {
                result = "你的余额不足，不允许决斗！！！";
            }else
            {
                //声明各种参数
                DuelPlayer player1 = null,player2 = null;
                DuelPlayerDTO player1DTO = new DuelPlayerDTO(),player2DTO = new DuelPlayerDTO();
                String winner = null,loser = null;


                //判断duelId
                if (duelId == null) {
                    //如果duelId不存在，则加入一个member的ID
                    //duelId = data.getJSONObject("author").getString("id");
                    duelId = "{\"open_id\":\"" + getOpenId(data) + "\",\"name\":\"" + duelPlayer.getUserName() + "\",\"cost\":" + moneyNeed +"}";

                    //key:groupdID,value: {open_id:xx,name:xx,cost:123}
                    redisTemplate.opsForValue().set(groupId, duelId);
                    //设置存在时长，过期则删除
                    redisTemplate.expire(groupId,300, TimeUnit.SECONDS);

                    result = BotMsgConstant.DUEL_LAUNCH_MSG;
                }
                else
                {

                    //获取匹配者决斗ID
                    String openId = duelJson.getString("open_id");


                    //无法自己与自己决斗
                    if (openId.equals(duelPlayer.getOpenId())) {
                        result = "你无法与自己决斗！";
                        //决斗
                    }else {
                        //如果duelId存在,则取出其值并且删除
                        player1 = DuelPlayer.builder().
                                userName(duelJson.getString("name")).
                                openId(duelJson.getString("open_id")).
                                build();

                        player2 = DuelPlayer.builder().
                                userName(duelPlayer.getUserName()).
                                openId(getOpenId(data)).
                                money(duelPlayer.getMoney()).
                                win(duelPlayer.getWin()).
                                lost(duelPlayer.getLost()).
                                build(); ;

                        //完整p1和p2的DTO,用于后续修改数据
                        BeanUtils.copyProperties(player1,player1DTO);
                        player1 = qqDuelMapper.selectDuelPlayerByOpenId(player1DTO);
                        BeanUtils.copyProperties(player1,player1DTO);
                        BeanUtils.copyProperties(player2,player2DTO);

                        //删除原先数据
                        redisTemplate.delete(groupId);

                        Random r = new Random();
                        int ranInt = r.nextInt(100) + 1;

                        log.info("player1:" + player1 + " player2:" + player2 + " ranInt:" + ranInt);
                        if (ranInt >= 50) {
                            winner =  player1.getUserName();
                            loser = player2.getUserName();
                            qqBotDuelTransactionService.updateDuelResults(player1DTO,player2DTO,moneyNeed);
                        }else
                        {
                            winner = player2.getUserName();
                            loser = player1.getUserName();
                            qqBotDuelTransactionService.updateDuelResults(player2DTO,player1DTO,moneyNeed);
                        }
                    }
                }

                if (!result.contains(BotMsgConstant.DUEL_LAUNCH_MSG) && winner != null)
                {
                    String msg = msgGeneratorService.randomDuelResult();
                    msg = msg.replaceAll("\\$winner\\$",winner);
                    if (loser != null) {
                        msg = msg.replaceAll("\\$loser\\$",loser);
                    }
                    result += msg;
                }
            }
        }

        QQGroupsMsg qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(result,id,seq);

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

            qqBotDuelTransactionService.updateName(duelPlayerDTO);
            content = "改名申请已提交请等待审核,一般每天审核两次，请耐心等待";

        }else{
            content = "格式出错请重新尝试";
        }

        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();

        QQGroupsMsg qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(content,id,seq);

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

        QQGroupsMsg qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(content,id,seq);

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

        List<DuelPlayer> listByKd = qqDuelMapper.rankDuelPlayerByKd();

        StringBuilder content = new StringBuilder("\n决斗KD榜\n");

        int rank = 1;

        for (DuelPlayer player:listByKd)
        {
            content.append("排名[").append(rank++).append("]-用户名[").append(player.getUserName()).append("]-KD[").append(player.getWin()).append("/").append(player.getLost()).append("]\n");
        }


        QQGroupsMsg qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(content.toString(),id,seq);

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
            qqBotDuelTransactionService.register(duelPlayerDTO);
            content = "注册成功，获得初始金币100，输入/决斗改名[新名字]更改";
        }else{
            content = "你已经注册过了，用户[" + duelPlayer.getUserName() + "]，你无需再注册！";
        }


        QQGroupsMsg qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(content,id,seq);

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


    /**
     * 危险操作一般禁止使用
     */
    @Override
    public void processAll() {
        qqBotDuelTransactionService.processAll();
    }

    @Override
    public void menu(JSONObject data, Integer seq) {
        String url = QQBotConstant.OPENAPI_URL + QQBotConstant.GROUP_SUFFIX + data.getString("group_openid");
        String id = data.getString("id");
        HttpHeaders headers = qqBotUtil.getHeader();

        String content = BotMsgConstant.MENU_DUEL_MSG;


        QQGroupsMsg qqGroupsMsg = qqBotUtil.qqGroupsTextMsg(content,id,seq);

        HttpEntity<QQGroupsMsg> qqGroupsMsgEntity = new HttpEntity<>(qqGroupsMsg,headers);
        ResponseEntity<JSONObject> groupMsgResp = restTemplate.exchange(url + "/messages", HttpMethod.POST, qqGroupsMsgEntity, JSONObject.class);
        log.info("消息发送成功：{}", groupMsgResp.getBody());
    }

}
