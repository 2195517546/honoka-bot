package com.uiloalxise.honoka.service;

import com.uiloalxise.pojo.entity.PJSKMusicObject;

/**
 * @author Uiloalxise
 * @ClassName PictureExecuterService
 * @Description 自定义图片生成或获取服务
 */
public interface PictureExecutorService {

    /**
     * todo
     * 未来计划添加，用于获取脸圆云的生成图片并且返回
     * @param object
     * @return
     */
    byte[] pjskMusicExecute(PJSKMusicObject object);
}
