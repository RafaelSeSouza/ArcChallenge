package com.arctouch.movies.data.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Converts a list of Integers to a string and vice-versa, so that it can be stored in the Room database
 */
public class IntegerListToStringConverter {

    private static Gson mGson = new Gson();

    @TypeConverter
    public static List<Integer> stringToIntegerList(String text) {
        if (text == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Integer>>() {}.getType();

        return mGson.fromJson(text, listType);
    }

    @TypeConverter
    public static String IntegerListToString(List<Integer> list) {
        return mGson.toJson(list);
    }
}
