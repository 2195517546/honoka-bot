package com.uiloalxise.pojo.entity.duel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName DuelText
 * @Description 决斗文本用的实体类
 */
@Data
@AllArgsConstructor
@Builder
public class DuelText implements Serializable {
    private Long id;
    private String content;
}
