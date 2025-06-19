package com.uiloalxise.honoka.mapper.duel;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Uiloalxise
 * @ClassName DuelTextMapper
 * @Description 决斗文本mapper
 */
@Mapper
public interface DuelTextMapper {
    @Select("SELECT content FROM duel_text ORDER BY RAND() LIMIT 1")
    String selectRandomContentOne();
}
