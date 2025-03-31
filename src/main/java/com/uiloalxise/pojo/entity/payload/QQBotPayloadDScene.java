package com.uiloalxise.pojo.entity.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQBotPayloadDScene
 * @Description TODO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQBotPayloadDScene implements Serializable {

    private String source;

    @JsonProperty("callback_data")
    private Object callbackData;
}
