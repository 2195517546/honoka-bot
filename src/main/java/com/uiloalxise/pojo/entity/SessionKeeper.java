package com.uiloalxise.pojo.entity;

import jakarta.websocket.Session;
import lombok.Data;

/**
 * @author Uiloalxise
 * @ClassName SessionKeeper
 * @Description websocket用的户会话对象储存实体类
 */
@Data
public class SessionKeeper {
    private Session session;
}
