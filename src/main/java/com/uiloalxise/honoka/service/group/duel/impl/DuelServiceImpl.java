package com.uiloalxise.honoka.service.group.duel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.constants.RegexConstant;
import com.uiloalxise.honoka.mapper.duel.DuelResultMapper;
import com.uiloalxise.honoka.mapper.duel.DuelTextMapper;
import com.uiloalxise.honoka.mapper.user.BotUserMapper;
import com.uiloalxise.honoka.mapper.user.BotUserPointsMapper;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.group.GroupBotUserService;
import com.uiloalxise.honoka.service.group.duel.DuelService;
import com.uiloalxise.pojo.dto.DuelPlayerDTO;
import com.uiloalxise.pojo.dto.DuelResultDTO;
import com.uiloalxise.pojo.entity.DuelPlayer;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.duel.DuelResult;
import com.uiloalxise.pojo.entity.user.BotUser;
import com.uiloalxise.pojo.entity.user.BotUserPoints;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Uiloalxise
 * @ClassName DuelServiceImpl
 * @Description 决斗服务的实现类
 */

@Service
@Slf4j
@Async
public class DuelServiceImpl implements DuelService {

    @Resource
    @Lazy
    private DuelService self;

    @Resource
    private DuelResultMapper duelResultMapper;

    @Resource
    private BotUserPointsMapper botUserPointsMapper;

    @Resource
    private BotUserMapper botUserMapper;

    @Resource
    private MessageSenderService messageSender;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private GroupBotUserService groupBotUserService;

    @Resource
    private DuelTextMapper duelTextMapper;


    /**
     * 获取决斗排行榜
     *
     * @param command
     */
    @Override
    public void top10(GroupMsgCommand command) {

        StringBuilder result = new StringBuilder("决斗排行榜\n");


        //这里写查询逻辑
        List<DuelResultDTO> duelResultDTOS = duelResultMapper.findTop10ByWinOrder();
        for (DuelResultDTO duelResultDTO : duelResultDTOS) {
            result.append(duelResultDTO.getNickname()).append(": ").append(duelResultDTO.getWin()).append("胜").append(duelResultDTO.getLost()).append("负\n");
        }




        messageSender.groupTextMessageSender(command, result.toString(), 1);
    }



    /**
     * 决斗-发起与接受
     * @param command 命令实体类
     */
    @Override
    public void duel(GroupMsgCommand command) {

        String openId = command.getAuthorId();
        LambdaQueryWrapper<BotUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BotUser::getOpenId, openId);
        BotUser botUser = botUserMapper.selectOne(queryWrapper);
        log.info("botUser:{}",botUser);
        if (botUser != null) {
            //duel:{groupId} - {info}
            //info = {openId}:{name}:{cost}
            String duelId = "duel:" + command.getGroupId();
            String info =(String) redisTemplate.opsForValue().get(duelId);

            //决斗需消耗金额
            Long moneyNeed = null;

            Matcher duelMatcher;

            Long userMoney = groupBotUserService.getMoney(botUser);

            if (info == null)
            {
                Pattern duelPattern = Pattern.compile(RegexConstant.DUEL_REGEX);
                duelMatcher = duelPattern.matcher(command.getContent());
                //获取金额
                if (duelMatcher.find()) {
                    moneyNeed = Long.valueOf(duelMatcher.group(2));
                    log.info("money需要:{}",moneyNeed);
                }
            }
            if (moneyNeed == null)
            {
                moneyNeed = 0L;
            }

            if (moneyNeed < 0L)
            {


                messageSender.groupTextMessageSender(command, "你输入的格式有问题，这次决斗申请无效！！金额也不能小于0\n决斗格式:\n/决斗[金额]", 1);
                return;
            }//余额不足不允许决斗
            else
            if (userMoney - moneyNeed < 0L) {
                messageSender.groupTextMessageSender(command, "你的余额不足不允许决斗", 1);
                return;
            }else
            {
                BotUser player1,player2;
                BotUser winner,loser;

                if (info == null)
                {
                    info = openId + ":" + botUser.getNickname() + ":" + moneyNeed;
                     redisTemplate.opsForValue().set(duelId,info);
                     //设置存在时长，过期则删除
                    redisTemplate.expire(duelId,300, TimeUnit.SECONDS);
                    messageSender.groupTextMessageSender(command, BotMsgConstant.DUEL_LAUNCH_MSG, 1);
                    log.info("duelId:{} info:{}",duelId,info);
                    return;
                }else{
                    //获取匹配者决斗信息
                    //info = {openId}:{name}:{cost}
                    log.info("info:{}",info);
                   String[] matcherInfo = info.split(":");
                   log.info("openId:{}",matcherInfo[0]);

                   if (openId.equals(matcherInfo[0])) {
                       messageSender.groupTextMessageSender(command, "你无法与自己决斗！", 1);
                       return;
                   }else{
                       //如果duelId存在,则取出其值并且删除
                       openId = matcherInfo[0];
                       moneyNeed = Long.valueOf(matcherInfo[2]);

                       if (userMoney - moneyNeed < 0L)
                       {
                           messageSender.groupTextMessageSender(command, "你的余额不足不允许决斗", 1);
                            return;
                       }

                       queryWrapper.clear();
                       queryWrapper.eq(BotUser::getOpenId, openId);
                        player1 =  botUserMapper.selectOne(queryWrapper);
                        // 添加空值判断
                        if (player1 == null) {
                            messageSender.groupTextMessageSender(command, "发起决斗的用户不存在，请确认对方是否已注册", 1);
                            return;
                        }

                        player2 = botUser;
                        log.info("{},{}",player1,player2);

                        SecureRandom r = new SecureRandom();
                        int ranInt = r.nextInt(100) + 1;

                        log.info("player1:{} player2:{} ranInt:{}", player1, player2, ranInt);
                        if (ranInt >= 50) {
                            winner =  player1;
                            loser = player2;
                        }else
                        {
                            winner = player2;
                            loser = player1;
                        }

                        self.updateDuelResults(winner,loser, BigDecimal.valueOf(moneyNeed));
                        String duelText = duelTextMapper.selectRandomContentOne();


                        duelText = duelText.replace("$winner$", winner.getNickname()).replace("$loser$", loser.getNickname());
                        messageSender.groupTextMessageSender(command, duelText, 1);


                        String avatarUrl = QQBotConstant.AVATAR_URL_PREFIX+winner.getOpenId()+"/640";
                        messageSender.groupPictureMessageSender(command,avatarUrl,"胜利者一目了然，是:" + winner.getNickname(),2);

                        //删除原先数据
                        redisTemplate.delete(duelId);
                   }

                }
            }
        }else{
            messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND, 1);
        }
    }

    /**
     * 决斗取消
     *
     * @param command 命令实体类
     */
    @Override
    public void cancel(GroupMsgCommand command) {
        String duelId = "duel:" + command.getGroupId();
        if (redisTemplate.hasKey(duelId)) {
       redisTemplate.delete(duelId);
       messageSender.groupTextMessageSender(command, "决斗取消成功！", 1);
       log.info("成功删除 Redis 中的 key: {}", duelId);
       } else {
                messageSender.groupTextMessageSender(command, "没有进行中的决斗!取消无效。", 1);
           log.warn("key 不存在: {}", duelId);
       }
    }


    @Override
    @Transactional
    public void updateDuelResults(BotUser winner, BotUser loser, BigDecimal cost) {

        log.info("1:{}",cost);
        self.updateUserPoints(winner, cost);
        log.info("2");
        self.updateUserPoints(loser, cost.negate());
        log.info("3");
        self.updateUserDuelResult(winner, true);
        self.updateUserDuelResult(loser, false);
        log.info("决斗结果已经生成-胜利:{},失败:{}",winner,loser);
    }


    @Override
    @Transactional
    public void updateUserPoints(BotUser botUser, BigDecimal cost) {
        String openId = botUser.getOpenId();

        // 更新用户积分
        BotUserPoints userPoints = botUserPointsMapper.selectOne(new QueryWrapper<BotUserPoints>()
            .eq("open_id", openId));

        if (userPoints == null) {
            userPoints = BotUserPoints.builder()
                .openId(openId)
                .userId(botUser.getUserId())
                .money(cost)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
            botUserPointsMapper.insert(userPoints);
        } else {
            userPoints.setMoney(userPoints.getMoney().add(cost));
            userPoints.setUpdateTime(new Date());
            botUserPointsMapper.updateById(userPoints);
        }

    }

    /**
     * 增加用户胜利或者输的次数
     *
     * @param botUser - 用户
     * @param result  - 结果，true代表赢，false代表输
     */
    @Override
    public void updateUserDuelResult(BotUser botUser, Boolean result) {
        String openId = botUser.getOpenId();
        DuelResult duelResult = duelResultMapper.selectOne(new QueryWrapper<DuelResult>()
            .eq("open_id", openId));

        if (duelResult != null)
        {
            if (result)
            {
                duelResult.setWin(duelResult.getWin() + 1);
            }else{
                duelResult.setLost(duelResult.getLost() + 1);
            }
            duelResultMapper.updateById(duelResult);
        }else{
            duelResult = DuelResult.builder()
                    .id(null)
                .openId(openId)
                .win(result ? 1L : 0L)
                .lost(result ? 0L : 1L)
                .build();
            duelResultMapper.insert(duelResult);
        }
    }
}
