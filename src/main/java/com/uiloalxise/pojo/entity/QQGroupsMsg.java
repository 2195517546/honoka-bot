package com.uiloalxise.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQGroupsMsg
 * @Description QQ消息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQGroupsMsg implements Serializable {
    private String content;
    private Integer msgType;
    private Object markdown;
    private Object keyboard;
    private Object media;
    private Object ark;
    private Object messageReference;
    private String eventId;
    private String msgId;
    private Integer msgSeq;
}
