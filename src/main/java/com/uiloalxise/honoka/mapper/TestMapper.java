package com.uiloalxise.honoka.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liumo
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

    /**
     *
     */
    List<String> selectAll();
}
