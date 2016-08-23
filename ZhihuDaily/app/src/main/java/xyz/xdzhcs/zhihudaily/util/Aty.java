package xyz.xdzhcs.zhihudaily.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by sanders on 2016/8/22.
 */
public class Aty {

    private Context context;
    private Class c;
    private Intent intent;
    private Bundle bundle;

    private Aty(){

    }
    public Aty(Context context, Class c){
        this.c=c;
        this.context=context;
        intent=new Intent(context,c);
        bundle=new Bundle();
    }
    public Aty withSerializable(String key, Serializable serializable){
        bundle.putSerializable(key,serializable);
        return this;
    }
    public Aty withInt(String key, int value){
        bundle.putInt(key,value);
        return this;
    }
    public Aty withString(String key, String value){
        bundle.putString(key,value);
        return this;
    }
    public void start(){
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
