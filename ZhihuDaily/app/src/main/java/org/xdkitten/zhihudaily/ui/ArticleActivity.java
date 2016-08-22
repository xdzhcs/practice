package org.xdkitten.zhihudaily.ui;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.xdkitten.zhihudaily.R;
import org.xdkitten.zhihudaily.app.App;
import org.xdkitten.zhihudaily.constant.Constant;
import org.xdkitten.zhihudaily.db.BufferUtil;
import org.xdkitten.zhihudaily.entity.NewsItem;
import org.xdkitten.zhihudaily.util.Api;
import org.xdkitten.zhihudaily.util.Logger;
import org.xdkitten.zhihudaily.util.SnackBarUtil;

public class ArticleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private MaterialDialog dialog;
    private String id;
    private String shareUrl;
    private ImageView ivHead;
    private CoordinatorLayout coordLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initViews();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //“加载中”对话框
        dialog = new MaterialDialog.Builder(ArticleActivity.this)
                .content("加载中...")
                .progress(true,0)
                .build();
        dialog.show();
        //获取上也页面传来的数据
        Intent intent = getIntent();
        NewsItem item= (NewsItem) intent.getExtras().get(Constant.NEWS_ITEM);
        id = item.getId();
        final String title = item.getTitle();
        final String first_img = item.getFirstImg();
        final String date=item.getDate();


        if(first_img!=null){
            Glide.with(ArticleActivity.this).load(first_img).centerCrop().into(ivHead);
        }

        //设置标题
        toolbar.setTitle(title);
        //初始化WebView
        initWebView(webView);

        String htmlFromDb=BufferUtil.loadArticleFromDB(id);
        if(htmlFromDb==null||htmlFromDb.equals("")) {
            Log.i("info","数据库没有缓存该文章");
            ////数据库已缓存改文章，从网络中获取文章
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.NEWS + id, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        // 如果没有body，则加载share_url中内容
                        if (jsonObject.isNull("body")) {
                            webView.loadUrl(jsonObject.getString("share_url"));
                            ivHead.setImageResource(R.drawable.no_img);
                            ivHead.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } else {  // body不为null
                            shareUrl = jsonObject.getString("share_url");
                            //加载顶部图片
                            if (!jsonObject.isNull("image")) { //如果文章页中有高清图,则加载高清图
                                Glide.with(ArticleActivity.this).load(jsonObject.getString("image")).centerCrop().into(ivHead);
                            } else if (first_img != null) { //如果没有高清图，则检查上一页传来的有点模糊的图，是否存在，如果存在，则加载这个非高清图片
                                Glide.with(ArticleActivity.this).load(first_img).centerCrop().into(ivHead);
                            } else { //加载默认图片
                                ivHead.setImageResource(R.drawable.no_img);
                                ivHead.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_master.css\" type=\"text/css\">";
                            String content = jsonObject.getString("body").replace("<div class=\"img-place-holder\">", "");
                            content = content.replace("<div class=\"headline\">", "");
                            // 根据主题的不同确定不同的加载内容
                            String bodyStyle = "<body>\n";
                            String html = "<!DOCTYPE html>\n"
                                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                                    + "<head>\n"
                                    + "\t<meta charset=\"utf-8\" />"
                                    + css
                                    + "\n</head>\n"
                                    + bodyStyle
                                    + content
                                    + "</body></html>";
                            webView.loadDataWithBaseURL("x-data://base", html, "text/html", "utf-8", null);
                            //保存数据到数据库
                            BufferUtil.saveArticle2DB(id,date,html);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                //访问访问网络失败
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    SnackBarUtil.longShow(coordLayout,"从网络中加载失败");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            App.getQueue().add(request);
        }else {
            //数据库已缓存改文章，从数据库中加载文章
            Log.i("info","数据库已缓存改文章，从数据库中加载文章");
            String html=BufferUtil.loadArticleFromDB(id);
            webView.loadDataWithBaseURL("x-data://base", html, "text/html", "utf-8", null);
            //Log.i("info",html);
            dialog.dismiss();
        }
    }

    /**
     * 初始化WebView
     * @param web
     */
    private void initWebView(final WebView web){
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAppCacheEnabled(false);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                web.loadUrl(url);
                return true;
            }

        });
    }
    /**
     * 获取相关控件的引用
     */
    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("this is content page");
        webView= (WebView) findViewById(R.id.web_view);
        ivHead= (ImageView) findViewById(R.id.iv_head);
        coordLayout= (CoordinatorLayout) findViewById(R.id.coord_layout);
    }
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
            } else {
                item.setTitle("夜");
                item.setIcon(R.drawable.ic_moon);

            }
            return true;
        }else if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
