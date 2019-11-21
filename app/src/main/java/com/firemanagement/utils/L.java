package com.firemanagement.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class L {

    //开关
    public static final boolean DEBUG = true;

    //TAG
    public static final String TAG = "L";

    private L() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("L cannot be instantiated");
    }


    //五个等级  DIWEF

    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }

    public static void f(String text) {
        if (DEBUG) {
            Log.wtf(TAG, text);
        }
    }


    //传入自定义tag

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void f(String tag, String msg) {
        if (DEBUG)
            Log.wtf(tag, msg);
    }


}
