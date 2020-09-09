package com.potato.datepicker.datepicker.utils;


import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
    /**
     * 年-月-日 时:分:秒
     */
    public static String DATE_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年-月-日
     */
    public static String DATE_SHORT_PATTERN = "yyyy-MM-dd";

    /**
     * 年-月-日
     */
    public static String DATE_YEAR_MONTH = "yyyy-MM";

    /**
     * 年-月-日 时:分
     */
    public static String DATE_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * 时:分
     */
    public static String DATE_HOUR_AND_MINUTE_PATTERN = "HH:mm";


    public static final long HUNDRED_YEARS = 100L * 365 * 1000 * 60 * 60 * 24L; // 100年



    /**
     * 获取时间格式化对象
     *
     * @param pattern 格式
     */
    public static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }

    /**
     * 毫秒转时间字符串
     * @param millis    毫秒
     * @param pattern   格式
     * @return
     */
    public static String millis2String(long millis, String pattern){
        return TimeUtils.millis2String(millis, getDateFormat(pattern));
    }

}
