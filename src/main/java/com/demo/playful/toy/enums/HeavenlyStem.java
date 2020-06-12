package com.demo.playful.toy.enums;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HeavenlyStem
 * 10天干
 *
 * @author 张涵林
 * @date 2020/6/11
 */
@Getter
@AllArgsConstructor
public enum HeavenlyStem {
    /**
     * 甲
     */
    JIA(4, "甲"),
    /**
     * 乙
     */
    YI(5, "乙"),
    /**
     * 丙
     */
    BING(6, "丙"),
    /**
     * 丁
     */
    DING(7, "丁"),
    /**
     * 戊
     */
    WU(8, "戊"),
    /**
     * 己
     */
    JI(9, "己"),
    /**
     * 庚
     */
    GENG(0, "庚"),
    /**
     * 辛
     */
    XIN(1, "辛"),
    /**
     * 壬
     */
    REN(2, "壬"),
    /**
     * 癸
     */
    GUI(3, "癸"),
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
     * 年-月天干对应表
     * key-年天干码
     * value-月天干对应的索引,数组索引表示农历月(下表从0开始,即0表示1月)
     */
    private static final ImmutableMap<Integer, Integer[]> YEAR_MONTH_HEAVENLY_STEM_MAPPING = ImmutableMap.<Integer, Integer[]>builder().
            put(1, new Integer[]{7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8}).
            put(6, new Integer[]{7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8}).
            put(2, new Integer[]{9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).
            put(7, new Integer[]{9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).
            put(3, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2}).
            put(8, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2}).
            put(4, new Integer[]{3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4}).
            put(9, new Integer[]{3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4}).
            put(0, new Integer[]{5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6}).
            put(5, new Integer[]{5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6}).
            build();

    /**
     * 根据ordinal得到天干对象
     *
     * @param ordinal ordinal
     * @return 天干对象
     */
    public static HeavenlyStem getHeavenlyStemByOrdinal(int ordinal) {
        for (HeavenlyStem heavenlyStem : HeavenlyStem.values()) {
            if (ordinal == heavenlyStem.ordinal() + 1) {
                return heavenlyStem;
            }
        }
        return HeavenlyStem.JIA;
    }

    /**
     * 获取年-天干
     *
     * @param lunarYear 农历年
     * @return 年天干
     */
    public static HeavenlyStem getYearHeavenlyStem(int lunarYear) {
        int yearRemainder = lunarYear % 10;
        HeavenlyStem heavenlyStem = getHeavenlyStemByOrdinal(yearRemainder > 3 ? yearRemainder - 3 : yearRemainder + 7);
        if (null == heavenlyStem) {
            throw new RuntimeException("yearHeavenlyStem is null , year:" + lunarYear);
        }
        return heavenlyStem;
    }

    /**
     * 获取月-天干
     *
     * @param lunarMonth       农历月
     * @param yearHeavenlyStem 年天干
     * @return 月天干
     */
    public static HeavenlyStem getMonthHeavenlyStem(int lunarMonth, HeavenlyStem yearHeavenlyStem) {
        HeavenlyStem heavenlyStem = getHeavenlyStemByOrdinal(YEAR_MONTH_HEAVENLY_STEM_MAPPING.get(yearHeavenlyStem.getCode())[lunarMonth]);
        if (null == heavenlyStem) {
            throw new RuntimeException("monthHeavenlyStem is null , lunarMonth:" + lunarMonth + ",yearHeavenlyStem:" + yearHeavenlyStem);
        }
        return heavenlyStem;
    }

    /**
     * 获取日-天干
     *
     * @param year  公历年
     * @param month 公历月
     * @param day   公历日
     * @return 日天干
     */
    public static HeavenlyStem getDayHeavenlyStem(int year, int month, int day) {
        year = month < 3 ? year - 1 : year;
        int c = year / 100;
        int y = year % 100;
        int m = month < 2 ? month + 13 : month + 1;
        int dayHeavenlyStemBaseCode = 4 * c + c / 4 + 5 * y + y / 4 + 3 * (m + 1) / 5 + day - 3;
        HeavenlyStem heavenlyStem = getHeavenlyStemByOrdinal(dayHeavenlyStemBaseCode % 10);
        if (null == heavenlyStem) {
            throw new RuntimeException("dayHeavenlyStem is null , year:" + year + ",month:" + month + ",day:" + day);
        }
        return heavenlyStem;
    }

    /**
     * 获取时辰-天干
     *
     * @param dayHeavenlyStem 日天干
     * @return 时辰天干
     */
    public static HeavenlyStem getHourHeavenlyStem(HeavenlyStem dayHeavenlyStem) {
        HeavenlyStem heavenlyStem = getHeavenlyStemByOrdinal(dayHeavenlyStem.getCode() * 2 - 1);
        if (null == heavenlyStem) {
            throw new RuntimeException("timeHeavenlyStem is null , dayHeavenlyStem:" + dayHeavenlyStem);
        }
        return heavenlyStem;
    }
}
