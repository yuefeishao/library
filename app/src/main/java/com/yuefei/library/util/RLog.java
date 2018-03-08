package com.yuefei.library.util;

import android.util.Log;

/**
 * Created by shao on 2018/3/8.
 */

public class RLog {

    public RLog() {
    }

    public static int v(String tag, String msg) {
        return Log.v("TAG", "[ " + tag + " ] " + msg);
    }

    public static int d(String tag, String msg) {
        return Log.d("TAG", "[ " + tag + " ] " + msg);
    }

    public static int i(String tag, String msg) {
        return Log.i("TAG", "[ " + tag + " ] " + msg);
    }

    public static int w(String tag, String msg) {
        return Log.w("TAG", "[ " + tag + " ] " + msg);
    }

    public static int e(String tag, String msg) {
        return Log.e("TAG", "[ " + tag + " ] " + msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.e("TAG", "[ " + tag + " ] " + msg, tr);
    }
}