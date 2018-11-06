package com.runtoinfo.youxiao.globalTools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by QiaoJunChao on 2018/8/23.
 */

@SuppressWarnings("all")
public class SPUtils {
    public SharedPreferences preferences;
    public Context context;
    @SuppressLint("WrongConstant")
    public SPUtils(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("YouXiao", Context.MODE_APPEND);
    }

    public void setBoolean(String key, boolean value){
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key){
        boolean result;
        try{
            result = preferences.getBoolean(key, false);
        }catch (Exception e){
            preferences.edit().remove(key);
            result = false;
        }
        return result;
    }

    public void setString(String key, String value){
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
        String result = "";
        try {
            result = preferences.getString(key,"");
        }catch (Exception e){
            preferences.edit().remove(key);
        }
        return result;
    }

    public void setInt(String key, int value){
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key){
        int result;
        try {
            result = preferences.getInt(key, 0xff);
        }catch (Exception e){
            preferences.edit().remove(key);
            result = 0xff;
        }
        return result;
    }
}
