package com.demo.playful.toy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DateEnum
 * 时间枚举
 *
 * @author zhanghanlin6
 * @date 2020/6/12
 */
@Getter
@AllArgsConstructor
public enum DateEnum {
    /**
     * 年
     */
    YEAR("Y", "年"),
    /**
     * 月
     */
    MONTH("M", "月"),
    /**
     * 日
     */
    DAY("D", "日"),
    /**
     * 时
     */
    HOUR("H", "时"),
    ;

    /**
     * 编码
     */
    private final String code;
    /**
     * 名称
     */
    private final String name;
}
