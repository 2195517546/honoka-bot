-- 所有建表语句如下
create table pjsk_music_pane
(
    id            int auto_increment
        primary key,
    title         varchar(100) null comment '标题',
    label         varchar(100) null comment '标签',
    editor        varchar(100) null comment '编曲',
    composer      varchar(100) null comment '作曲',
    lyrics        varchar(100) null comment '作词',
    opening_time  timestamp    null comment '开放时间',
    level_message varchar(250) null comment '等级信息:E6C208ULC从一开始就拥有',
    like_title    varchar(100) null comment '模糊查询'
)
    comment '世界计划铺面';
-- 一条level_message格式如右边类似于EZ7C142ULC最初から所持CN从一开始就拥有|N14C343ULC最初から所持CN从一开始就拥有|H19C600ULC最初から所持CN从一开始就拥有|EX26C947ULC最初から所持CN从一开始就拥有|MA33C1157ULCGOOD以下を7以内でEXPERTクリアで解放CN以7个以内的GOOD以下判定通关EXPERT|APDCULCCN
