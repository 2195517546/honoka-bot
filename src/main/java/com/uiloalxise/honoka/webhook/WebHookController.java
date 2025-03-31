package com.uiloalxise.honoka.webhook;

import com.alibaba.fastjson2.JSONObject;
import com.uiloalxise.honoka.service.QQBotHandleService;
import com.uiloalxise.pojo.entity.payload.QQBotPayload;
import com.uiloalxise.pojo.entity.payload.QQBotPayloadD;
import com.uiloalxise.pojo.entity.payload.WebHookResult;
import com.uiloalxise.utils.QQBotUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.HexFormat;

/**
 * @author Uiloalxise
 * @ClassName WebHookController
 * @Description TODO
 */
@Slf4j
@RestController
public class WebHookController {

    @Resource
    private QQBotUtil qqBotUtil;

    @Resource
    private QQBotHandleService qqBotHandleService;

    @RequestMapping("/webhook/test")
    public String index() {
        return "Hello World";
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody QQBotPayload body,
                                 @RequestHeader("X-Signature-Ed25519") String sig,
                                 @RequestHeader("X-Signature-Timestamp") String timestamp)
    {
        log.info(body.toJson().toString());
        log.info(sig);
        log.info(timestamp);

        JSONObject json = JSONObject.from(body);
        JSONObject d = json.getJSONObject("d");
        Integer op = body.getOp();

        String seed = qqBotUtil.seedGenerator();
        KeyPair keyPair = qqBotUtil.keyPairGenerator(seed.getBytes(StandardCharsets.UTF_8));

        switch (op) {
            case 0 ->{
                qqBotHandleService.summaryHandle(body);
                return ResponseEntity.ok(true);
            }
            case 13 ->{
                log.info("验证回调.....");
                QQBotPayloadD data = JSONObject.parseObject(String.valueOf(d), QQBotPayloadD.class);
                byte[] message = (data.getEventTs() + data.getPlainToken()).getBytes(StandardCharsets.UTF_8);
                byte[] signature = qqBotUtil.messageSigner(keyPair.getPrivate(), message);

                WebHookResult result = WebHookResult.builder()
                        .plainToken(data.getPlainToken())
                        .signature(HexFormat.of().formatHex(signature))
                        .build();
                log.info("返回signature:{}",result.getSignature());
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            default ->{log.info(op.toString());}
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
