package xyz.xdzhcs.zhihudaily.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 工具类
 * Created by sanders on 2016/8/12.
 */
public class ToastUtil {
    public static void showLong(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
