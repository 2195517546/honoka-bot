package com.uiloalxise.pojo.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Object d;
    private Integer s;
    private String t;
}
