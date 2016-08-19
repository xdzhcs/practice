package org.xdkitten.zhihudaily.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.xdkitten.zhihudaily.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动页面，实现功能为展示启动图片3s后跳到MainActivity
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //在启动页面停留3秒以后跳到主页面
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,3*1000);

    }
}
