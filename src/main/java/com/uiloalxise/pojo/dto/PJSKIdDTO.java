package com.uiloalxise.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName PJSKIdDTO
 * @Description TODO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PJSKIdDTO implements Serializable {
    @JsonProperty("sound_id")
    private int soundId;

    @JsonProperty("open_id")
    private String openId;
}
