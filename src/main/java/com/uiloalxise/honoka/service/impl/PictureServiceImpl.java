package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.honoka.service.PictureService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.uiloalxise.constants.WebConstant.RANDOM_PIC_URL;


/**
 * @author Uiloalxise
 * @ClassName PictureServiceImpl
 * @Description TODO
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {


    @Resource
    private RestTemplate restTemplate;

    /**
     * 返回一个随机的url
     *
     * @param path - 随机目标的文件目录路径参数
     * @return
     */
    @Override
    public String getRandomPicture(String path) {
        String url = RANDOM_PIC_URL + path;
        log.info(url);
        String forObject = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        String result = null;
        if (jsonObject != null) {
            result = jsonObject.getString("data");
        }else {
            result = "https://faceroundcloud.site/wantu/shion233.gif";
        }

        return result;
    }

}
