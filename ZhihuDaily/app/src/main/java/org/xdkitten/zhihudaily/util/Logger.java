package org.xdkitten.zhihudaily.util;

import android.util.Log;

/**
 * Created by sanders on 2016/8/12.
 */
public class Logger {

    public static String TAG="info";

    public static void i(String content){
        Log.i(TAG,content);
    }
    public static void e(String content){
        Log.e(TAG,content);
    }
    public static void v(String content){
        Log.v(TAG,content);
    }

}
