package com.uiloalxise.pojo.entity.duel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Uiloalxise
 * @ClassName DuelResult
 * @Description TODO
 */
@Data
@AllArgsConstructor
@Builder
public class DuelResult {

    private Long id;
    private String openId;
    private Long win;
    private Long lost;
}
