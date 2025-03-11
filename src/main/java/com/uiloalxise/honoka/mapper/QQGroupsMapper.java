package com.uiloalxise.honoka.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @author Uiloalxise
 * @ClassName QQGroupsMapper
 * @Description TODO
 */
@Mapper
public interface QQGroupsMapper {

    void insertGroupsOpenId(Set<String> groupsOpenIds);
}
