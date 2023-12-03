package com.example.reclaim;

import androidx.room.TypeConverter;
import java.util.Date;

public class Converters {
    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(long time) {
        return new Date(time);
    }
}

