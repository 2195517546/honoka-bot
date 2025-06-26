package com.uiloalxise.pojo.entity.payload2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName Sender
 * @Description TODO
 */
@Data
public class Sender implements Serializable {
    @JsonProperty("user_id")
    private String userId;
    private String nickname;
    private String card;
    private String role;
}
