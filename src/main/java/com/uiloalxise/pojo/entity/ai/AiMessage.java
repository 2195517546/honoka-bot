package com.uiloalxise.pojo.entity.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Uiloalxise
 * @ClassName AiMessage
 * @Description TODO
 */
@Data
@AllArgsConstructor
@Builder
public class AiMessage implements Serializable {
    private String role;
    private String content;
}
