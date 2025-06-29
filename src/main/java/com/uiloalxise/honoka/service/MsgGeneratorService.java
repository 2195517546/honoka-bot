package com.uiloalxise.honoka.service;

/**
 * @author Uiloalxise
 * @ClassName MsgGeneratorService
 * @Description 生成随机消息的服务
 *
 */
public interface MsgGeneratorService {

    /**
     *
     * @return
     */
    boolean deleteDingTalk(Integer index);

    /**
     *
     * @return
     */
    boolean addDingTalk(String content);

    /**
     * 全部钉言钉语，用于手动添加文本钉言钉语
     * @return
     */
    String allDingTalk();

    /**
     * 从文本中随机出一条钉言钉语
     * @return 一条钉言钉语
     */
    String randomDingTalk();

    /**
     * 通过关键词key获取一条对应的回复value
     * @param key 关键词
     * @return 一条回复value
     */
    String defaultReply(String key);


    /**
     * 随机决斗结束语
     */
    String randomDuelResult();
}
