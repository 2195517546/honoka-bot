package com.uiloalxise.honoka.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.constants.WebConstant;
import com.uiloalxise.honoka.service.PictureService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;



/**
 * @author Uiloalxise
 * @ClassName PictureServiceImpl
 * @Description 图片url类服务实现
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {


    @Resource
    private RestTemplate restTemplate;

    @Resource(name = "apiWebClient")
    private WebClient apiWebClient;

    /**
     * 返回一个随机的url
     *
     * @param path - 随机目标的文件目录路径参数
     * @return 一个随机图片url
     */
    @Override
    public String getRandomPicture(String path) {
        String uri = WebConstant.RANDOM_PIC_URI + path;
        String result;
        log.info("准备uri目标:{}",uri);

        JSONObject jsonObject = apiWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();


        if (jsonObject != null) {

            result = jsonObject.getString("data");
        }else {
            result = "https://faceroundcloud.site/wantu/shion233.gif";
        }
        log.info("result:{}",result);

        result = result.replace("https://faceroundcloud.site/", "https://www.faceroundcloud.site/static/");

        return result;
    }

    /**
     * 获取pjsk功能生成的图片
     * @param openId
     * @return
     */
    @Override
    public String getPjskPicture(String openId) {
        String uri = WebConstant.GET_PJSK_PIC_URI + openId;

        return WebConstant.WEBSITE_API_URL + uri;
    }

}
