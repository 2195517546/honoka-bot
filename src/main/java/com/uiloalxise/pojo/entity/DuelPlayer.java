package com.uiloalxise.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Uiloalxise
 * @ClassName DuelPlayer
 * @Description TODO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuelPlayer {

    /*
    -- auto-generated definition
    create table duel_player
    (
        open_id   varchar(128) default ''         not null
            primary key,
        money     bigint       default 100        null,
        win       int          default 0          null,
        lost      int          default 0          null,
        user_name varchar(50)  default 'new_user' not null
    )
        comment '决斗游戏表';
     */
    private String openId;
    private Long money;
    private Integer win;
    private Integer lost;
    private String userName;

}
