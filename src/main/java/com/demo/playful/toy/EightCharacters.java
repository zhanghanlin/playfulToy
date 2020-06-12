package com.demo.playful.toy;

import com.demo.playful.toy.enums.DateEnum;
import com.demo.playful.toy.enums.Earthly;
import com.demo.playful.toy.enums.FiveElements;
import com.demo.playful.toy.enums.HeavenlyStem;
import com.demo.playful.toy.utils.DateUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
     * main函数
     *
     * @param args args
     */
    public static void main(String[] args) {
        List<String> paramList = Arrays.asList("2017-04-14 11", "1990-12-26 11", "1992-01-20 11");
        for (String birth : paramList) {
            Calendar birthCal = DateUtils.parseCalendar(birth, DateUtils.Format.Y_M_D_H);
            Map<DateEnum, Map<HeavenlyStem, Earthly>> eightCharacters = calculationEightCharacters(birthCal);
            print(birthCal, eightCharacters);
        }
    }

    /**
     * 打印方法
     *
     * @param birthCal 生日
     * @param map      八字对象
     */
    private static void print(Calendar birthCal, Map<DateEnum, Map<HeavenlyStem, Earthly>> map) {
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder codeBuilder = new StringBuilder();
        Map<FiveElements, Integer> fiveCountMap = Maps.newTreeMap();
        for (Map.Entry<DateEnum, Map<HeavenlyStem, Earthly>> entry : map.entrySet()) {
            for (Map.Entry<HeavenlyStem, Earthly> heEntry : entry.getValue().entrySet()) {
                nameBuilder.append(heEntry.getKey().getName()).append(heEntry.getValue().getName());
                codeBuilder.append(heEntry.getKey().getCode()).append("|").append(heEntry.getValue().getCode());
                FiveElements fiveElementsH = FiveElements.getFiveElementsByHeavenlyStem(heEntry.getKey());
                FiveElements fiveElementsE = FiveElements.getFiveElementsByEarthly(heEntry.getValue());
                fiveCountMap.put(fiveElementsH, fiveCountMap.containsKey(fiveElementsH) ? fiveCountMap.get(fiveElementsH) + 1 : 1);
                fiveCountMap.put(fiveElementsE, fiveCountMap.containsKey(fiveElementsE) ? fiveCountMap.get(fiveElementsE) + 1 : 1);
            }
            codeBuilder.append(" ");
            nameBuilder.append(" ");
        }
        StringBuilder fiveBuilder = new StringBuilder();
        for (Map.Entry<FiveElements, Integer> fiveEntry : fiveCountMap.entrySet()) {
            fiveBuilder.append(fiveEntry.getKey().getName()).append(fiveEntry.getValue()).append(" ");
        }
        Calendar lunarCal = DateUtils.LunarDate.solarToLunar(birthCal);
        log.info("公历生日 : {}", DateFormatUtils.format(birthCal, "yyyy年MM月dd日 HH时"));
        log.info("农历生日 : {}", DateUtils.LunarDate.lunarDateToString(lunarCal));
        log.info("生辰八字 : {}", nameBuilder.toString());
        log.info("八字五行 : {}", fiveBuilder.toString());
        log.info("八字编码 : {}", codeBuilder.toString());
        log.info("=========================================================");
    }

    /**
     * 计算生辰八字
     *
     * @param birthCal 生日对象
     */
    private static Map<DateEnum, Map<HeavenlyStem, Earthly>> calculationEightCharacters(Calendar birthCal) {
        Calendar lunarCal = DateUtils.LunarDate.solarToLunar(birthCal);
        int year = birthCal.get(Calendar.YEAR);
        int month = birthCal.get(Calendar.MONTH);
        int day = birthCal.get(Calendar.DAY_OF_MONTH);
        int hour = birthCal.get(Calendar.HOUR_OF_DAY);
        int lunarYear = lunarCal.get(Calendar.YEAR);
        int lunarMonth = lunarCal.get(Calendar.MONTH);
        HeavenlyStem yearHeavenlyStem = HeavenlyStem.getYearHeavenlyStem(lunarYear);
        HeavenlyStem dayHeavenlyStem = HeavenlyStem.getDayHeavenlyStem(year, month, day);
        Map<DateEnum, Map<HeavenlyStem, Earthly>> map = Maps.newLinkedHashMap();
        map.put(DateEnum.YEAR, ImmutableMap.of(yearHeavenlyStem, Earthly.getYearEarthly(lunarYear)));
        map.put(DateEnum.MONTH, ImmutableMap.of(HeavenlyStem.getMonthHeavenlyStem(lunarMonth, yearHeavenlyStem), Earthly.getMonthEarthly(lunarMonth)));
        map.put(DateEnum.DAY, ImmutableMap.of(dayHeavenlyStem, Earthly.getDayEarthly(year, month, day)));
        map.put(DateEnum.HOUR, ImmutableMap.of(HeavenlyStem.getTimeHeavenlyStem(dayHeavenlyStem), Earthly.getHourEarthly(hour)));
        return map;
    }
}