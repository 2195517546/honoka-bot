package com.uiloalxise.honoka.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uiloalxise.pojo.entity.user.BotUserPoints;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Uiloalxise
 * @ClassName BotUserPointsMapper
 * @Description 机器人用户积分(金币)库的mapper
 */
@Mapper
public interface BotUserPointsMapper extends BaseMapper<BotUserPoints> {
}
