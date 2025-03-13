package com.uiloalxise.honoka.service;


/**
 * @author Uiloalxise
 * @ClassName PictureServiceImpl
 * @Description 图片url服务
 */
public interface PictureService {

    /**
     * 返回一个随机的url
     * @param path 哪种图片
     * @return 图片的url
     */
    String getRandomPicture(String path);


}
