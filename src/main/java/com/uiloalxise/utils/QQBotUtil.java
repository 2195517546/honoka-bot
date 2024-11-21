package com.uiloalxise.utils;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author Uiloalxise
 * @ClassName QQBotUtil
 * @Description QQ机器人工具类
 */
@Data
@AllArgsConstructor
@Slf4j
public class QQBotUtil {
    private String appId;
    private String appSecret;
    private String token;
    private String qqNumber;

    private final static String ACCESS_TOKEN_URL = "https://bots.qq.com/app/getAppAccessToken";
    private final static String AUTHOR_URL = "https://api.sgroup.qq.com";
    private final static String GATE_WAY_URL = "https://api.sgroup.qq.com/gateway/bot";

    private final static String AUTHORIZATION_STRING = "Authorization";

    /**
     * 发起ws连接请求
     * @return
     */
    public JSONObject getWebsocket() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type"," application/json");
        headers.set("Authorization", getAuthorization());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(GATE_WAY_URL, HttpMethod.GET, entity, JSONObject.class);

        return Objects.requireNonNull(responseEntity.getBody());
    }

    /**
     * 获取通过凭证
     * @return
     */
    public JSONObject getAccessToken()
    {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type"," application/json");

        JSONObject body = new JSONObject();
        body.put("appId", appId);
        body.put("clientSecret", appSecret);

        HttpEntity<String> entity = new HttpEntity<>(body.toJSONString(),headers);


        ResponseEntity<JSONObject> response = restTemplate.exchange(ACCESS_TOKEN_URL,
                HttpMethod.POST,
                entity,
                JSONObject.class);

        log.info("获取凭证成功:{},凭证时间：{}ms", Objects.requireNonNull(response.getBody()).get("access_token"),response.getBody().get("expires_in"));

        return response.getBody();
    }

    /**
     * 获取鉴权凭证
     * ,格式为
     * {QQBot access_token}
     * @return
     */
    public String getAuthorization()
    {
        return "QQBot " +getAccessToken().getString("access_token");
    }


    /**
     * 获取常用http header
     * @return
     */
    public HttpHeaders getHeader()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type"," application/json");
        headers.set("Authorization", getAuthorization());
        return headers;
    }


    /**
     * 生成一般的文本消息群消息
     * @param content
     * @param msgId
     * @param msgSeq
     * @return
     */
    public QQGroupsMsg qqGroupsTextMsg(String content,String msgId,Integer msgSeq)
    {
        return QQGroupsMsg.builder()
                .content(content)
                .msgType(0)
                .eventId("GROUP_AT_MESSAGE_CREATE")
                .msgId(msgId)
                .msgSeq(msgSeq)
                .build();
    }

    /**
     * 从d的json获取openid
     * @param data
     * @return
     */
    public String getOpenId(JSONObject data) {
        return data.getJSONObject("author").getString("member_openid");
    }

}
