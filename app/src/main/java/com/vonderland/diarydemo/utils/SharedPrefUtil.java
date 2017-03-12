package com.vonderland.diarydemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vonderland.diarydemo.application.DiaryDemoApplication;
import com.vonderland.diarydemo.constant.Constant;

/**
 * Created by Vonderland on 2017/3/11.
 */

public class SharedPrefUtil {
    /**
     * 保存在手机中的文件名
     */
    private final String FILE_NAME = "us_diary_data";

    private SharedPreferences sp;

    private SharedPrefUtil() {
        sp = DiaryDemoApplication.getGlobalContext()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    private static class SingletonHolder {
        private static final SharedPrefUtil INSTANCE = new SharedPrefUtil();
    }

    public static SharedPrefUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void put(String key, Object object) {
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object == null ? "" : object.toString());
        }

        editor.apply();
    }

    public Object get(String key, Object defaultObject) {

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof  Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return null;
        }
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void logOutClear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Constant.SP_KEY_TOKEN);
        editor.remove(Constant.SP_KEY_UID);
        editor.apply();
    }
}
