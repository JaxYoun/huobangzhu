package com.troy.keeper.hbz.helper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日期时间处理工具类
 */
@Named("DateMapper")
public final class DateUtils {

    @Value("${staticImagePrefix}")
    private static String staticImagePrefix;

    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH = "yyyy-MM-dd HH";
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final Long ONE_DAY_MS = 86400000L;

    public static Date instantToDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        long m = instant.toEpochMilli();
        Date date = new Date();
        date.setTime(m);
        return date;
    }

    public static Date longToDate(Long time) {
        if (time == null) {
            return null;
        }
        Date date = new Date();
        date.setTime(time);
        return date;
    }

    public static Long toDateStart(Long dm) {
        if (dm == null) {
            return null;
        }
        Date d = new Date();
        d.setTime(dm);
        String x = new SimpleDateFormat(yyyy_MM_dd).format(d) + " 00:00:00";
        return defaultStringFormatToLong(x);
    }

    public static String instantToString(Instant instant, String format) {
        if (instant == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(instantToDate(instant));
    }

    public static String longToString(Long t, String format) {
        if (t == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(longToDate(t));
    }

    public static Instant stringToInstant(String data, String format) throws Exception {
        if (data == null) {
            return null;
        }
        try {
            return Instant.ofEpochMilli(new SimpleDateFormat(format).parse(data).getTime());
        } catch (ParseException e) {
            throw new Exception(e);
        }
    }

    public static Long stringToLong(String data, String format) throws Exception {
        if (data == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(data).getTime();
        } catch (ParseException e) {
            throw new Exception(e);
        }
    }

    public static Instant defaultStringFormatToInstant(String date) {
        if (date == null) {
            return null;
        }
        try {
            return stringToInstant(date, yyyy_MM_dd_HH_mm_ss);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Named("defaultStringFormatToLong")
    public static Long defaultStringFormatToLong(String date) {
        if (date == null) {
            return null;
        }
        try {
            return stringToLong(date, yyyy_MM_dd_HH_mm_ss);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String instantToDefaultString(Instant date) {
        if (date == null) {
            return null;
        }

        return instantToString(date, yyyy_MM_dd_HH_mm_ss);
    }

    @Named("longToDefaultString")
    public static String longToDefaultString(Long date) {
        if (date == null) {
            return null;
        }
        return longToString(date, yyyy_MM_dd_HH_mm_ss);
    }

    @Named("longToNoHourString")
    public static String longToNoHourString(Long date) {
        if (date == null) {
            return null;
        }
        return longToString(date, yyyy_MM_dd);
    }


    public static Long noHourStringFormatToLong(String date) {
        if (date == null) {
            return null;
        }
        try {
            return stringToLong(date, yyyy_MM_dd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转时间戳转时间字符串（精确到分钟），
     *
     * @param date
     * @return
     */
    @Named("longToNoSecondString")
    public static String longToNoSecondString(Long date) {
        if (date == null) {
            return null;
        }
        return longToString(date, yyyy_MM_dd_HH_mm);
    }

    /**
     * 时间字符串（精确到分钟）转时间戳
     *
     * @param date
     * @return
     */
    @Named("noSecondStringToLong")
    public static Long noSecondStringToLong(String date) {
        if (date == null) {
            return null;
        }
        try {
            return stringToLong(date, yyyy_MM_dd_HH_mm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 拼接日期与时间
     * @param date
     * @param time
     * @return
     */
    public static String joinDateAndTime(String date, String time) {
        if (StringUtils.isBlank(date) && StringUtils.isBlank(time)) {
            throw new IllegalArgumentException("时间和日期不能同时为空！");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date);
        stringBuilder.append(time);
        return stringBuilder.toString();
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
}
