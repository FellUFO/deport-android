package com.android.bottom.utils;


import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date getDate(String date){
        Date currDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        currDate = simpleDateFormat.parse(date,pos);
        return currDate;
    }
    public static Date getDate(String date,String format){
        Date currDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        currDate = simpleDateFormat.parse(date,pos);
        return currDate;
    }

    public static String getStringDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = simpleDateFormat.format(date);
        return format;
    }
    public static String getStringDate(Date date,String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String formatStr = simpleDateFormat.format(date);
        return formatStr;
    }

}
