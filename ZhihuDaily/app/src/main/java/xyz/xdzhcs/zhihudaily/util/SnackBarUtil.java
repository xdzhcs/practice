package xyz.xdzhcs.zhihudaily.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Snackbar 工具类
 * Created by sanders on 2016/8/20.
 */
public class SnackBarUtil {
    /**
     * 长时间显示 SnackBar
     * @param view
     * @param msg
     */
    public static void longShow(View view,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_LONG).show();
    }

    /**
     * 短时间显示 SnackBar
     * @param view
     * @param msg
     */
    public static void shortShow(View view,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示 SnackBar,且附带Action
     * @param view
     * @param msg
     * @param actionName
     * @param listener
     */
    public static void longShowWithAction(View view, String msg, String actionName, final View.OnClickListener listener){
        Snackbar.make(view,msg,Snackbar.LENGTH_LONG).setAction(actionName, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        }).show();
    }

    /**
     * 短时间显示 SnackBar,且附带Action
     * @param view
     * @param msg
     * @param actionName
     * @param listener
     */
    public static void longShortWithAction(View view, String msg, String actionName, final View.OnClickListener listener){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).setAction(actionName, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        }).show();
    }
}
