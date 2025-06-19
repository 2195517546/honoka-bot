package com.uiloalxise.pojo.entity.payload2;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName Message
 * @Description 消息实体类
 */
@Data
public class Message implements Serializable {
    private String type;
    private CQData data;
}
