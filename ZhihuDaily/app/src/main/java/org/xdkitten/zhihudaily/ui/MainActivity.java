package org.xdkitten.zhihudaily.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.xdkitten.zhihudaily.R;
import org.xdkitten.zhihudaily.constant.Constant;
import org.xdkitten.zhihudaily.db.BufferUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //记录上一次按下返回键的时间，用于实现双击返回退出
    private long lastPressBackTime = System.currentTimeMillis();

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private  NavigationView navigationView;
    private int curFragment= Constant.TIME_NEWS_FRAGMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initViews();

        changeFragment(new TimeNewsFragment());
    }
    void initViews(){
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);
        //弹出drawer的图标按钮
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //drawer监听器
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //实现双击返回退出
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPressBackTime < 1000) {
                //如果两次按返回键的时间小于1s，则退出
                super.onBackPressed();
            } else {
                Toast.makeText(this, "双击返回键可退出程序", Toast.LENGTH_SHORT).show();
                lastPressBackTime = currentTime;
            }

        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_day_night) {
            if (item.getTitle().equals("夜")) {
                item.setTitle("日");
                item.setIcon(R.drawable.ic_sun);
                this.setTheme(R.style.AppTheme_NoActionBar_NightTheme);
            } else {
                item.setTitle("夜");
                item.setIcon(R.drawable.ic_moon);
                this.setTheme(R.style.AppTheme_NoActionBar);
            }
            return true;
        }else if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
     */


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                toolbar.setTitle(item.getTitle());
                if(curFragment!=Constant.TIME_NEWS_FRAGMENT) {
                    changeFragment(TimeNewsFragment.newInstance());
                    curFragment=Constant.TIME_NEWS_FRAGMENT;
                }
                break;
            case R.id.nav_theme_news:
                toolbar.setTitle(item.getTitle());
                if(curFragment!=Constant.THEME_FRAGMENT) {
                    changeFragment(ThemeFragment.newInstance());
                    curFragment=Constant.THEME_FRAGMENT;
                }
                break;
            case R.id.nav_hot_news:
                toolbar.setTitle(item.getTitle());
                if(curFragment!=Constant.HOT_FRAGMENT){
                    changeFragment(HotFragment.newInstance());
                    curFragment=Constant.HOT_FRAGMENT;
                }
                break;
            case R.id.nav_setting:
                toolbar.setTitle(item.getTitle());
                changeFragment(SettingsFragment.newInstance());
                curFragment=Constant.SETTINGS;
                break;
            case R.id.nav_about:
                toolbar.setTitle(item.getTitle());
                MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                        .title("关于")
                        .neutralText("知道了")
                        .content(R.string.about)
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).build();

                dialog.show();
                //changeFragment(AboutFragment.newInstance());
                //curFragment=Constant.ABOUT;
                break;
            default:break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //更换fragment
    public void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content_layout,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BufferUtil.clearNotToday(getDateString(0));
    }
    /**
     * 获取当前日期的字符串形式
     * @param num 当前已经新闻列表已经加载的次数
     * @return
     */
    private String getDateString(int num){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-num);
        String date=sdf.format(calendar.getTime());
        return date;
    }
}
