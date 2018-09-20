package com.troy.keeper.hbz.sys;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by leecheng on 2017/6/27.
 */
public class FormatedDate {

    private final static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 字段
     */
    private Calendar relCalendar;
    private int year;
    private Instant time;
    private int month;
    private int week;
    private int dayofweek;
    private int dayofmonth;
    private int hour;
    private int minute;
    private int second;
    private int hourofday;
    private int state;

    @SneakyThrows
    public static final FormatedDate parseDate(String dates, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(dates);
            return new FormatedDate(date);
        } catch (ParseException e) {
            throw e;
        }
    }

    public static final FormatedDate parseDefaultTime(String defaultTimeFormatTime) throws ParseException {
        try {
            Date date = DEFAULT_TIME_FORMAT.parse(defaultTimeFormatTime);
            return new FormatedDate(date);
        } catch (ParseException e) {
            throw e;
        }
    }

    public static final FormatedDate parseDateDefault(String defaultFormatDate) throws ParseException {
        try {
            Date date = DEFAULT_DATE_FORMAT.parse(defaultFormatDate);
            return new FormatedDate(date);
        } catch (ParseException e) {
            throw e;
        }
    }

    public FormatedDate() {
        this(System.currentTimeMillis());
    }

    public FormatedDate(Date rel) {
        this.init(rel);
    }

    public FormatedDate(Instant date) {
        Date d = new Date();
        d.setTime(date.toEpochMilli());
        init(d);
    }

    public FormatedDate(Long timeMillis) {
        Date dt = new Date();
        dt.setTime(timeMillis);
        init(dt);
    }

    private void init(Date rel) {
        relCalendar = Calendar.getInstance();
        relCalendar.setTime(rel);
        this.time = Instant.ofEpochMilli(rel.getTime());
        year = relCalendar.get(Calendar.YEAR);
        month = (relCalendar.get(Calendar.MONTH) + 1);
        week = relCalendar.get(Calendar.WEEK_OF_MONTH);
        dayofweek = (relCalendar.get(Calendar.DAY_OF_WEEK) - 1);
        dayofmonth = relCalendar.get(Calendar.DAY_OF_MONTH);
        hour = relCalendar.get(Calendar.HOUR);
        hourofday = relCalendar.get(Calendar.HOUR_OF_DAY);
        minute = relCalendar.get(Calendar.MINUTE);
        second = relCalendar.get(Calendar.SECOND);
        state = relCalendar.get(Calendar.AM_PM);
    }

    public FormatedDate addDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(relCalendar.getTimeInMillis());
        calendar.add(Calendar.DATE, day);
        return new FormatedDate(calendar.getTimeInMillis());
    }

    public FormatedDate addMillis(int millis) {
        return new FormatedDate(relCalendar.getTimeInMillis() + millis);
    }

    public FormatedDate addMinute(int minute) {
        return new FormatedDate(relCalendar.getTimeInMillis() + minute * 60000);
    }

    public void show() {
        System.out.println("第：" + year + "年");
        System.out.println("第：" + month + "月");
        System.out.println("第：" + week + "周");
        System.out.println("周:" + dayofweek);
        System.out.println("" + dayofmonth + "日");
    }

    public Date getDate() {
        Date date = new Date();
        date.setTime(this.getTime().toEpochMilli());
        return date;
    }

    public Long getTimeMillis() {
        return this.relCalendar.getTimeInMillis();
    }

    public Calendar getCalendar() {
        return this.relCalendar;
    }

    public FormatedDate getDayFirst() {
        try {
            Date date = getDate();
            String dateStr = DEFAULT_DATE_FORMAT.format(date);
            Date newDate = DEFAULT_DATE_FORMAT.parse(dateStr);
            return new FormatedDate(newDate.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getFormat(String formate) {
        Date date = new Date();
        date.setTime(this.relCalendar.getTimeInMillis());
        try {
            return new SimpleDateFormat(formate).format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Instant getTime() {
        return time;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public int getDayofweek() {
        return dayofweek;
    }

    public int getDayofmonth() {
        return dayofmonth;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getState() {
        return state;
    }

    public int getHourofday() {
        return hourofday;
    }

    public String toString() {
        return getFormat("yyyy-MM-dd HH:mm:ss");
    }

}
