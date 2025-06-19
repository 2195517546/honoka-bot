package com.uiloalxise.honoka.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName QQGroupsMapper
 * @Description qq群消息的mapper
 */
@Mapper
public interface QQGroupsMapper{

    void insertGroupsOpenId(Set<String> groupsOpenIds);

    @Select("select qq_groups.groups_open_id from qq_groups")
    List<String> groupsOpenId();
}
