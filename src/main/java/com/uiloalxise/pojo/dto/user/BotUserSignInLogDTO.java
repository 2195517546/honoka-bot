package com.uiloalxise.pojo.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Uiloalxise
 * @ClassName BotUserSignInLogDTO
 * @Description TODO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotUserSignInLogDTO {
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
