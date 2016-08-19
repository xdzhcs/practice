package org.xdkitten.zhihudaily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sanders on 2016/7/12.
 */
public class NetworkUtil {
    /**
     * 检查网络是否可用
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        if(context!=null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info= manager.getActiveNetworkInfo();
            if(info!=null){
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 检查是否为可用的WIFI连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context){
        if(context!=null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info= manager.getActiveNetworkInfo();
            if(info!=null){
                if(info.getType()==ConnectivityManager.TYPE_WIFI){
                    return info.isAvailable();
                }
            }
        }
        return false;
    }

    /**
     * 检查是否为可用的移动数据连接
     * @param context
     * @return
     */
    public static boolean isMobileDataConnected(Context context){
        if(context!=null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info= manager.getActiveNetworkInfo();
            if(info!=null){
                if(info.getType()==ConnectivityManager.TYPE_MOBILE){
                    return info.isAvailable();
                }
            }
        }
        return false;
    }
}
