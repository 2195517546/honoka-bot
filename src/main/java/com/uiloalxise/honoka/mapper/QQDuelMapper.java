package com.uiloalxise.honoka.mapper;

import com.uiloalxise.pojo.dto.DuelPlayerDTO;
import com.uiloalxise.pojo.entity.DuelPlayer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Uiloalxise
 * @ClassName QQDuelMapper
 * @Description qq决斗mapper
 */
@Mapper
public interface QQDuelMapper {

    /**
     * openid找用户
     * @param duelPlayerDTO
     * @return
     */
    DuelPlayer selectDuelPlayerByOpenId(DuelPlayerDTO duelPlayerDTO);

    void addDuelPlayer(DuelPlayerDTO duelPlayerDTO);

    List<DuelPlayer> rankDuelPlayerByKd();

    void updateDuelPlayerOnlyNameByOpenId(DuelPlayerDTO duelPlayerDTO);

    void updateDuelPlayerByOpenIdExceptName(DuelPlayerDTO duelPlayerDTO);

    void processAll();
}
