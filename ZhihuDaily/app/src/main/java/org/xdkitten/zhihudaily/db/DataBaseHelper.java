package org.xdkitten.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sanders on 2016/7/12.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists LatestPosts(id integer primary key,title text not null,type integer not null,img_url text,date integer not null)");
        db.execSQL("create table if not exists Contents(id integer primary key,data integer not null,content text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
