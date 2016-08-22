package org.xdkitten.zhihudaily.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xdkitten.zhihudaily.R;
import org.xdkitten.zhihudaily.adapter.NewsAdapter;
import org.xdkitten.zhihudaily.app.App;
import org.xdkitten.zhihudaily.constant.Constant;
import org.xdkitten.zhihudaily.db.BufferUtil;
import org.xdkitten.zhihudaily.entity.NewsItem;
import org.xdkitten.zhihudaily.entity.Theme;
import org.xdkitten.zhihudaily.util.Api;
import org.xdkitten.zhihudaily.util.Aty;
import org.xdkitten.zhihudaily.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment extends Fragment {

    public static String THEME="theme";
    private Theme theme;
    private RecyclerView rvTheme;
    private List<NewsItem> themeNews;
    private String tag="PagerFragment";
    private NewsAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeNews=new ArrayList<NewsItem>();
        //theme= (Theme) savedInstanceState.getSerializable(THEME);
        theme= (Theme) getArguments().getSerializable(THEME);
        //loadThemeNews();
    }
    public static PagerFragment newInstance(Theme theme) {
        
        Bundle bundle = new Bundle();
        bundle.putSerializable(THEME,theme);
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_pager, container, false);
        initViews(view);
        initEvents();
        loadThemeNews();
        return view;
    }



    private void loadThemeNewsFromInternet(final int num){

        if(themeNews==null){
            themeNews=new ArrayList<NewsItem>();
        }
        //生成日期字符串
        final String date = getDateString(num);

        //发起volley的json请求
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Api.THEME + theme.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(!response.isNull("stories")){
                            //如果存在 stories 数组 ，则继续解析
                            try {
                                JSONArray stories = response.getJSONArray("stories");
                                for(int i=0;i<stories.length();i++){
                                    String title=stories.getJSONObject(i).getString("title");
                                    String type=stories.getJSONObject(i).getString("type");
                                    String id=stories.getJSONObject(i).getString("id");
                                    List<String> imageList=null;
                                    if(!stories.getJSONObject(i).isNull("images")){
                                        JSONArray images=stories.getJSONObject(i).getJSONArray("images");
                                        imageList=new ArrayList<String>();
                                        imageList.add(images.getString(0));
                                        //Logger.i("有图片");
                                    }
                                    NewsItem item=new NewsItem(id,title,imageList,type,date);
                                    //Logger.i(theme.getName()+":"+item.toString());
                                    if(!isExists(item)) {
                                        themeNews.add(item);
                                    }

                                    BufferUtil.saveThemeNews2DB(item,theme.getId());
                                    adapter=new NewsAdapter(getActivity(),themeNews);
                                    adapter.setItemOnClickListener(new NewsAdapter.OnRecyclerViewOnClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            //点击后跳到阅读Activity
                                            new Aty(getActivity(),ArticleActivity.class)
                                                    .withSerializable(Constant.NEWS_ITEM,themeNews.get(position))
                                                    .start();
                                        }
                                    });
                                    rvTheme.setAdapter(adapter);
                                    //adapter.notifyDataSetChanged();
                                    //停止刷新转动
                                    if(refreshLayout.isRefreshing()){
                                        refreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshLayout.setRefreshing(false);
                                            }
                                        });
                                    }
                                }
                                Logger.i("from net themeNews.size():"+themeNews.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Logger.e("Json解析错误");
                            }
                        }else {
                            Logger.e("Json解析错误：没有stories 数组");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(error.toString());
                    }
                });
        request.setTag(tag);
        App.getQueue().add(request);
    }

    private boolean isExists(NewsItem item){
        boolean exists=false;
        for(NewsItem item1:themeNews){
            if (item1.getId().equals(item.getId())){
                return true;
            }
        }
        return false;
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
    private void loadThemeNewsFromDB(int num){
        String date=getDateString(num);
        List<NewsItem> posts = BufferUtil.loadThemeNewsFromDB(date,theme.getId());
        if(themeNews==null){
            themeNews=new ArrayList<NewsItem>();
        }
        themeNews.addAll(posts);
        //停止刷新旋转
        if(refreshLayout.isRefreshing()){
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                }
            });
        }
        adapter=new NewsAdapter(getActivity(), themeNews);
        adapter.setItemOnClickListener(new NewsAdapter.OnRecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击后跳到阅读Activity
                new Aty(getActivity(),ArticleActivity.class)
                        .withSerializable(Constant.NEWS_ITEM,themeNews.get(position))
                        .start();
            }
        });
        rvTheme.setAdapter(adapter);
        Logger.i("loadThemeNewsFromDB--hotNews.size()"+themeNews.size());
    }


    private void loadThemeNews() {
        //从数据库中加载
        loadThemeNewsFromDB(0);
        //从网络中获取
        loadThemeNewsFromInternet(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getQueue().cancelAll(tag);
    }
    private void initEvents(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        themeNews.clear();
                        loadThemeNews();
                    }
                });
            }
        });
    }
    /**
     * 初始化相关控件
     * @param view
     */
    private void initViews(View view) {
        rvTheme= (RecyclerView) view.findViewById(R.id.rv_theme);
        rvTheme.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }
}
