package com.demo.playful.toy.utils;

import com.demo.playful.toy.enums.Earthly;
import com.demo.playful.toy.enums.HeavenlyStem;
import com.google.common.collect.ImmutableMap;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * DateUtils
 * 日期工具类
 *
 * @author 张涵林
 * @date 2020/6/9
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 字符串日期转换为Calendar
     *
     * @param dateStr 日期字符串
     * @param format  日期格式
     * @return Calendar日期对象
     */
    public static Calendar parseCalendar(String dateStr, String format) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtils.parseDate(dateStr, format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * 日期格式化
     */
    public interface Format {
        /**
         * yyyy-MM-dd HH
         */
        String Y_M_D_H = "yyyy-MM-dd HH";
    }

    /**
     * 公历日期服务
     */
    public static class LunarDate {
        /**
         * 支持转换的最小农历年份
         */
        public static final int MIN_YEAR = 1900;
        /**
         * 支持转换的最大农历年份
         */
        public static final int MAX_YEAR = 2099;
        /**
         * 农历月对应表
         * key - 农历月数字
         * value - 农历月名称
         */
        private static final ImmutableMap<Integer, String> LUNAR_MONTH_MAPPING = ImmutableMap.<Integer, String>builder().
                put(0, "正").put(1, "二").put(2, "三").put(3, "四").put(4, "五").put(5, "六").
                put(6, "七").put(7, "八").put(8, "九").put(9, "十").put(10, "冬").put(11, "腊").
                build();

        /**
         * 日期数字对应表
         * key - 数字
         * value - 名称
         */
        private static final ImmutableMap<Integer, String> DAY_NUM_MAPPING = ImmutableMap.<Integer, String>builder().
                put(1, "一").put(2, "二").put(3, "三").put(4, "四").put(5, "五").put(6, "六").
                put(7, "七").put(8, "八").put(9, "九").put(10, "十").put(0, "十").build();

        /**
         * 用来表示1900年到2099年间农历年份的相关信息，共24位bit的16进制表示，其中：
         * 前4位表示该年闰哪个月；
         * 5-17位表示农历年份13个月的大小月分布，0表示小，1表示大；
         * 最后7位表示农历年首（正月初一）对应的公历日期。
         * 以2014年的数据0x955ABF为例说明：
         * 1001 0101 0101 1010 1011 1111
         * 闰九月 农历正月初一对应公历1月31号
         */
        private static final int[] LUNAR_INFO = {
                /*1900*/
                0x84B6BF,
                /*1901-1910*/
                0x04AE53, 0x0A5748, 0x5526BD, 0x0D2650, 0x0D9544, 0x46AAB9, 0x056A4D, 0x09AD42, 0x24AEB6, 0x04AE4A,
                /*1911-1920*/
                0x6A4DBE, 0x0A4D52, 0x0D2546, 0x5D52BA, 0x0B544E, 0x0D6A43, 0x296D37, 0x095B4B, 0x749BC1, 0x049754,
                /*1921-1930*/
                0x0A4B48, 0x5B25BC, 0x06A550, 0x06D445, 0x4ADAB8, 0x02B64D, 0x095742, 0x2497B7, 0x04974A, 0x664B3E,
                /*1931-1940*/
                0x0D4A51, 0x0EA546, 0x56D4BA, 0x05AD4E, 0x02B644, 0x393738, 0x092E4B, 0x7C96BF, 0x0C9553, 0x0D4A48,
                /*1941-1950*/
                0x6DA53B, 0x0B554F, 0x056A45, 0x4AADB9, 0x025D4D, 0x092D42, 0x2C95B6, 0x0A954A, 0x7B4ABD, 0x06CA51,
                /*1951-1960*/
                0x0B5546, 0x555ABB, 0x04DA4E, 0x0A5B43, 0x352BB8, 0x052B4C, 0x8A953F, 0x0E9552, 0x06AA48, 0x6AD53C,
                /*1961-1970*/
                0x0AB54F, 0x04B645, 0x4A5739, 0x0A574D, 0x052642, 0x3E9335, 0x0D9549, 0x75AABE, 0x056A51, 0x096D46,
                /*1971-1980*/
                0x54AEBB, 0x04AD4F, 0x0A4D43, 0x4D26B7, 0x0D254B, 0x8D52BF, 0x0B5452, 0x0B6A47, 0x696D3C, 0x095B50,
                /*1981-1990*/
                0x049B45, 0x4A4BB9, 0x0A4B4D, 0xAB25C2, 0x06A554, 0x06D449, 0x6ADA3D, 0x0AB651, 0x095746, 0x5497BB,
                /*1991-2000*/
                0x04974F, 0x064B44, 0x36A537, 0x0EA54A, 0x86B2BF, 0x05AC53, 0x0AB647, 0x5936BC, 0x092E50, 0x0C9645,
                /*2001-2010*/
                0x4D4AB8, 0x0D4A4C, 0x0DA541, 0x25AAB6, 0x056A49, 0x7AADBD, 0x025D52, 0x092D47, 0x5C95BA, 0x0A954E,
                /*2011-2020*/
                0x0B4A43, 0x4B5537, 0x0AD54A, 0x955ABF, 0x04BA53, 0x0A5B48, 0x652BBC, 0x052B50, 0x0A9345, 0x474AB9,
                /*2021-2030*/
                0x06AA4C, 0x0AD541, 0x24DAB6, 0x04B64A, 0x6a573D, 0x0A4E51, 0x0D2646, 0x5E933A, 0x0D534D, 0x05AA43,
                /*2031-2040*/
                0x36B537, 0x096D4B, 0xB4AEBF, 0x04AD53, 0x0A4D48, 0x6D25BC, 0x0D254F, 0x0D5244, 0x5DAA38, 0x0B5A4C,
                /*2041-2050*/
                0x056D41, 0x24ADB6, 0x049B4A, 0x7A4BBE, 0x0A4B51, 0x0AA546, 0x5B52BA, 0x06D24E, 0x0ADA42, 0x355B37,
                /*2051-2060*/
                0x09374B, 0x8497C1, 0x049753, 0x064B48, 0x66A53C, 0x0EA54F, 0x06AA44, 0x4AB638, 0x0AAE4C, 0x092E42,
                /*2061-2070*/
                0x3C9735, 0x0C9649, 0x7D4ABD, 0x0D4A51, 0x0DA545, 0x55AABA, 0x056A4E, 0x0A6D43, 0x452EB7, 0x052D4B,
                /*2071-2080*/
                0x8A95BF, 0x0A9553, 0x0B4A47, 0x6B553B, 0x0AD54F, 0x055A45, 0x4A5D38, 0x0A5B4C, 0x052B42, 0x3A93B6,
                /*2081-2090*/
                0x069349, 0x7729BD, 0x06AA51, 0x0AD546, 0x54DABA, 0x04B64E, 0x0A5743, 0x452738, 0x0D264A, 0x8E933E,
                /*2091-2099*/
                0x0D5252, 0x0DAA47, 0x66B53B, 0x056D4F, 0x04AE45, 0x4A4EB9, 0x0A4D4C, 0x0D1541, 0x2D92B5
        };

        /**
         * 将公历日期转换为农历日期，且标识是否是闰月
         *
         * @param birthCal 生日对象
         * @return 返回公历日期对应的农历日期
         */
        public static Calendar solarToLunar(Calendar birthCal) {
            Calendar baseCal = new GregorianCalendar(MIN_YEAR, Calendar.JANUARY, 31);
            int offset = (int) ((birthCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 86400000L);
            // 用offset减去每农历年的天数计算当天是农历第几天
            // iYear最终结果是农历的年份, offset是当年的第几天
            int iYear, daysOfYear = 0;
            for (iYear = MIN_YEAR; iYear <= MAX_YEAR && offset > 0; iYear++) {
                daysOfYear = daysInLunarYear(iYear);
                offset -= daysOfYear;
            }
            if (offset < 0) {
                offset += daysOfYear;
                iYear--;
            }
            int leapMonth = leapMonth(iYear);
            // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
            int iMonth, daysOfMonth = 0;
            for (iMonth = 1; iMonth <= 13 && offset > 0; iMonth++) {
                daysOfMonth = daysInLunarMonth(iYear, iMonth);
                offset -= daysOfMonth;
            }
            // 当前月超过闰月，要校正
            if (leapMonth != 0 && iMonth > leapMonth) {
                --iMonth;
            }
            // offset小于0时，也要校正
            if (offset < 0) {
                offset += daysOfMonth;
                --iMonth;
            }
            // 农历年份
            Calendar lunarCalendar = Calendar.getInstance();
            lunarCalendar.set(Calendar.YEAR, iYear);
            lunarCalendar.set(Calendar.MONTH, iMonth - 1);
            lunarCalendar.set(Calendar.DAY_OF_MONTH, offset + 1);
            lunarCalendar.set(Calendar.HOUR_OF_DAY, birthCal.get(Calendar.HOUR_OF_DAY));
            return lunarCalendar;
        }

        /**
         * 传回农历 year年的总天数
         *
         * @param year 将要计算的年份
         * @return 返回传入年份的总天数
         */
        private static int daysInLunarYear(int year) {
            int i, sum = 348;
            if (leapMonth(year) != 0) {
                sum = 377;
            }
            int monthInfo = LUNAR_INFO[year - MIN_YEAR] & 0x0FFF80;
            for (i = 0x80000; i > 0x7; i >>= 1) {
                if ((monthInfo & i) != 0) {
                    sum += 1;
                }
            }
            return sum;
        }

        /**
         * 传回农历 year年month月的总天数，总共有13个月包括闰月
         *
         * @param year  将要计算的年份
         * @param month 将要计算的月份
         * @return 传回农历 year年month月的总天数
         */
        private static int daysInLunarMonth(int year, int month) {
            if ((LUNAR_INFO[year - MIN_YEAR] & (0x100000 >> month)) == 0) {
                return 29;
            } else {
                return 30;
            }
        }

        /**
         * 传回农历 year年闰哪个月 1-12 , 没闰传回 0
         *
         * @param year 将要计算的年份
         * @return 传回农历 year年闰哪个月1-12, 没闰传回 0
         */
        private static int leapMonth(int year) {
            return ((LUNAR_INFO[year - MIN_YEAR] & 0xF00000)) >> 20;
        }

        /**
         * 农历日期toString
         *
         * @param lunarCalendar 农历日期
         * @return toString
         */
        public static String lunarDateToString(Calendar lunarCalendar) {
            StringBuilder builder = new StringBuilder();
            HeavenlyStem yearHeavenlyStem = HeavenlyStem.getYearHeavenlyStem(lunarCalendar.get(Calendar.YEAR));
            Earthly yearEarthly = Earthly.getYearEarthly(lunarCalendar.get(Calendar.YEAR));
            Earthly hourEarthly = Earthly.getHourEarthly(lunarCalendar.get(Calendar.HOUR_OF_DAY));
            builder.append(yearHeavenlyStem.getName()).append(yearEarthly.getName()).append("年");
            builder.append(LUNAR_MONTH_MAPPING.get(lunarCalendar.get(Calendar.MONTH))).append("月");
            int lunarDay = lunarCalendar.get(Calendar.DAY_OF_MONTH);
            String tenBit = lunarDay > 10 ? (lunarDay >= 20 ? (lunarDay >= 30 ? "三" : "廿") : "十") : "初";
            builder.append(tenBit).append(DAY_NUM_MAPPING.get(lunarDay % 10));
            builder.append(" ").append(hourEarthly.getName()).append("时");
            return builder.toString();
        }
    }
}
