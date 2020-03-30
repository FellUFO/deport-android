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
}
