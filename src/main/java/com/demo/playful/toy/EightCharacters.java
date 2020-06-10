package com.demo.playful.toy;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Calendar;

/**
 * EightCharacters
 *
 * @author zhanghanlin6
 * @date 2020/6/9
 */
@Slf4j
public class EightCharacters {
    /**
     * 最小年
     */
    public static final int MIN_YEAR = 1900;
    /**
     * 最大年
     */
    public static final int MAX_YEAR = 2099;

    /**
     * 年-月天干对应表
     * key-年天干码
     * value-月天干对应的天干吗,数组索引表示农历月(下表从0开始,即0表示1月)
     */
    private static final ImmutableMap<Integer, Integer[]> YEAR_MONTH_HEAVENLY_STEM_MAPPING = ImmutableMap.<Integer, Integer[]>builder().
            put(1, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1}).
            put(6, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1}).
            put(2, new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3}).
            put(7, new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3}).
            put(3, new Integer[]{4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5}).
            put(8, new Integer[]{4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5}).
            put(4, new Integer[]{6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7}).
            put(9, new Integer[]{6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7}).
            put(0, new Integer[]{8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}).
            put(5, new Integer[]{8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}).
            build();

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
     * 月基数
     * 计算日天干使用
     */
    private static final int[] MONTH_BASE = new int[]{0, 31, -1, 30, 0, 31, 1, 32, 3, 33, 4, 34};

    /**
     * main函数
     *
     * @param args args
     */
    public static void main(String[] args) {
        int lunarMonth = 3;
        String eightCharacters = calculationEightCharacters("2017-04-14 11:44", lunarMonth);
        log.info("eightCharacters : {}", eightCharacters);
        lunarMonth = 11;
        eightCharacters = calculationEightCharacters("1990-12-26 11:44", lunarMonth);
        log.info("eightCharacters : {}", eightCharacters);
    }

    /**
     * 计算生辰八字
     *
     * @param birth      公历生日
     * @param lunarMonth 生日对应农历月
     */
    private static String calculationEightCharacters(String birth, int lunarMonth) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtils.parseDate(birth, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalArgumentException("birth convert Exception,birth:" + birth, e);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return calculationEightCharacters(year, month, day, hour, lunarMonth);
    }

    /**
     * 计算生辰八字
     *
     * @param year       公历年
     * @param month      公历月
     * @param day        公历日
     * @param hour       公历小时
     * @param lunarMonth 农历月
     */
    private static String calculationEightCharacters(int year, int month, int day, int hour, int lunarMonth) {
        if (MIN_YEAR > year || MAX_YEAR < year) {
            throw new IllegalArgumentException("year应在" + MIN_YEAR + "至" + MAX_YEAR + "之间");
        }
        HeavenlyStem yearHeavenlyStem = getYearHeavenlyStem(year);
        Earthly yearEarthly = getYearEarthly(year);
        HeavenlyStem monthHeavenlyStem = getMonthHeavenlyStem(lunarMonth, yearHeavenlyStem);
        Earthly monthEarthly = getMonthEarthly(lunarMonth);
        HeavenlyStem dayHeavenlyStem = getDayHeavenlyStem(year, month, day);
        Earthly dayEarthly = getDayEarthly(year, month, day);
        HeavenlyStem hourHeavenlyStem = getTimeHeavenlyStem(dayHeavenlyStem);
        Earthly hourEarthly = getTimeEarthly(hour);
        return yearHeavenlyStem.getName() + yearEarthly.getName() +
                monthHeavenlyStem.getName() + monthEarthly.getName() +
                dayHeavenlyStem.getName() + dayEarthly.getName() +
                hourHeavenlyStem.getName() + hourEarthly.getName();
    }

    /**
     * 获取年-天干
     *
     * @param year 年
     * @return 年天干
     */
    private static HeavenlyStem getYearHeavenlyStem(int year) {
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStem(year % 10);
        if (null == heavenlyStem) {
            throw new RuntimeException("yearHeavenlyStem is null , year:" + year);
        }
        return heavenlyStem;
    }

    /**
     * 获取年-地支
     *
     * @param year 年
     * @return 年地支
     */
    private static Earthly getYearEarthly(int year) {
        Earthly earthly = Earthly.getEarthly(year % 12);
        if (null == earthly) {
            throw new RuntimeException("yearEarthly is null , year:" + year);
        }
        return earthly;
    }

    /**
     * 获取月-天干
     *
     * @param lunarMonth       农历月
     * @param yearHeavenlyStem 年天干
     * @return 月天干
     */
    private static HeavenlyStem getMonthHeavenlyStem(int lunarMonth, HeavenlyStem yearHeavenlyStem) {
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStem(YEAR_MONTH_HEAVENLY_STEM_MAPPING.get(yearHeavenlyStem.getCode())[lunarMonth - 1]);
        if (null == heavenlyStem) {
            throw new RuntimeException("monthHeavenlyStem is null , lunarMonth:" + lunarMonth + ",yearHeavenlyStem:" + yearHeavenlyStem);
        }
        return heavenlyStem;
    }

    /**
     * 获取月-地支
     *
     * @param lunarMonth 农历月
     * @return 月地支
     */
    private static Earthly getMonthEarthly(int lunarMonth) {
        Earthly earthly = Earthly.getEarthly((lunarMonth + 5) % 12);
        if (null == earthly) {
            throw new RuntimeException("monthEarthly is null , lunarMonth:" + lunarMonth);
        }
        return earthly;
    }

    /**
     * 获取日-天干
     *
     * @param year  年
     * @param month 公历月
     * @param day   公历日
     * @return 日天干
     */
    private static HeavenlyStem getDayHeavenlyStem(int year, int month, int day) {
        int dayHeavenlyStemBaseCode = ((year % 100 - 1) / 4) * 6 + (((year % 100 - 1) / 4) * 3 + (year % 100 - 1) % 4) * 5 + MONTH_BASE[month - 1] + day + (year > 2000 ? 0 : 15);
        boolean leapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        if (leapYear) {
            dayHeavenlyStemBaseCode += 1;
        }
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStemByOrdinal((dayHeavenlyStemBaseCode % 60) % 10);
        if (null == heavenlyStem) {
            throw new RuntimeException("dayHeavenlyStem is null , year:" + year + ",month:" + month + ",day:" + day);
        }
        return heavenlyStem;
    }

    /**
     * 获取日-地支
     *
     * @param year  年
     * @param month 公历月
     * @param day   公历日
     * @return 日地支
     */
    private static Earthly getDayEarthly(int year, int month, int day) {
        int dayHeavenlyStemBaseCode = ((year % 100 - 1) / 4) * 6 + (((year % 100 - 1) / 4) * 3 + (year % 100 - 1) % 4) * 5 + MONTH_BASE[month - 1] + day + (year > 2000 ? 0 : 15);
        boolean leapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        if (leapYear) {
            dayHeavenlyStemBaseCode += 1;
        }
        Earthly earthly = Earthly.getEarthlyByOrdinal((dayHeavenlyStemBaseCode % 60) % 12);
        if (null == earthly) {
            throw new RuntimeException("dayEarthly is null , year:" + year + ",month:" + month + ",day:" + day);
        }
        return earthly;
    }

    /**
     * 获取时辰-天干
     *
     * @param dayHeavenlyStem 日天干
     * @return 时辰天干
     */
    private static HeavenlyStem getTimeHeavenlyStem(HeavenlyStem dayHeavenlyStem) {
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStemByOrdinal(dayHeavenlyStem.getCode() * 2 - 1);
        if (null == heavenlyStem) {
            throw new RuntimeException("timeHeavenlyStem is null , dayHeavenlyStem:" + dayHeavenlyStem);
        }
        return heavenlyStem;
    }

    /**
     * 获取时辰-地支
     *
     * @param hour 小时
     * @return 时辰地支
     */
    private static Earthly getTimeEarthly(int hour) {
        Earthly earthly = TIME_EARTHLY_MAPPING.get(hour);
        if (null == earthly) {
            throw new RuntimeException("timeEarthly is null , hour:" + hour);
        }
        return earthly;
    }

    @Getter
    @AllArgsConstructor
    enum HeavenlyStem {
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
         * 根据code得到天干对象
         *
         * @param code code
         * @return 天干对象
         */
        public static HeavenlyStem getHeavenlyStem(int code) {
            for (HeavenlyStem heavenlyStem : HeavenlyStem.values()) {
                if (code == heavenlyStem.getCode()) {
                    return heavenlyStem;
                }
            }
            return HeavenlyStem.JIA;
        }

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
    }

    @Getter
    @AllArgsConstructor
    enum Earthly {
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
         * 根据code得到地支对象
         *
         * @param code code
         * @return 地支对象
         */
        public static Earthly getEarthly(int code) {
            for (Earthly earthly : Earthly.values()) {
                if (code == earthly.getCode()) {
                    return earthly;
                }
            }
            return null;
        }

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
    }
}