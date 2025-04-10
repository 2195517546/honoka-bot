package com.uiloalxise.honoka.service.group.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.constants.QQBotConstant;
import com.uiloalxise.honoka.mapper.QQUserMapper;
import com.uiloalxise.honoka.mapper.user.BotUserMapper;
import com.uiloalxise.honoka.mapper.user.BotUserPointsMapper;
import com.uiloalxise.honoka.service.MessageSenderService;
import com.uiloalxise.honoka.service.group.GroupBotUserService;
import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import com.uiloalxise.pojo.entity.user.BotUser;
import com.uiloalxise.pojo.entity.user.BotUserPoints;
import jakarta.annotation.Resource;
import jdk.jfr.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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
        LambdaQueryWrapper<BotUser> queryWrapper = new LambdaQueryWrapper<BotUser>();
        queryWrapper.eq(BotUser::getOpenId, openId);
        BotUser botUser = botUserMapper.selectOne(queryWrapper);

        if (botUser != null) {
            messageSender.groupTextMessageSender(command,"用户存在",1);
        }else
        {
            messageSender.groupTextMessageSender(command, BotMsgConstant.ERROR_USER_NOT_FOUND,1);
        }
    }

    /**
     * 获得用户信息
     *
     * @param openId 用户id
     * @return 用户信息
     */
    @Async
    protected CompletableFuture<BotUser> getBotUserByUserId(String openId) {
        LambdaQueryWrapper<BotUser> queryWrapper = new LambdaQueryWrapper<BotUser>();
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
