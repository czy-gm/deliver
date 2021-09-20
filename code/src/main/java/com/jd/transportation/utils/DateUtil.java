package com.jd.transportation.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 *
 * @author czy_gm
 * @version 1.0
 * @since 2021/6/5
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final DateTimeFormatter formalFtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter timeFtf = DateTimeFormatter.ofPattern("[H:m][HHmm]");

    public static final DateTimeFormatter slashFtf = DateTimeFormatter.ofPattern("y/M/d H:m");

    /**
     * 解析datetime
     *
     * @param s string
     * @return datetime
     */
    public static LocalDateTime parseDateTime(String s) {
        return LocalDateTime.parse(s, formalFtf);
    }

    /**
     * 解析时间（时分）
     *
     * @param s string
     * @return localtime
     */
    public static LocalTime parseLocalTime(String s) {
        return LocalTime.parse(s, timeFtf);
    }

    /**
     * 将时间转换为时间戳
     *
     * @param s 时间
     * @return 时间戳
     */
    public static long parseTimestamp(String s) {
        return LocalDateTime.parse(s, slashFtf).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 格式化localDateTime
     *
     * @param localDateTime localDateTime
     * @return string
     */
    public static String formatDateTime(LocalDateTime localDateTime) {
        return formalFtf.format(localDateTime);
    }

    /**
     * 解析天数和时间（时分）
     *
     * @param s string
     * @return days and localtime
     */
    public static Pair<Integer, LocalTime> parseDaysAndTime(String s) {
        String[] split = s.split("D");
        int days = Integer.parseInt(split[0].trim());
        LocalTime time = LocalTime.parse(split[1], timeFtf);
        return Pair.of(days, time);
    }

    /**
     * 字符串s是否为给定格式
     *
     * @param s      string
     * @param format 格式
     * @return 是否给定格式
     */
    public static boolean isValid(String s, DateTimeFormatter format) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        try {
            LocalDateTime.parse(s, format);
        } catch (Exception e) {
            logger.error("datetime parse error. {}", s);
            return false;
        }
        return true;
    }
}
