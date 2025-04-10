package com.uiloalxise.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Uiloalxise
 * @ClassName MapperResult
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapperResult {
    public final static Integer ERROR = -1;
    public final static Integer SUCCESS = 1;
    //记录操作结果
    private Integer count;
    //经过类型是否为错误
    private Integer resultType;
    private String message;
}
