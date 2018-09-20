package com.troy.keeper.hbz.sys;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leecheng on 2018/1/3.
 */
public class TimeMillisFormat {

    private String format;

    public TimeMillisFormat(String format) {
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
