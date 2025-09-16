package com.uiloalxise.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Uiloalxise
 * @ClassName DuelResultDTO
 * @Description TODO
 */
@Data
@Builder
@AllArgsConstructor
public class DuelResultDTO{
    private Long id;
    private String openId;
    private Integer win;
    private Integer lost;
    private String nickname;
}
