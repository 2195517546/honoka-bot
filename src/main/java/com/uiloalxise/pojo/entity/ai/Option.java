package com.uiloalxise.pojo.entity.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Uiloalxise
 * @ClassName Option
 * @Description TODO
 */
@Data
@AllArgsConstructor
@Builder
public class Option implements Serializable {
    BigDecimal temperature;
    Integer num_predict;
    Integer num_ctx;

    public  Option()
    {
        temperature = BigDecimal.valueOf(0.7);
        num_predict = 512;
        num_ctx= 2048;

    }
}
