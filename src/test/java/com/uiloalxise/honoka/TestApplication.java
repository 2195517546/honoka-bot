package com.uiloalxise.honoka;

import com.uiloalxise.honoka.utils.AIUtil;
import com.uiloalxise.honoka.utils.QQBotUtil;
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
        String res = null;
        try {
            res = aiUtil.getAiResponse("回复我3个字收到就行了",  "deepseek-r1:7b", "user").get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        log.info(res);

    }
}
