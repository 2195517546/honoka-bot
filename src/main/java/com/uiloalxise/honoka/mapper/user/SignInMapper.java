package com.uiloalxise.honoka.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uiloalxise.pojo.entity.user.BotUserSignInLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Uiloalxise
 * @ClassName SignInMapper
 * @Description 签到用的mapper
 */
@Mapper
public interface SignInMapper extends BaseMapper<BotUserSignInLog> {

}
