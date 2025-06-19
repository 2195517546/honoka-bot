package com.uiloalxise.honoka.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uiloalxise.pojo.entity.user.BotUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

/**
 * @author Uiloalxise
 * @ClassName BotUserMapper
 * @Description 机器人用户mapper
 */
@Mapper
public interface BotUserMapper extends BaseMapper<BotUser> {
    // 使用注解定义 selectByOpenIdForUpdate 方法
    @Select("SELECT * FROM bot_user WHERE open_id = #{openId} FOR UPDATE")
    BotUser selectByOpenIdForUpdate(@Param("openId") String openId);

    // 可选：定义 selectByOpenId 方法
    @Select("SELECT * FROM bot_user WHERE open_id = #{openId} LIMIT 1")
    BotUser selectByOpenId(@Param("openId") String openId);
}
