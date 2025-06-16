package com.uiloalxise.honoka.mapper;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName QQUserMapper.xml
 * @Description TODO
 */
@Mapper
public interface QQUserMapper {

    void insertUsersOpenId(@Param("openIds") List<String> openIds);

    void registerAllOpenId(@Param("usersOpenIds")List<String> usersOpenIds);

    @Select("select qq_users.open_id from qq_users where status = 0")
    List<String> unregisterOpenIds();

    @Update("update qq_users set status = 1 where open_id = #{openId}")
    void registerOpenId(String openId);
}
