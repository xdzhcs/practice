package org.xdkitten.zhihudaily.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.xdkitten.zhihudaily.db.DBHelper;

/**
 * 全局Application
 * Created by sanders on 2016/8/19.
 */
public class App extends Application {

    private static RequestQueue queue;
    private static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        queue= Volley.newRequestQueue(this);
        dbHelper=new DBHelper(this,"zhihu",null,1);
    }

    /**
     * 获取 Volley Request Queue
     * @return
     */
    public static RequestQueue getQueue() {
        return queue;
    }
    public static DBHelper getDbHelper(){
        return dbHelper;
    }
}
