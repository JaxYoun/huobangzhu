package com.troy.keeper.hbz.sys;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leecheng on 2017/11/13.
 */
public class TimeMillisFormater {

    private String format;

    public TimeMillisFormater(String format) {
        this.format = format;
    }

    @SneakyThrows(ParseException.class)
    public Long parse(String dateStr) {
        if (dateStr == null) {
            return null;
        } else {
            Date date = new SimpleDateFormat(this.format).parse(dateStr);
            return date.getTime();
        }
    }

    public String format(Long timeMillis) {
        if (timeMillis == null) {
            return null;
        } else {
            Date date = new Date();
            date.setTime(timeMillis);
            return new SimpleDateFormat(this.format).format(date);
        }
    }

}
