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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        String openId = command.getAuthorId();
        LambdaQueryWrapper<BotUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BotUser::getOpenId, openId);
        BotUser botUser = botUserMapper.selectOne(queryWrapper);

        String avatarUrl = QQBotConstant.AVATAR_URL_PREFIX+botUser.getOpenId()+"/640";

        BotUserVO vo = self.getBotUserVO(botUser);


        if (vo != null) {
            messageSender.groupPictureMessageSender(command,avatarUrl,"本功能暂时不提供改名" + vo.toString(),1);
        }else
        {
            messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND,1);
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
                // 尝试直接插入签到记录
                BotUserSignInLog signInLog = BotUserSignInLog.builder()
                    .userId(botUser.getUserId().longValue())
                    .openId(openId)
                    .signDate(new Date())
                    .signTime(new Date())
                    .rewardValue(new BigDecimal("100.00"))
                    .createTime(new Date())
                    .comment("日常签到奖励")
                    .build();

                botUserSignInLogMapper.insert(signInLog);

                // 如果插入成功，更新积分
                self.updateUserPoints(botUser, command);

            } catch (DuplicateKeyException e) {
                // 唯一键冲突，说明已经签到过
                messageSender.groupTextMessageSender(command, "您今天已经签到过了哦~", 1);
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

    @Transactional(rollbackFor = Exception.class)
    protected void saveNewBotUserAndPoints(GroupMsgCommand command) {
        Date date = new Date();
        // 添加用户
        BotUser newBotUser = BotUser.builder()
                .openId(command.getAuthorId())
                .nickname(BotMsgConstant.DEFAULT_USER_NAME + command.getAuthorId().substring(1, 4))
                .createTime(date)
                .updateTime(date)
                .build();
        botUserMapper.insert(newBotUser);

        // 再次查询用户
        BotUser botUserNow = getBotUserByUserId(command.getAuthorId()).join();
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
