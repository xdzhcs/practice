package xyz.xdzhcs.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sanders on 2016/7/12.
 */
public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists TIME_NEWS(ID integer primary key,TITLE text ,TYPE text ,IMG_URL text,DATE text )");
        db.execSQL("create table if not exists ARTICLE(ID integer primary key,DATE text ,HTML text )");
        db.execSQL("create table if not exists THEME(ID integer primary key,COLOR text ,THUMBNAIL text,DESCRIPTION text,NAME text )");
        db.execSQL("create table if not exists HOT_NEWS(ID integer primary key,TITLE text ,TYPE text ,IMG_URL text,DATE text )");
        db.execSQL("create table if not exists THEME_NEWS(ID integer primary key,TITLE text ,TYPE text ,IMG_URL text,DATE text,THEME_ID text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
