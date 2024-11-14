package com.uiloalxise.utils;

import com.uiloalxise.constants.RegexConstant;
import com.uiloalxise.constants.StringConstant;
import com.uiloalxise.pojo.entity.PJSKMusicObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Uiloalxise
 * @ClassName PJSKUtil
 * @Description TODO
 */
@Data
@AllArgsConstructor
@Slf4j
public class PJSKUtil {

    private final static String Url = "https://www.faceroundcloud.site/static/pjsk/music_pane/";

    /**
     * 获取difficult全称
     * @param difficult
     * @return
     */
    public String getDifficult(String difficult)
    {
        difficult = difficult.toLowerCase();
        String result = "";
        if (difficult.contains("ez")||difficult.contains("0"))
        {
            result += "easy";
        }
        else
        if ((difficult.charAt(0) == 'n')||difficult.contains("1"))
        {
            result += "normal";
        }
        else
        if (difficult.contains("h")||difficult.contains("2"))
        {
            result += "hard";
        }
        else
        if (difficult.contains("ex")||difficult.contains("3"))
        {
            result += "expert";
        }
        else
        if (difficult.contains("ma")||difficult.contains("4"))
        {
            result += "master";
        }
        else
        if (difficult.contains("apd"))
        {
            result += "append";
        }

        if (result.isEmpty())
        {
            result = difficult;
        }

        return result;
    }

    /**
     * 获取铺面的图片url
     * @return
     */
    public String getPaneUrl(PJSKMusicObject obj)
    {
        String result = Url + String.format("%03d", obj.getId());

        return result + StringConstant.dPng;
    }

    /**
     * 获取铺面的图片url
     * @param difficult
     * @return
     */
    public String getDifficultUrl(PJSKMusicObject obj, String difficult)
    {
        difficult = difficult.toLowerCase();
        String result = Url + obj.getId();

        result += getDifficult(difficult);

        return result + StringConstant.dPng;
    }

    public String PJSKMusicLevelCutter(PJSKMusicObject obj)
    {
        String[]args = obj.getLevelMessage().split("\\|");
        StringBuilder result = new StringBuilder();
        String regex = null;

        for (int i = 0; i < 5; i++) {


            if(i == 0)regex=RegexConstant.EASY_REGEX;
            if(i == 1)regex=RegexConstant.NORMAL_REGEX;
            if(i == 2)regex=RegexConstant.HARD_REGEX;
            if(i == 3)regex=RegexConstant.EXPERT_REGEX;
            if(i == 4)regex=RegexConstant.MASTER_REGEX;




            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(args[i]);
            if (matcher.find()) {
                result.append(getDifficult(String.valueOf(i)) + "难度【" +  matcher.group(1)
                    +"】note数【" + matcher.group(2)
                    +"】获取方式【" + matcher.group(3) + "|" + matcher.group(4)
                    +"】\n");
            }

        }

        if (!args[5].contains("APDC"))
        {
            regex=RegexConstant.APPEND_REGEX;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(args[5]);
            if (matcher.find()) {
                result.append("append难度【" + matcher.group(1)
                        + "】note数【" + matcher.group(2)
                        + "】获取方式【" + matcher.group(3) + "|" + matcher.group(4)
                        + "】\n");
            }
        }

        return result.toString();
    }
}
