package com.demo.playful.toy.domain;

import com.demo.playful.toy.enums.Earthly;
import com.demo.playful.toy.enums.FiveElements;
import com.demo.playful.toy.enums.HeavenlyStem;
import lombok.Builder;
import lombok.Data;

import java.util.Calendar;
import java.util.Map;

/**
 * EightCharacters
 * 生辰八字对象
 *
 * @author zhanghanlin6
 * @date 2020/6/12
 */
@Data
public class EightCharactersDto {
    /**
     * 公历-日期
     */
    private Calendar date;
    /**
     * 农历-日期
     */
    private Calendar lunarDate;
    /**
     * 年-天干地支
     */
    private HeavenlyStemEarthlyDto yearHeavenlyStemEarthly;
    /**
     * 月-天干地支
     */
    private HeavenlyStemEarthlyDto monthHeavenlyStemEarthly;
    /**
     * 日-天干地支
     */
    private HeavenlyStemEarthlyDto dayHeavenlyStemEarthly;
    /**
     * 时-天干地支
     */
    private HeavenlyStemEarthlyDto hourHeavenlyStemEarthly;
    /**
     * 五行数量
     */
    private Map<FiveElements, Integer> fiveElementsCountMap;

    @Data
    @Builder
    public static class HeavenlyStemEarthlyDto {
        /**
         * 天干
         */
        private HeavenlyStem heavenlyStem;
        /**
         * 地支
         */
        private Earthly earthly;
    }

    /**
     * 天干地支toString
     *
     * @return 天干地支toString
     */
    public String heavenlyStemEarthlyNameToString() {
        return yearHeavenlyStemEarthly.getHeavenlyStem().getName() + yearHeavenlyStemEarthly.getEarthly().getName() + " " +
                monthHeavenlyStemEarthly.getHeavenlyStem().getName() + monthHeavenlyStemEarthly.getEarthly().getName() + " " +
                dayHeavenlyStemEarthly.getHeavenlyStem().getName() + dayHeavenlyStemEarthly.getEarthly().getName() + " " +
                hourHeavenlyStemEarthly.getHeavenlyStem().getName() + hourHeavenlyStemEarthly.getEarthly().getName();
    }


    /**
     * 天干地支toString
     *
     * @return 天干地支toString
     */
    public String heavenlyStemEarthlyCodeToString() {
        return yearHeavenlyStemEarthly.getHeavenlyStem().getCode() + "|" + yearHeavenlyStemEarthly.getEarthly().getCode() + " " +
                monthHeavenlyStemEarthly.getHeavenlyStem().getCode() + "|" + monthHeavenlyStemEarthly.getEarthly().getCode() + " " +
                dayHeavenlyStemEarthly.getHeavenlyStem().getCode() + "|" + dayHeavenlyStemEarthly.getEarthly().getCode() + " " +
                hourHeavenlyStemEarthly.getHeavenlyStem().getCode() + "|" + hourHeavenlyStemEarthly.getEarthly().getCode();
    }


    /**
     * 五行数量toString
     *
     * @return 五行数量toString
     */
    public String fiveElementsCountToString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<FiveElements, Integer> entry : fiveElementsCountMap.entrySet()) {
            builder.append(entry.getKey().getName()).append(entry.getValue()).append(" ");
        }
        return builder.toString();
    }
}
