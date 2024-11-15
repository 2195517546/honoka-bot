package com.uiloalxise.constants;

/**
 * @author Uiloalxise
 * @ClassName RegexConstant
 * @Description 正则表达式常数
 */
public class RegexConstant {
    /**
     * 图片Regex
     */
    public final static String PICTURE_REGEX = "^(.*?)(\\.jpg|\\.png|\\.gif)";

    public final static String MUSIC_PANE_REGEX = "(谱面|pjskp)([\\u2E80-\\uFE4F]+)(?i)(ez|n|h|ex|ma|apd)";
    public final static String MUSIC_REGEX = "(歌曲|pjskm)([\\u2E80-\\uFE4F]+)";


    /**
     *
     EZ7C166ULC最初から所持CN从一开始就拥有|
     N11C296ULC最初から所持CN从一开始就拥有|
     H17C635ULC最初から所持CN从一开始就拥有|
     EX24C827ULC最初から所持CN从一开始就拥有|
     MA29C975ULCGOOD以下を7以内でEXPERTクリアで解放CN以7个以内的GOOD以下判定通关EXPERT|
     APDCULCCN
     */
    public final static String EASY_REGEX = "EZ(\\d+)C(\\d+)ULC(.*)CN(.*)";
    public final static String NORMAL_REGEX = "N(\\d+)C(\\d+)ULC(.*)CN(.*)";
    public final static String HARD_REGEX = "H(\\d+)C(\\d+)ULC(.*)CN(.*)";
    public final static String EXPERT_REGEX = "EX(\\d+)C(\\d+)ULC(.*)CN(.*)";
    public final static String MASTER_REGEX = "MA(\\d+)C(\\d+)ULC(.*)CN(.*)";
    public final static String APPEND_REGEX = "APD(\\d+)C(\\d+)ULC(.*)CN(.*)";


}
