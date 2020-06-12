package com.demo.playful.toy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FiveElementsEnum
 * 五行
 *
 * @author 张涵林
 * @date 2020/6/12
 */
@Getter
@AllArgsConstructor
public enum FiveElements {
    /**
     * 金
     */
    JIN(1, "金"),
    /**
     * 木
     */
    MU(2, "木"),
    /**
     * 水
     */
    SHUI(3, "水"),
    /**
     * 火
     */
    HUO(4, "火"),
    /**
     * 土
     */
    TU(5, "土"),
    ;
    /**
     * 编码
     */
    private final int code;
    /**
     * 名称
     */
    private final String name;

    /**
     * 根据天干对象得到对应五行
     *
     * @param heavenlyStem heavenlyStem天干
     * @return 五行对象
     */
    public static FiveElements getFiveElementsByHeavenlyStem(HeavenlyStem heavenlyStem) {
        if (heavenlyStem.equals(HeavenlyStem.JIA) || heavenlyStem.equals(HeavenlyStem.YI)) {
            return FiveElements.MU;
        } else if (heavenlyStem.equals(HeavenlyStem.BING) || heavenlyStem.equals(HeavenlyStem.DING)) {
            return FiveElements.HUO;
        } else if (heavenlyStem.equals(HeavenlyStem.WU) || heavenlyStem.equals(HeavenlyStem.JI)) {
            return FiveElements.TU;
        } else if (heavenlyStem.equals(HeavenlyStem.GENG) || heavenlyStem.equals(HeavenlyStem.XIN)) {
            return FiveElements.JIN;
        }
        return FiveElements.SHUI;
    }

    /**
     * 根据地支对象得到对应五行
     *
     * @param earthly earthly地支
     * @return 五行对象
     */
    public static FiveElements getFiveElementsByEarthly(Earthly earthly) {
        if (earthly.equals(Earthly.YIN) || earthly.equals(Earthly.MOU)) {
            return FiveElements.MU;
        } else if (earthly.equals(Earthly.SI) || earthly.equals(Earthly.WU)) {
            return FiveElements.HUO;
        } else if (earthly.equals(Earthly.CHEN) || earthly.equals(Earthly.CHOU)) {
            return FiveElements.TU;
        } else if (earthly.equals(Earthly.XU) || earthly.equals(Earthly.WEI)) {
            return FiveElements.TU;
        } else if (earthly.equals(Earthly.SHEN) || earthly.equals(Earthly.YOU)) {
            return FiveElements.JIN;
        }
        return FiveElements.SHUI;
    }
}
