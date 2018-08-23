package com.runtoinfo.youxiao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import myapplication.MyApplication;

/**
 * Created by QiaoJunChao on 2018/8/9.
 */

public class SPUtils {


    public static void setBoolean(String key, boolean value){
        MyApplication.Sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key){
        return MyApplication.Sp.getBoolean(key, false);
    }

    public static void setString(String key, String value){
        MyApplication.Sp.edit().putString(key, value).apply();
    }

    public static String getString(String key){
        return MyApplication.Sp.getString(key,"null");
    }

    public static void setInt(String key, int value){
        MyApplication.Sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key){
        return MyApplication.Sp.getInt(key, 0xff);
    }

}
