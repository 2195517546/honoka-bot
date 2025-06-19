package com.uiloalxise.honoka;

import com.uiloalxise.honoka.mapper.user.BotUserMapper;
import com.uiloalxise.honoka.utils.AIUtil;
import com.uiloalxise.honoka.utils.QQBotUtil;
import com.uiloalxise.pojo.entity.user.BotUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;


/**
 * @author Uiloalxise
 * @ClassName TestApplication
 * @Description junit入口
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestApplication {

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private AIUtil aiUtil;

    @Resource
    private BotUserMapper botUserMapper;

    @Test
    public void test() {
        BotUser botUser = botUserMapper.selectByOpenId("280ABEC05AD07F3BA29F7C55A13C7C23");
        log.info(botUser.toString());

    }
}
