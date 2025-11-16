package com.hmdp.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author tankaiwen
 */
public class DateUtils {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String YYYY_MM = "yyyyMM";

    public static Pair<String, LocalDateTime> getNowByFormat(String format) {
        LocalDateTime now = LocalDateTime.now();
        String nowStr = now.format(DateTimeFormatter.ofPattern(format));
        return Pair.of(nowStr, now);
    }
}
