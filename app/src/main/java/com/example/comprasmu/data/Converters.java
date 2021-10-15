package com.example.comprasmu.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

     @TypeConverter
    public static HashMap<String, String> stringToMap(String value)

    {
        //TypeToken<Map<String, String>> () {}.type
        return new Gson().fromJson(value,HashMap.class);
    }

    @TypeConverter
    public static String mapToString(HashMap<String, String> value)  {
        if(value == null)
            return "";
        else

            return new Gson().toJson(value);
    }

}
