package com.uiloalxise.honoka.service.impl;

import com.uiloalxise.constants.BotMsgConstant;
import com.uiloalxise.honoka.service.MsgGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName MsgGeneratorServiceImpl
 * @Description TODO
 */
@Service
@Slf4j
public class MsgGeneratorServiceImpl implements MsgGeneratorService {
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 随机钉言钉语string生成器
     * @return 一串字符串，从txt中随机抽取的一行
     */
    @Override
    public String randomDingTalk()
    {
        String filePath = "ding_talk.txt";
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
     * @param key
     * @return
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