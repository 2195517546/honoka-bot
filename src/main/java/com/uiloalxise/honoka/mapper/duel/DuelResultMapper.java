package com.uiloalxise.honoka.mapper.duel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uiloalxise.pojo.dto.DuelResultDTO;
import com.uiloalxise.pojo.entity.duel.DuelResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Uiloalxise
 * @ClassName DuelResultMapper
 * @Description 决斗结果的Mapper
 */
@Mapper
public interface DuelResultMapper extends BaseMapper<DuelResult> {

    DuelResultDTO findDuelResultWithNicknameByOpenId(String openId);
    List<DuelResultDTO> findTop10ByWinOrder();

}
