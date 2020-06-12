package com.demo.playful.toy;

import com.alibaba.fastjson.JSONObject;
import com.demo.playful.toy.enums.DateEnum;
import com.demo.playful.toy.enums.Earthly;
import com.demo.playful.toy.enums.HeavenlyStem;
import com.demo.playful.toy.utils.DateUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.*;

/**
 * EightCharacters
 * 计算生辰八字
 *
 * @author zhanghanlin6
 * @date 2020/6/9
 */
@Slf4j
public class EightCharacters {
    /**
     * 日期格式化-生日
     */
    private static final String DATE_FORMAT_BIRTH = "yyyy-MM-dd HH";

    /**
     * main函数
     *
     * @param args args
     */
    public static void main(String[] args) throws ParseException {
        List<String> paramList = Arrays.asList("2017-04-14 11", "1990-12-26 11", "1992-01-20 11");
        for (String birth : paramList) {
            Map<DateEnum, Map<HeavenlyStem, Earthly>> eightCharacters = calculationEightCharacters(birth);
            log.info(JSONObject.toJSONString(eightCharacters));
            print(birth, eightCharacters);
        }
    }

    /**
     * 打印方法
     *
     * @param birth 生日
     * @param map   八字对象
     */
    private static void print(String birth, Map<DateEnum, Map<HeavenlyStem, Earthly>> map) throws ParseException {
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder codeBuilder = new StringBuilder();
        for (Map.Entry<DateEnum, Map<HeavenlyStem, Earthly>> entry : map.entrySet()) {
            for (Map.Entry<HeavenlyStem, Earthly> heEntry : entry.getValue().entrySet()) {
                nameBuilder.append(heEntry.getKey().getName()).append(heEntry.getValue().getName());
                codeBuilder.append(heEntry.getKey().getCode()).append("-").append(heEntry.getValue().getCode());
            }
            codeBuilder.append(" ");
            nameBuilder.append(" ");
        }
        Date birthDate = DateUtils.parseDate(birth, DATE_FORMAT_BIRTH);
        Calendar lunarCal = DateUtils.LunarDate.solarToLunar(birthDate);
        log.info("公历生日:{},农历生日:{}", DateFormatUtils.format(birthDate, "yyyy年MM月dd日HH时"), DateUtils.LunarDate.lunarDateToString(lunarCal));
        log.info("生辰八字:{}, {}", nameBuilder.toString(), codeBuilder.toString());
        log.info("=========================================================");
    }

    /**
     * 计算生辰八字
     *
     * @param birth 公历生日
     */
    public static Map<DateEnum, Map<HeavenlyStem, Earthly>> calculationEightCharacters(String birth) {
        try {
            return calculationEightCharacters(DateUtils.parseDate(birth, DATE_FORMAT_BIRTH));
        } catch (ParseException e) {
            throw new IllegalArgumentException("birth convert Exception,birth:" + birth, e);
        }
    }

    /**
     * 计算生辰八字
     *
     * @param birthDate 生日对象
     */
    private static Map<DateEnum, Map<HeavenlyStem, Earthly>> calculationEightCharacters(Date birthDate) {
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate);
        Calendar lunarCal = DateUtils.LunarDate.solarToLunar(birthDate);
        int year = birthCal.get(Calendar.YEAR);
        int month = birthCal.get(Calendar.MONTH) + 1;
        int day = birthCal.get(Calendar.DAY_OF_MONTH);
        int hour = birthCal.get(Calendar.HOUR_OF_DAY);
        int lunarYear = lunarCal.get(Calendar.YEAR);
        int lunarMonth = lunarCal.get(Calendar.MONTH) + 1;
        HeavenlyStem yearHeavenlyStem = HeavenlyStem.getYearHeavenlyStem(lunarYear);
        HeavenlyStem dayHeavenlyStem = HeavenlyStem.getDayHeavenlyStem(year, month, day);
        Map<DateEnum, Map<HeavenlyStem, Earthly>> map = Maps.newLinkedHashMap();
        map.put(DateEnum.YEAR, ImmutableMap.of(yearHeavenlyStem, Earthly.getYearEarthly(lunarYear)));
        map.put(DateEnum.MONTH, ImmutableMap.of(HeavenlyStem.getMonthHeavenlyStem(lunarMonth, yearHeavenlyStem), Earthly.getMonthEarthly(lunarMonth)));
        map.put(DateEnum.DAY, ImmutableMap.of(dayHeavenlyStem, Earthly.getDayEarthly(year, month, day)));
        map.put(DateEnum.HOUR, ImmutableMap.of(HeavenlyStem.getTimeHeavenlyStem(dayHeavenlyStem), Earthly.getTimeEarthly(hour)));
        return map;
    }
}