package com.uiloalxise.honoka.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liumo
 * 测试mapper
 */
@Mapper
public interface TestMapper {

    /**
     *
     * @param name
     * @param age
     */
    @Insert("insert xiaoyi_app.test001 (name, age) values (#{name},#{age})")
    void insert(String name, int age);

}
