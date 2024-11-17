package com.uiloalxise.honoka.service;

/**
 * @author Uiloalxise
 * @ClassName MsgGeneratorService
 * @Description TODO
 * 生成随机消息的服务
 */
public interface MsgGeneratorService {

    /**
     * 从文本中随机出一条钉言钉语
     * @return 一条钉言钉语
     */
    String randomDingTalk();

    /**
     * 通过关键词key获取一条对应的回复value
     * @param key
     * @return 一条回复value
     */
    String defaultReply(String key);


    /**
     * 随机决斗结束语
     */
    String randomDuelResult();
}
