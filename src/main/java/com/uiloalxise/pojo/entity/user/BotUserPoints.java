package com.uiloalxise.pojo.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Uiloalxise
 * @ClassName BotUserPoints
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotUserPoints {
    private String openId;
    @TableId(type = IdType.AUTO)
    private Integer userId;

    private BigDecimal money;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
