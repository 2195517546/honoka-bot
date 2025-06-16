package com.uiloalxise.utils;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Uiloalxise
 * @ClassName AIUtil
 * @Description TODO
 */
@Component
@AllArgsConstructor
@Slf4j
public class AIUtil {
    @Resource(name = "aiWebClient")
    private WebClient aiWebClient;


    String getAiResponse(String prompt, String model,String user) {

    }
}




