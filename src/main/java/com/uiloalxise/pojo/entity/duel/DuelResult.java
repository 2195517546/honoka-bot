package com.uiloalxise.pojo.entity.duel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openId;
    private Long win;
    private Long lost;
}
