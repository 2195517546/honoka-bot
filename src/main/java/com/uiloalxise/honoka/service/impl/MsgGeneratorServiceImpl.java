package com.uiloalxise.honoka.service.impl;

import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.honoka.service.MsgGeneratorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName MsgGeneratorServiceImpl
 * @Description 文本生成用服务实现
 */
@Service
@Slf4j
public class MsgGeneratorServiceImpl implements MsgGeneratorService {

    private static final String DING_TALK_TXT = "ding_talk.txt";

    @Resource(name = "apiWebClient")
    private WebClient apiWebClient;


    /**
     * 随机钉言钉语string生成器
     * @return 一串字符串，从txt中随机抽取的一行
     */
    @Override
    public String randomDingTalk()
    {
        String filePath = DING_TALK_TXT;
        File file = new File(filePath);
        ArrayList<String> list = new ArrayList<>();

        try {
            // 检查文件是否存在
            if (!file.exists()) {
                // 如果文件不存在，则创建新文件
                boolean isCreated = file.createNewFile();
                log.info("dingtalk文件已创建：{},{}",filePath,isCreated);
            } else {

                // 如果文件存在，则读取文件内容
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        if (!list.isEmpty()) {
            Random random = new Random(System.currentTimeMillis());

            String result = list.get(random.nextInt(list.size()));

            list.clear();
            return result;
        }else {
            return BotMsgConstant.DEFAULT_DINGTALK_MSG;
        }
    }



//    /**
//     * 随机钉言钉语string生成器
//     * @return 一串字符串，从txt中随机抽取的一行
//     */
//    @Override
//    public String randomDingTalk() {
//
//        Resource resource = resourceLoader.getResource("classpath:static/dingTalk.txt");
//        InputStream inputStream = null;
//        try{
//            inputStream =resource.getInputStream();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        ArrayList<String> list = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                list.add(line);
//            }
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Random random = new Random(System.currentTimeMillis());
//
//        String result = list.get(random.nextInt(list.size()));
//
//        list.clear();
//        return result;
//    }

    /**
     * 默认回复生成器
     * @param key 关键词
     * @return 回复
     */
    @Override
    public String defaultReply(String key)
    {
        String filePath = "default_reply.txt";
        File file = new File(filePath);

        HashMap<String,String> replyMap = new HashMap<>();

        try {
            // 检查文件是否存在
            if (!file.exists()) {
                // 如果文件不存在，则创建新文件
                boolean isCreated = file.createNewFile();
                log.info("DefaultReply文件已创建：{},{}",filePath,isCreated);
            } else {

                // 如果文件存在，则读取文件内容
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    replyMap.put(line.split("-")[0],line.split("-")[1]);
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        try{
            String result = BotMsgConstant.DEFAULT_REPLY_MSG;

            Set<String> keys = replyMap.keySet();
            for(String item : keys){
                if(key.contains(item)){
                    result = replyMap.get(item);
                    break;
                }
            }



            replyMap.clear();

            return result;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 随机决斗结束语
     * $winner$替换为胜利者
     * $loser$替换为失败者
     */
    @Override
    public String randomDuelResult() {
        String filePath = "duel_msg.txt";
        File file = new File(filePath);
        ArrayList<String> list = new ArrayList<>();

        try {
            // 检查文件是否存在
            if (!file.exists()) {
                // 如果文件不存在，则创建新文件
                boolean isCreated = file.createNewFile();
                log.info("duel_msg.txt文件已创建：{},{}",filePath,isCreated);
            } else {

                // 如果文件存在，则读取文件内容
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        if (!list.isEmpty()) {
            Random random = new Random(System.currentTimeMillis());

            String result = list.get(random.nextInt(list.size()));

            list.clear();

            log.info("生成的决斗内容：{}",result);
            return result;
        }else {
            return BotMsgConstant.DEFAULT_DUEL_MSG;
        }


    }

//    @Override
//    public String defaultReply(String key) {
//
//        Resource resource = resourceLoader.getResource("classpath:static/defaultReply.txt");
//        InputStream inputStream = null;
//        try{
//            inputStream =resource.getInputStream();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//        HashMap<String,String> replyMap = new HashMap<>();
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                replyMap.put(line.split("-")[0],line.split("-")[1]);
//            }
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try{
//            String result = null;
//
//            Set<String> keys = replyMap.keySet();
//            for(String item : keys){
//                if(key.contains(item)){
//                    result = replyMap.get(item);
//                    break;
//                }
//            }
//
//
//
//            replyMap.clear();
//
//            return result;
//        }catch (Exception e){
//            log.error(e.toString());
//            return null;
//        }
//
//    }


}
