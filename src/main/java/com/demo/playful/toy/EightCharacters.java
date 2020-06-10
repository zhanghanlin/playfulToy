package com.demo.playful.toy;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.*;

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
     * 日期格式化-生日
     */
    private static final String DATE_FORMAT_BIRTH = "yyyy-MM-dd HH";

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
    public static void main(String[] args) throws ParseException {
        List<String> paramList = Arrays.asList("2017-04-14 11", "1990-12-26 11", "1992-01-20 11");
        for (String birth : paramList) {
            Map<String, Map<HeavenlyStem, Earthly>> eightCharacters = calculationEightCharacters(birth);
            print(birth, eightCharacters);
        }
    }

    /**
     * 打印方法
     *
     * @param birth 生日
     * @param map   八字对象
     */
    private static void print(String birth, Map<String, Map<HeavenlyStem, Earthly>> map) throws ParseException {
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder codeBuilder = new StringBuilder();
        for (Map.Entry<String, Map<HeavenlyStem, Earthly>> entry : map.entrySet()) {
            codeBuilder.append(entry.getKey());
            for (Map.Entry<HeavenlyStem, Earthly> heEntry : entry.getValue().entrySet()) {
                nameBuilder.append(heEntry.getKey().getName()).append(heEntry.getValue().getName());
                codeBuilder.append(heEntry.getKey().getCode()).append("-").append(heEntry.getValue().getCode());
            }
            codeBuilder.append(",");
            nameBuilder.append(entry.getKey());
        }
        Date birthDate = DateUtils.parseDate(birth, DATE_FORMAT_BIRTH);
        log.info("{}生", DateFormatUtils.format(birthDate, "yyyy年MM月dd日HH时"));
        log.info("生辰八字:{}, {}", nameBuilder.toString(), codeBuilder.toString());
        log.info("=========================================================");
    }

    /**
     * 计算生辰八字
     *
     * @param birth 公历生日
     */
    private static Map<String, Map<HeavenlyStem, Earthly>> calculationEightCharacters(String birth) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtils.parseDate(birth, DATE_FORMAT_BIRTH));
        } catch (ParseException e) {
            throw new IllegalArgumentException("birth convert Exception,birth:" + birth, e);
        }
        return calculationEightCharacters(calendar);
    }

    /**
     * 计算生辰八字
     *
     * @param birthCal 生日对象
     */
    private static Map<String, Map<HeavenlyStem, Earthly>> calculationEightCharacters(Calendar birthCal) {
        Calendar lunarCal = DateUtils.LunarDate.solarToLunar(birthCal);
        int year = birthCal.get(Calendar.YEAR);
        int month = birthCal.get(Calendar.MONTH) + 1;
        int day = birthCal.get(Calendar.DAY_OF_MONTH);
        int hour = birthCal.get(Calendar.HOUR_OF_DAY);
        int lunarYear = lunarCal.get(Calendar.YEAR);
        int lunarMonth = lunarCal.get(Calendar.MONTH) + 1;
        HeavenlyStem yearHeavenlyStem = getYearHeavenlyStem(lunarYear);
        HeavenlyStem dayHeavenlyStem = getDayHeavenlyStem(year, month, day);
        Map<String, Map<HeavenlyStem, Earthly>> map = Maps.newLinkedHashMap();
        map.put("年", ImmutableMap.of(yearHeavenlyStem, getYearEarthly(lunarYear)));
        map.put("月", ImmutableMap.of(getMonthHeavenlyStem(lunarMonth, yearHeavenlyStem), getMonthEarthly(lunarMonth)));
        map.put("日", ImmutableMap.of(dayHeavenlyStem, getDayEarthly(year, month, day)));
        map.put("时", ImmutableMap.of(getTimeHeavenlyStem(dayHeavenlyStem), getTimeEarthly(hour)));
        return map;
    }

    /**
     * 获取年-天干
     *
     * @param lunarYear 农历年
     * @return 年天干
     */
    private static HeavenlyStem getYearHeavenlyStem(int lunarYear) {
        int yearRemainder = lunarYear % 10;
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStemByOrdinal(yearRemainder > 3 ? yearRemainder - 3 : yearRemainder + 7);
        if (null == heavenlyStem) {
            throw new RuntimeException("yearHeavenlyStem is null , year:" + lunarYear);
        }
        return heavenlyStem;
    }

    /**
     * 获取年-地支
     *
     * @param lunarYear 农历年
     * @return 年地支
     */
    private static Earthly getYearEarthly(int lunarYear) {
        int yearRemainder = lunarYear % 12;
        Earthly earthly = Earthly.getEarthlyByOrdinal(yearRemainder > 3 ? yearRemainder - 3 : yearRemainder + 9);
        if (null == earthly) {
            throw new RuntimeException("yearEarthly is null , year:" + lunarYear);
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
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStemByOrdinal(YEAR_MONTH_HEAVENLY_STEM_MAPPING.get(yearHeavenlyStem.getCode())[lunarMonth - 1]);
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
        Earthly earthly = Earthly.getEarthlyByOrdinal((lunarMonth + 2) % 12);
        if (null == earthly) {
            throw new RuntimeException("monthEarthly is null , lunarMonth:" + lunarMonth);
        }
        return earthly;
    }

    /**
     * 获取日-天干
     *
     * @param year  公历年
     * @param month 公历月
     * @param day   公历日
     * @return 日天干
     */
    private static HeavenlyStem getDayHeavenlyStem(int year, int month, int day) {
        year = month < 3 ? year - 1 : year;
        int c = year / 100;
        int y = year % 100;
        int m = month < 3 ? month + 12 : month;
        int dayHeavenlyStemBaseCode = 4 * c + c / 4 + 5 * y + y / 4 + 3 * (m + 1) / 5 + day - 3;
        HeavenlyStem heavenlyStem = HeavenlyStem.getHeavenlyStemByOrdinal(dayHeavenlyStemBaseCode % 10);
        if (null == heavenlyStem) {
            throw new RuntimeException("dayHeavenlyStem is null , year:" + year + ",month:" + month + ",day:" + day);
        }
        return heavenlyStem;
    }

    /**
     * 获取日-地支
     *
     * @param year  公历年
     * @param month 公历月
     * @param day   公历日
     * @return 日地支
     */
    private static Earthly getDayEarthly(int year, int month, int day) {
        year = month < 3 ? year - 1 : year;
        int c = year > 2000 ? 20 : 19;
        int y = year % 100;
        int m = month < 3 ? month + 12 : month;
        int dayHeavenlyStemBaseCode = 8 * c + c / 4 + 5 * y + y / 4 + 3 * (m + 1) / 5 + day + 7 + (m % 2 == 0 ? 6 : 0);
        Earthly earthly = Earthly.getEarthlyByOrdinal(dayHeavenlyStemBaseCode % 12);
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