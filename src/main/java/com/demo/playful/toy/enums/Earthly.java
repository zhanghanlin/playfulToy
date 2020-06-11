package com.demo.playful.toy.enums;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Earthly
 * 12地支
 *
 * @author zhanghanlin6
 * @date 2020/6/11
 */
@Getter
@AllArgsConstructor
public enum Earthly {
    /**
     * 子
     */
    ZI(4, "子"),
    /**
     * 丑
     */
    CHOU(5, "丑"),
    /**
     * 寅
     */
    YIN(6, "寅"),
    /**
     * 卯
     */
    MOU(7, "卯"),
    /**
     * 辰
     */
    CHEN(8, "辰"),
    /**
     * 巳
     */
    SI(9, "巳"),
    /**
     * 午
     */
    WU(10, "午"),
    /**
     * 未
     */
    WEI(11, "未"),
    /**
     * 申
     */
    SHEN(12, "申"),
    /**
     * 酉
     */
    YOU(1, "酉"),
    /**
     * 戌
     */
    XU(2, "戌"),
    /**
     * 亥
     */
    HAI(3, "亥"),
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
     * 根据ordinal得到地支对象
     *
     * @param ordinal ordinal
     * @return 地支对象
     */
    public static Earthly getEarthlyByOrdinal(int ordinal) {
        for (Earthly earthly : Earthly.values()) {
            if (ordinal == earthly.ordinal() + 1) {
                return earthly;
            }
        }
        return null;
    }

    /**
     * 时辰对应表
     * key - 小时数字
     * value - 小时对应的时间地支
     */
    private static final ImmutableMap<Integer, Earthly> TIME_EARTHLY_MAPPING = ImmutableMap.<Integer, Earthly>builder().
            put(23, Earthly.ZI).put(0, Earthly.ZI).
            put(1, Earthly.CHOU).put(2, Earthly.CHOU).
            put(3, Earthly.YIN).put(4, Earthly.YIN).
            put(5, Earthly.MOU).put(6, Earthly.MOU).
            put(7, Earthly.CHEN).put(8, Earthly.CHEN).
            put(9, Earthly.SI).put(10, Earthly.SI).
            put(11, Earthly.WU).put(12, Earthly.WU).
            put(13, Earthly.WEI).put(14, Earthly.WEI).
            put(15, Earthly.SHEN).put(16, Earthly.SHEN).
            put(17, Earthly.YOU).put(18, Earthly.YOU).
            put(19, Earthly.XU).put(20, Earthly.XU).
            put(21, Earthly.HAI).put(22, Earthly.HAI).
            build();

    /**
     * 获取年-地支
     *
     * @param lunarYear 农历年
     * @return 年地支
     */
    public static Earthly getYearEarthly(int lunarYear) {
        int yearRemainder = lunarYear % 12;
        Earthly earthly = getEarthlyByOrdinal(yearRemainder > 3 ? yearRemainder - 3 : yearRemainder + 9);
        if (null == earthly) {
            throw new RuntimeException("yearEarthly is null , year:" + lunarYear);
        }
        return earthly;
    }

    /**
     * 获取月-地支
     *
     * @param lunarMonth 农历月
     * @return 月地支
     */
    public static Earthly getMonthEarthly(int lunarMonth) {
        Earthly earthly = getEarthlyByOrdinal((lunarMonth + 2) % 12);
        if (null == earthly) {
            throw new RuntimeException("monthEarthly is null , lunarMonth:" + lunarMonth);
        }
        return earthly;
    }

    /**
     * 获取日-地支
     *
     * @param year  公历年
     * @param month 公历月
     * @param day   公历日
     * @return 日地支
     */
    public static Earthly getDayEarthly(int year, int month, int day) {
        year = month < 3 ? year - 1 : year;
        int c = year > 2000 ? 20 : 19;
        int y = year % 100;
        int m = month < 3 ? month + 12 : month;
        int dayHeavenlyStemBaseCode = 8 * c + c / 4 + 5 * y + y / 4 + 3 * (m + 1) / 5 + day + 7 + (m % 2 == 0 ? 6 : 0);
        Earthly earthly = getEarthlyByOrdinal(dayHeavenlyStemBaseCode % 12);
        if (null == earthly) {
            throw new RuntimeException("dayEarthly is null , year:" + year + ",month:" + month + ",day:" + day);
        }
        return earthly;
    }

    /**
     * 获取时辰-地支
     *
     * @param hour 小时
     * @return 时辰地支
     */
    public static Earthly getTimeEarthly(int hour) {
        Earthly earthly = TIME_EARTHLY_MAPPING.get(hour);
        if (null == earthly) {
            throw new RuntimeException("timeEarthly is null , hour:" + hour);
        }
        return earthly;
    }
}
