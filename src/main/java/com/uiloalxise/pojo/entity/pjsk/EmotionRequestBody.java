package com.uiloalxise.pojo.entity.pjsk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName EmotionRequestBody
 * @Description TODO
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class EmotionRequestBody implements Serializable {

    @JsonProperty("open_id")
    private String openId;

    private String content;
}
