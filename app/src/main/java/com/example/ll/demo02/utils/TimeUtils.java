package com.example.ll.demo02.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/1.
 */
public class TimeUtils {

    public static final String FORMAT08 = "yyyyMMdd";
    public static final String FORMAT14 = "yyyyMMddHHmmss";
    public static final String FORMAT17 = "yyyyMMddHHmmssSSS";
    public static final String FORMAT19 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT10 = "yyyy-MM-dd";
    public static final String FORMAT06 = "yyyy";

    public static String formatDate(Date d, String format) {
        String rs = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
//		Calendar c = Calendar.getInstance();
//		c.setTime(d);
//		df.setCalendar(c);
        rs = df.format(d);
        return rs;
    }

    public static Date formateDateStr(String format, String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate08(Date d) {
        return formatDate(d, FORMAT08);
    }

    public static String formatDate10(Date d) {
        return formatDate(d, FORMAT10);
    }

    public static String formatDate14(Date d) {
        return formatDate(d, FORMAT14);
    }

    public static String formatDate17(Date d) {
        return formatDate(d, FORMAT17);
    }

    public static String formatDate19(Date d) {
        return formatDate(d, FORMAT19);
    }

    public static String formatDate06(Date d) {
        return formatDate(d, FORMAT06);
    }


    public static Date getAddTime(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + year);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + month);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + day);
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + hour);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minute);
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + second);
        return cal.getTime();
    }

}
