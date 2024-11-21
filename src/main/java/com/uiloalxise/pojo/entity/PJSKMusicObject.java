package com.uiloalxise.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Uiloalxise
 * @ClassName PJSKMusicObject
 * @Description PJSK音乐
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PJSKMusicObject {

    private Integer id;
    private String title;
    private String label;
    private String editor;
    private String composer;
    private String lyrics;
    private LocalDateTime openingTime;
    private String likeTitle;
    /**
     * https://storage.sekai.best/sekai-music-charts/jp/{{id}}/{{diff}}.png
     * EZ6C208ULC从一开始就拥有|
     * N12C484ULC从一开始就拥有|
     * H17C526ULC从一开始就拥有|
     * EX23C918ULC从一开始就拥有|
     * MA26C996ULC以7个以内的GOOD以下判定通关EXPERT|
     * APD0C0ULCNULL
     */
    private String LevelMessage;


}
