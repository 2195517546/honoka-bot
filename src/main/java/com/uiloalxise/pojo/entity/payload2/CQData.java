package com.uiloalxise.pojo.entity.payload2;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName CQData
 * @Description cq的数据结构
 */
@Data
@Builder
public class CQData implements Serializable {
    private String qq;
    private String text;
}
