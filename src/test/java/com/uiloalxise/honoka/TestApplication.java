package com.uiloalxise.honoka;

import com.uiloalxise.honoka.utils.AIUtil;
import com.uiloalxise.honoka.utils.QQBotUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Uiloalxise
 * @ClassName TestApplication
 * @Description TODO
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestApplication {

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private AIUtil aiUtil;

    @Test
    public void test() {
        String res = String.valueOf(aiUtil.getAiResponse("钉钉十号被王八草了怎么办",  "deepseek-r1:7b", "user"));

        log.info(res);

    }
}
