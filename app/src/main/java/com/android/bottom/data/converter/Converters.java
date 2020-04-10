package com.android.bottom.data.converter;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long date2Timestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
