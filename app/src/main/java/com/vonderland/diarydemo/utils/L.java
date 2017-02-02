package com.vonderland.diarydemo.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class L {
    public static final boolean DEBUGGING_ENABLED = true;

    public static void v(String tag, String message) {
        if (DEBUGGING_ENABLED && !TextUtils.isEmpty(message)) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUGGING_ENABLED && !TextUtils.isEmpty(message)) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUGGING_ENABLED && !TextUtils.isEmpty(message)) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUGGING_ENABLED && !TextUtils.isEmpty(message)) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUGGING_ENABLED && !TextUtils.isEmpty(message)) {
            Log.e(tag, message);
        }
    }
}
