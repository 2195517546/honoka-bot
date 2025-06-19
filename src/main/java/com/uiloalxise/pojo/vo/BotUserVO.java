package com.uiloalxise.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Uiloalxise
 * @ClassName BotUserVO
 * @Description 机器人用户VO
 */
@Getter
@Setter
@Builder
public class BotUserVO implements Serializable {

    private String openId;

    private Integer userId;

    private String nickname;

    private BigDecimal money;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Boolean isSignToday;

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String formattedDate = createTime != null ? dateFormat.format(createTime) : "未知时间";

        return  "\n昵称:" + nickname + "\n" +
                "积分余额:" + money + "\n" +
                "今日是否签到:" + (isSignToday != null && isSignToday ? "是" : "否") + "\n" +
                "用户创建时间:" + formattedDate;

    }
}
