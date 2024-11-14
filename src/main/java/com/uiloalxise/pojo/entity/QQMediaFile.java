package com.uiloalxise.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName QQMediaFile
 * @Description TODO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQMediaFile implements Serializable {
    private Integer fileType;
    private String url;
    private Boolean srvSendMsg;
}
