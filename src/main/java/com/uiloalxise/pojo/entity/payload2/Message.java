package com.uiloalxise.pojo.entity.payload2;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName Message
 * @Description TODO
 */
@Data
public class Message implements Serializable {
    private String type;
    private CQData data;
}
