package com.uiloalxise.utils;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.pojo.entity.QQGroupsMsg;
import com.uiloalxise.pojo.entity.payload.CustomPrivateKey;
import com.uiloalxise.pojo.entity.payload.CustomPublicKey;
import com.uiloalxise.pojo.entity.token.QQBotAccessToken;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

/**
 * @author Uiloalxise
 * @ClassName QQBotUtil
 * @Description QQ机器人工具类
 */
@Data
@Slf4j
public class QQBotUtil {
    private String appId;
    private String appSecret;
    private String token;
    private String qqNumber;

    public QQBotUtil(String appId, String appSecret, String token, String qqNumber) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.token = token;
        this.qqNumber = qqNumber;
    }

    private final static String ACCESS_TOKEN_URL = "https://bots.qq.com/app/getAppAccessToken";
    private final static String AUTHOR_URL = "https://api.sgroup.qq.com";
    private final static String GATE_WAY_URL = "https://api.sgroup.qq.com/gateway/bot";



    private final static String USER_HEAD_URL = "https://q.qlogo.cn/qqapp/102098744/";
    private final static String AUTHORIZATION_STRING = "Authorization";

    private static final int ED25519_SEED_SIZE = 32;
    private static final int ED25519_SIGNATURE_SIZE = 64;

    public boolean verifySignature(String signatureHex,String timestamp,byte[] httpBody,byte[] publicKeyBytes)
    {
        try{
            byte[] sig = HexFormat.of().parseHex(signatureHex);

            if(sig.length != ED25519_SIGNATURE_SIZE || (sig[63] & 0xE0)!=0)
            {
                log.warn("Invalid signature!签名格式错误！");
                return false;
            }

            ByteArrayOutputStream msg = new ByteArrayOutputStream();
            msg.write(timestamp.getBytes(StandardCharsets.UTF_8));
            msg.write(httpBody);
            Ed25519Signer signer = new Ed25519Signer();
            signer.init(false,new Ed25519PublicKeyParameters(publicKeyBytes,0));
            signer.update(msg.toByteArray(),0,msg.size());

            return signer.verifySignature(sig);

        }catch (Exception e)
        {
            log.error(e.getMessage(),e);
            return false;
        }

    }

    public KeyPair keyPairGenerator(byte[] seed) {
        Ed25519KeyPairGenerator generator = new Ed25519KeyPairGenerator();
        generator.init(new KeyGenerationParameters(null,ED25519_SEED_SIZE));

        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(seed,0);
        Ed25519PublicKeyParameters publicKeyParams = privateKeyParams.generatePublicKey();

        byte[] publicKeyBytes = publicKeyParams.getEncoded();
        byte[] privateKeyBytes = privateKeyParams.getEncoded();
        return new KeyPair(
                new CustomPublicKey(publicKeyBytes),
                new CustomPrivateKey(privateKeyBytes)
        );
    }

    public String seedGenerator()
    {
        String seed = this.getAppSecret();
        if(seed.length() < ED25519_SEED_SIZE)
        {
            seed = seed.repeat(2);
        }
        return seed.substring(0, ED25519_SEED_SIZE);
    }

    public byte[] messageSigner(PrivateKey privateKey, byte[] message)
    {
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true,new Ed25519PrivateKeyParameters(privateKey.getEncoded(),0));
        signer.update(message, 0, message.length);
        return signer.generateSignature();
    }

    //存储的accesstoken
    private QQBotAccessToken accessToken = null;


    @Resource
    RestTemplate restTemplate;

    /**
     * 获取通过凭证
     * @return 获取token
     */
    public void freshAccessToken()
    {
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type"," application/json");

        JSONObject body = new JSONObject();
        body.put("appId", appId);
        body.put("clientSecret", appSecret);

        HttpEntity<String> entity = new HttpEntity<>(body.toJSONString(),headers);


        ResponseEntity<QQBotAccessToken> response = restTemplate.exchange(ACCESS_TOKEN_URL,
                HttpMethod.POST,
                entity,
                QQBotAccessToken.class);

        log.info("获取凭证成功:{},凭证时间：{}s", Objects.requireNonNull(response.getBody()).getAccessToken(),response.getBody().getExpiresIn());

        accessToken = response.getBody();
    }

    /**
     * 获取鉴权凭证
     * ,格式为
     * {QQBot access_token}
     * @return 获取auth
     */
    public String getAuthorization()
    {
        return "QQBot " +getAccessToken().getAccessToken();
    }


    /**
     * 获取常用http header
     * @return 默认http头部
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
     * @param content 文本
     * @param msgId 消息id
     * @param msgSeq 消息seq
     * @return qq群消息实体类
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
     * @param data d
     * @return 一个openid
     */
    public String getOpenId(JSONObject data) {
        return data.getJSONObject("author").getString("member_openid");
    }

}
