package com.uiloalxise.pojo.entity.payload;

import com.alibaba.fastjson2.JSONObject;
import lombok.*;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQBotPayload
 * @Description QQBOT payload的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQBotPayload implements Serializable {
    private String id;
    private Integer op;
    private QQBotPayloadD d;
    private Integer s;
    private String t;

    public JSONObject toJson()
    {
        return JSONObject.from(this);
    }
}
