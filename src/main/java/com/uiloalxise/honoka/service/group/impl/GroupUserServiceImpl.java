package com.uiloalxise.honoka.service.group.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.honoka.mapper.QQUserMapper;
import com.uiloalxise.honoka.mapper.user.BotUserMapper;
import com.uiloalxise.honoka.mapper.user.BotUserPointsMapper;
import com.uiloalxise.honoka.mapper.user.SignInMapper;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.group.GroupBotUserService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.user.BotUser;
import com.uiloalxise.pojo.entity.user.BotUserPoints;
import com.uiloalxise.pojo.entity.user.BotUserSignInLog;
import com.uiloalxise.pojo.vo.BotUserVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Uiloalxise
 * @ClassName GroupUserServiceImpl
 * @Description TODO
 */

@Service
@Slf4j
public class GroupUserServiceImpl implements GroupBotUserService {

    @Resource
    private QQUserMapper qqUserMapper;

    @Resource
    private  BotUserMapper botUserMapper;

    @Resource
    private  BotUserPointsMapper botUserPointsMapper;

    @Resource
    private SignInMapper botUserSignInLogMapper;


    @Resource
    @Lazy
    private GroupUserServiceImpl self;

    @Resource
    private  MessageSenderService messageSender;
    /**
     * 获得用户信息
     * @param command 命令实体类
     */
    @Override
    @Async
    public void infoBotUser(GroupMsgCommand command) {
        try {
        String openId = command.getAuthorId();
        LambdaQueryWrapper<BotUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BotUser::getOpenId, openId);


        BotUser botUser = botUserMapper.selectOne(queryWrapper);

        String avatarUrl = QQBotConstant.AVATAR_URL_PREFIX+botUser.getOpenId()+"/640";


            BotUserVO vo = self.getBotUserVO(botUser);


            if (vo != null) {
                messageSender.groupPictureMessageSender(command, avatarUrl, vo.toString() + "\n输入[/签到]可以签到", 1);
            } else {
                messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND, 1);
            }
        }catch (Exception e){
            messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND, 1);
        }
    }

    @Transactional
    protected BotUserVO getBotUserVO(BotUser botUser) {
        if (botUser == null) {
            return null;
        }

        String openId = botUser.getOpenId();
        Integer userId = botUser.getUserId();

        // 1. 查询用户积分信息
        BotUserPoints userPoints = botUserPointsMapper.selectOne(
            new LambdaQueryWrapper<BotUserPoints>()
                .eq(BotUserPoints::getOpenId, openId)
        );

        // 2. 查询今日是否签到
        Date today = new Date();

        long todaySignInCount = botUserSignInLogMapper.selectCount(
            new LambdaQueryWrapper<BotUserSignInLog>()
                .eq(BotUserSignInLog::getOpenId, openId)
                .apply("DATE(sign_date) = DATE({0})", today)
        );
        boolean isSignToday = todaySignInCount > 0;

        // 3. 构建返回对象
        return BotUserVO.builder()
            .openId(openId)
            .userId(userId)
            .nickname(botUser.getNickname())
            .money(userPoints != null ? userPoints.getMoney() : BigDecimal.ZERO)
            .createTime(botUser.getCreateTime())
            .isSignToday(isSignToday)
            .build();
    }



    /**
     * 获得用户信息
     *
     * @param openId 用户id
     * @return 用户信息
     */
    @Async
    protected CompletableFuture<BotUser> getBotUserByUserId(String openId) {
        LambdaQueryWrapper<BotUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BotUser::getOpenId, openId);

        BotUser botUser = botUserMapper.selectOne(queryWrapper);

        return CompletableFuture.completedFuture(botUser);
    }

    /**
     * 用户注册
     * @param command 命令实体类
     */
    @Override
    @Async
    public void registerBotUser(GroupMsgCommand command) {
        CompletableFuture<BotUser> future = getBotUserByUserId(command.getAuthorId());
        future.thenAccept(botUser -> {
            if (botUser != null) {
                messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_EXIST, 1);
                return;
            }
            self.saveNewBotUserAndPoints(command); // 提取事务逻辑
            messageSender.groupTextMessageSender(command, BotMsgConstant.SUCCESS_REGISTER_SUCCESS, 1);
        }).exceptionally(throwable -> handleException(command, throwable));
    }

    /**
     * 日常签到功能
     *
     * @param command 命令实体类
     */
        @Async
        @Override
        public void dailySignInBotUser(GroupMsgCommand command) {
            String openId = command.getAuthorId();

            CompletableFuture<BotUser> userFuture = getBotUserByUserId(openId);
            userFuture.thenAccept(botUser -> {
                if (botUser == null) {
                    messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND, 1);
                    return;
                }

                try {
                    // 检查今天是否已经签到
                    if (hasSignedInToday(openId)) {
                        messageSender.groupTextMessageSender(command, "您今天已经签到过了哦~", 1);
                        return;
                    }

                    // 获取昨天的签到记录
                    BotUserSignInLog yesterdaySignIn = getYesterdaySignIn(openId);
                    long currentCombo = 1; // 默认连续签到天数为1

                    if (yesterdaySignIn != null) {
                        // 如果昨天签到了，则今天的连续签到天数 = 昨天的连续签到天数 + 1
                        currentCombo = yesterdaySignIn.getCombo() != null ? yesterdaySignIn.getCombo() + 1 : 1;
                    }

                    // 创建今天的签到记录
                    BotUserSignInLog signInLog = BotUserSignInLog.builder()
                        .userId(botUser.getUserId().longValue())
                        .openId(openId)
                        .signDate(new Date())
                        .signTime(new Date())
                        .rewardValue(new BigDecimal("100.00"))
                        .createTime(new Date())
                        .comment("日常签到奖励")
                        .combo(currentCombo) // 设置连续签到天数
                        .build();

                    botUserSignInLogMapper.insert(signInLog);

                    // 如果插入成功，更新积分
                    self.updateUserPoints(botUser, command);

                    // 发送签到成功消息，包含连续签到天数
                    messageSender.groupTextMessageSender(command,
                        String.format("签到成功！已连续签到%d天", currentCombo), 1);

                } catch (Exception e) {
                    log.error("签到失败", e);
                    messageSender.groupTextMessageSender(command, "签到失败，请稍后再试", 1);
                }
            }).exceptionally(throwable -> {
                log.error("签到失败", throwable);
                messageSender.groupTextMessageSender(command, "签到失败，请稍后再试", 1);
                return null;
            });
        }

    /**
     * 检查用户今天是否已经签到
     */
    private boolean hasSignedInToday(String openId) {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(today);

        return botUserSignInLogMapper.selectCount(
            new QueryWrapper<BotUserSignInLog>()
                .eq("open_id", openId)
                .apply("DATE(sign_date) = STR_TO_DATE('" + todayStr + "', '%Y-%m-%d')")
        ) > 0;
    }

    /**
     * 获取用户昨天的签到记录
     */
    private BotUserSignInLog getYesterdaySignIn(String openId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayStr = sdf.format(yesterday);

        return botUserSignInLogMapper.selectOne(
            new QueryWrapper<BotUserSignInLog>()
                .eq("open_id", openId)
                .apply("DATE(sign_date) = STR_TO_DATE('" + yesterdayStr + "', '%Y-%m-%d')")
        );
    }

    @Transactional
    protected void updateUserPoints(BotUser botUser, GroupMsgCommand command) {
        String openId = botUser.getOpenId();

        // 更新用户积分
        BotUserPoints userPoints = botUserPointsMapper.selectOne(new QueryWrapper<BotUserPoints>()
            .eq("open_id", openId));

        if (userPoints == null) {
            userPoints = BotUserPoints.builder()
                .openId(openId)
                .userId(botUser.getUserId())
                .money(new BigDecimal("100.00"))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
            botUserPointsMapper.insert(userPoints);
        } else {
            userPoints.setMoney(userPoints.getMoney().add(new BigDecimal("100.00")));
            userPoints.setUpdateTime(new Date());
            botUserPointsMapper.updateById(userPoints);
        }

        messageSender.groupTextMessageSender(command,
            String.format("签到成功！获得100元奖励，当前余额：%.2f元", userPoints.getMoney()), 1);
    }

    /**
     * 自动用户注册
     */
    @Override
    @Async
    public void autoRegisterBotUser() {
        log.info("正在执行自动注册");
        List<String> openIds = qqUserMapper.unregisterOpenIds();

        for (String openId : openIds) {
            GroupMsgCommand command = GroupMsgCommand.builder()
                    .authorId(openId)
                    .build();
            self.saveNewBotUserAndPoints(command);
        }


    }

    /**
     * 改名,把这个用户的new名改为command的content
     *
     * @param command 命令实体类
     */
    @Override
    @Transactional
    public void changeName(GroupMsgCommand command) {
    // 获取用户信息
    BotUser existingUser = botUserMapper.selectByOpenIdForUpdate(command.getAuthorId());

        log.info("改名开始:{} ,{}",command.getContent(),existingUser);
        if (existingUser == null) {
            messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND, 1);
            return;
        }

        // 更新用户昵称
        Date date = new Date();
        BotUser newBotUser = BotUser.builder()
                .userId(existingUser.getUserId())
                .openId(existingUser.getOpenId())
                .nickname(existingUser.getNickname())
                .newNickname(command.getContent().replace("改名",""))
                .createTime(existingUser.getCreateTime())
                .updateTime(date)
                .build();

        // 执行更新操作
        int rowsAffected = botUserMapper.updateById(newBotUser);

        if (rowsAffected > 0) {
            messageSender.groupTextMessageSender(command, "恭喜你改名成功！请等待审核", 1);
        } else {
            log.error("更新用户昵称失败:{}",rowsAffected);
            messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_UNKNOWN, 1);
        }
    }

    /**
     * 返回用户金币
     *
     * @param botUser
     * @return
     */
    @Override
    public Long getMoney(BotUser botUser) {
        if (botUser == null) {
            return 0L;
        }

        // 查询用户积分信息
        BotUserPoints userPoints = botUserPointsMapper.selectOne(
            new LambdaQueryWrapper<BotUserPoints>()
                .eq(BotUserPoints::getOpenId, botUser.getOpenId())
        );

        return userPoints != null ? userPoints.getMoney().longValue() : 0L;
    }

    @Transactional(rollbackFor = Exception.class)
    protected void saveNewBotUserAndPoints(GroupMsgCommand command) {
        // 使用 SELECT ... FOR UPDATE 锁定 openId 记录
        BotUser existingUser = botUserMapper.selectByOpenIdForUpdate(command.getAuthorId());
        if (existingUser != null) {
            log.info("User with openId {} already exists, skipping", command.getAuthorId());
            return;
        }

        Date date = new Date();
        BotUser newBotUser = BotUser.builder()
                .openId(command.getAuthorId())
                .nickname(BotMsgConstant.DEFAULT_USER_NAME + command.getAuthorId().substring(1, 4))
                .createTime(date)
                .updateTime(date)
                .build();
        botUserMapper.insert(newBotUser);

        BotUser botUserNow = botUserMapper.selectByOpenId(command.getAuthorId());
        BotUserPoints newUserPoints = BotUserPoints.builder()
                .userId(botUserNow.getUserId())
                .openId(command.getAuthorId())
                .money(BigDecimal.ZERO)
                .createTime(date)
                .updateTime(date)
                .build();
        botUserPointsMapper.insert(newUserPoints);

        qqUserMapper.registerOpenId(newBotUser.getOpenId());
    }

    private Void handleException(GroupMsgCommand command,Throwable throwable) {
    messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_UNKNOWN, 1);
    log.error(throwable.getMessage());
    return null;
    }
}
