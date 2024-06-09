package com.example.todoandroid.domain;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriConverter {
    @TypeConverter
    public static Uri toUri(String string) {
        return Uri.parse(string);
    }

    @TypeConverter
    public static String toString(Uri uri) {
        return uri.toString();
    }
}
