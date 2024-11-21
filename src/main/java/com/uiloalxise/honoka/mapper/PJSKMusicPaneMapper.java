package com.uiloalxise.honoka.mapper;


import com.uiloalxise.pojo.entity.PJSKMusicObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Uiloalxise
 * @ClassName PJSKMusicPaneMapper
 * @Description PJSK查铺mapper
 */
@Mapper
public interface PJSKMusicPaneMapper {

    @Select("select * from xiaoyi_app.pjsk_music_pane where id = #{id}")
    PJSKMusicObject selectByPrimaryKey(Integer id);

    @Select("select * from xiaoyi_app.pjsk_music_pane where pjsk_music_pane.title like #{title} or like_title like #{title} or SOUNDEX(title) like SOUNDEX(#{title})")
    List<PJSKMusicObject> selectByTitle(String title);

    @Select("select * from xiaoyi_app.pjsk_music_pane  where like_title like #{title} order by id DESC")
    List<PJSKMusicObject> selectByLikeTitle(String title);


}
