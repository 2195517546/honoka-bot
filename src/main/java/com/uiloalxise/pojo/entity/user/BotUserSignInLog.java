package com.uiloalxise.pojo.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Uiloalxise
 * @ClassName BotUserSignInLog
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotUserSignInLog {
    private Long id;
    private Long userId;
    private String openId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;
    private BigDecimal rewardValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String comment;
}
