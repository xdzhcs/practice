package org.xdkitten.zhihudaily.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.xdkitten.zhihudaily.app.App;
import org.xdkitten.zhihudaily.entity.NewsItem;
import org.xdkitten.zhihudaily.entity.Theme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanders on 2016/8/20.
 */
public class BufferUtil {

    //保存文章到数据库
    public static void saveArticle2DB(String id,String date,String html){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="insert into ARTICLE values(?,?,?)";
        db.execSQL(sql,new String[]{id,date,html});
    }
    //从数据库从读取文章数据
    public static String loadArticleFromDB(String id){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="select HTML from ARTICLE where id=?";
        String html=null;
        Cursor cursor= db.rawQuery(sql,new String[]{id});
        if(cursor.moveToNext()){
            html=cursor.getString(cursor.getColumnIndex("HTML"));
        }
        cursor.close();
        return html;
    }

    public static List<NewsItem> loadNewsFromDB(String date){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql = "select ID,TITLE,TYPE,IMG_URL,DATE from TIME_NEWS where date=?";
        List<NewsItem> posts = new ArrayList<NewsItem>();
        Cursor cursor  = db.rawQuery(sql,new String[]{date});
        while (cursor.moveToNext()){
            NewsItem post = new NewsItem();
            post.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            post.setId(cursor.getString(cursor.getColumnIndex("ID")));
            post.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            post.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
            List<String> images=new ArrayList<String>();
            images.add(cursor.getString(cursor.getColumnIndex("IMG_URL")));
            post.setImages(images);
            posts.add(post);
        }
        cursor.close();
        return posts;
    }

    public static List<NewsItem> loadHotNewsFromDB(String date){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql = "select ID,TITLE,TYPE,IMG_URL,DATE from HOT_NEWS where date=?";
        List<NewsItem> posts = new ArrayList<NewsItem>();
        Cursor cursor  = db.rawQuery(sql,new String[]{date});
        while (cursor.moveToNext()){
            NewsItem post = new NewsItem();
            post.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            post.setId(cursor.getString(cursor.getColumnIndex("ID")));
            post.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            post.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
            List<String> images=new ArrayList<String>();
            images.add(cursor.getString(cursor.getColumnIndex("IMG_URL")));
            post.setImages(images);
            //Log.i("info","读取:"+post.toString());
            posts.add(post);
        }
        cursor.close();
        return posts;
    }

    public static List<NewsItem> loadThemeNewsFromDB(String date,String themeId){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql = "select ID,TITLE,TYPE,IMG_URL,DATE,THEME_ID from THEME_NEWS where DATE=? and THEME_ID=?";
        List<NewsItem> posts = new ArrayList<NewsItem>();
        Cursor cursor  = db.rawQuery(sql,new String[]{date,themeId});
        while (cursor.moveToNext()){
            NewsItem post = new NewsItem();
            post.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            post.setId(cursor.getString(cursor.getColumnIndex("ID")));
            post.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            post.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
            List<String> images=new ArrayList<String>();
            images.add(cursor.getString(cursor.getColumnIndex("IMG_URL")));
            post.setImages(images);
            posts.add(post);
            //Logger.i("loadThemeNewsFromDB->THEME_ID:"+cursor.getString(cursor.getColumnIndex("TYPE"))+":"+post.toString());
        }
        cursor.close();
        return posts;
    }

    public static void saveHotNews2DB(NewsItem item){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="replace into HOT_NEWS values(?,?,?,?,?)" ;
        db.execSQL(sql,new Object[]{item.getId(),item.getTitle(),item.getType(),item.getFirstImg(),item.getDate()});
    }

    public static void saveTimeNews2DB(NewsItem post){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="replace into TIME_NEWS values(?,?,?,?,?)" ;
        db.execSQL(sql,new Object[]{post.getId(),post.getTitle(),post.getType(),post.getFirstImg(),post.getDate()});
        //Log.i("info","保存:"+post.toString());
    }
    public static void saveThemeNews2DB(NewsItem item,String themeId){
        //Logger.i("saveThemeNews2DB->themeid:"+themeId+item.toString());
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="replace into THEME_NEWS values(?,?,?,?,?,?)" ;
        db.execSQL(sql,new Object[]{item.getId(),item.getTitle(),item.getType(),item.getFirstImg(),item.getDate(),themeId});
        //Log.i("info","保存:"+item.toString());
    }
    public static void saveTheme2DB(Theme theme){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="replace into THEME values(?,?,?,?,?)" ;
        db.execSQL(sql,new Object[]{theme.getId(),theme.getColor(),theme.getThumbnail(),theme.getDescription(),theme.getName()});
        //Log.i("info","保存:"+post.toString());
    }

    public static List<Theme> loadThemesFromDB(){
        List<Theme> themes=new ArrayList<Theme>();
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="select ID,COLOR,THUMBNAIL,DESCRIPTION,NAME from THEME";
        Cursor cursor= db.rawQuery(sql,new String[]{});
        while (cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("ID"));
            String color=cursor.getString(cursor.getColumnIndex("COLOR"));
            String thumbnail=cursor.getString(cursor.getColumnIndex("THUMBNAIL"));
            String description=cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            String name=cursor.getString(cursor.getColumnIndex("NAME"));
            Theme theme = new Theme(color,thumbnail,description,id,name);
            themes.add(theme);
        }
        cursor.close();
        return themes;
    }
    public static void clearNotToday(String date){
        SQLiteDatabase db= App.getDbHelper().getWritableDatabase();
        String sql="delete from THEME_NEWS where date != ?";
        db.execSQL(sql,new Object[]{date});
        sql="delete from HOT_NEWS where date != ?";
        db.execSQL(sql,new Object[]{date});
    }
    public static void clearAllBuffer(Context context){
        clearDB(context);
        clearCathe(context);
        clearSP(context);
    }
    public static void clearDB(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
    public static void clearSP(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }
    public static void clearCathe(Context context) {
        ///data/data/.../cache
        deleteFilesByDirectory(context.getCacheDir());
    }


}
