package com.uiloalxise.honoka.mapper;

import com.uiloalxise.pojo.entity.commands.GroupMsgCommand;
import org.apache.ibatis.annotations.Mapper;
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

    void insertUsersOpenId(Set<String> usersOpenIds);

    void registerAllOpenId(List<String> usersOpenIds);

    @Select("select qq_users.open_id from qq_users where status = 0")
    List<String> unregisterOpenIds();

    @Update("update bot_user set status = 1 where open_id = #{openId}")
    void registerOpenId(String openId);
}
