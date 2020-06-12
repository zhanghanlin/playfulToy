package com.demo.playful.toy;

import com.demo.playful.toy.domain.EightCharactersDto;
import com.demo.playful.toy.enums.Earthly;
import com.demo.playful.toy.enums.FiveElements;
import com.demo.playful.toy.enums.HeavenlyStem;
import com.demo.playful.toy.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * CalculationEightCharacters
 * 计算生辰八字
 *
 * @author 张涵林
 * @date 2020/6/9
 */
@Slf4j
public class CalculationEightCharacters {

    /**
     * main函数
     *
     * @param args args
     */
    public static void main(String[] args) {
        List<String> paramList = Arrays.asList("2017-04-14 11", "1990-12-26 11", "1992-01-20 11");
        for (String birth : paramList) {
            Calendar birthCal = DateUtils.parseCalendar(birth, DateUtils.Format.Y_M_D_H);
            EightCharactersDto eightCharacters = calculationEightCharacters(birthCal);
            log.info("公历生日 : {}", DateFormatUtils.format(eightCharacters.getDate(), "yyyy年MM月dd日 HH时"));
            log.info("农历生日 : {}", DateUtils.LunarDate.lunarDateToString(eightCharacters.getLunarDate()));
            log.info("生辰八字 : {}", eightCharacters.heavenlyStemEarthlyNameToString());
            log.info("八字五行 : {}", eightCharacters.fiveElementsCountToString());
            log.info("八字编码 : {}", eightCharacters.heavenlyStemEarthlyCodeToString());
            log.info("=========================================================");
        }
    }

    /**
     * 计算生辰八字
     *
     * @param birthCal 公历生日对象
     */
    private static EightCharactersDto calculationEightCharacters(Calendar birthCal) {
        Calendar lunarCal = DateUtils.LunarDate.solarToLunar(birthCal);
        // 公历年
        int year = birthCal.get(Calendar.YEAR);
        // 公历月
        int month = birthCal.get(Calendar.MONTH);
        // 公历日
        int day = birthCal.get(Calendar.DAY_OF_MONTH);
        // 小时
        int hour = birthCal.get(Calendar.HOUR_OF_DAY);
        // 农历年
        int lunarYear = lunarCal.get(Calendar.YEAR);
        // 公历月
        int lunarMonth = lunarCal.get(Calendar.MONTH);
        EightCharactersDto.HeavenlyStemEarthlyDto yearHeavenlyStemEarthly = EightCharactersDto.HeavenlyStemEarthlyDto.builder().
                heavenlyStem(HeavenlyStem.getYearHeavenlyStem(lunarYear)).
                earthly(Earthly.getYearEarthly(lunarYear)).build();
        EightCharactersDto.HeavenlyStemEarthlyDto monthHeavenlyStemEarthly = EightCharactersDto.HeavenlyStemEarthlyDto.builder().
                heavenlyStem(HeavenlyStem.getMonthHeavenlyStem(lunarMonth, yearHeavenlyStemEarthly.getHeavenlyStem())).
                earthly(Earthly.getMonthEarthly(lunarMonth)).build();
        EightCharactersDto.HeavenlyStemEarthlyDto dayHeavenlyStemEarthly = EightCharactersDto.HeavenlyStemEarthlyDto.builder().
                heavenlyStem(HeavenlyStem.getDayHeavenlyStem(year, month, day)).
                earthly(Earthly.getDayEarthly(year, month, day)).build();
        EightCharactersDto.HeavenlyStemEarthlyDto hourHeavenlyStemEarthly = EightCharactersDto.HeavenlyStemEarthlyDto.builder().
                heavenlyStem(HeavenlyStem.getHourHeavenlyStem(dayHeavenlyStemEarthly.getHeavenlyStem())).
                earthly(Earthly.getHourEarthly(hour)).build();
        List<EightCharactersDto.HeavenlyStemEarthlyDto> heavenlyStemEarthlyList =
                Lists.newArrayList(yearHeavenlyStemEarthly, monthHeavenlyStemEarthly, dayHeavenlyStemEarthly, hourHeavenlyStemEarthly);
        Map<FiveElements, Integer> fiveCountMap = Maps.newTreeMap();
        for (EightCharactersDto.HeavenlyStemEarthlyDto heavenlyStemEarthlyDto : heavenlyStemEarthlyList) {
            FiveElements fiveElementsH = FiveElements.getFiveElementsByHeavenlyStem(heavenlyStemEarthlyDto.getHeavenlyStem());
            FiveElements fiveElementsE = FiveElements.getFiveElementsByEarthly(heavenlyStemEarthlyDto.getEarthly());
            fiveCountMap.put(fiveElementsH, fiveCountMap.containsKey(fiveElementsH) ? fiveCountMap.get(fiveElementsH) + 1 : 1);
            fiveCountMap.put(fiveElementsE, fiveCountMap.containsKey(fiveElementsE) ? fiveCountMap.get(fiveElementsE) + 1 : 1);
        }
        EightCharactersDto eightCharactersDto = new EightCharactersDto();
        eightCharactersDto.setDate(birthCal);
        eightCharactersDto.setLunarDate(lunarCal);
        eightCharactersDto.setYearHeavenlyStemEarthly(yearHeavenlyStemEarthly);
        eightCharactersDto.setMonthHeavenlyStemEarthly(monthHeavenlyStemEarthly);
        eightCharactersDto.setDayHeavenlyStemEarthly(dayHeavenlyStemEarthly);
        eightCharactersDto.setHourHeavenlyStemEarthly(hourHeavenlyStemEarthly);
        eightCharactersDto.setFiveElementsCountMap(fiveCountMap);
        return eightCharactersDto;
    }
}