package xyz.xdzhcs.zhihudaily.ui;


import android.os.Bundle;
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
import org.json.JSONObject;
import xyz.xdzhcs.zhihudaily.R;
import xyz.xdzhcs.zhihudaily.adapter.NewsAdapter;
import xyz.xdzhcs.zhihudaily.app.App;
import xyz.xdzhcs.zhihudaily.constant.Constant;
import xyz.xdzhcs.zhihudaily.db.BufferUtil;
import xyz.xdzhcs.zhihudaily.entity.NewsItem;
import xyz.xdzhcs.zhihudaily.util.Api;
import xyz.xdzhcs.zhihudaily.util.Aty;
import xyz.xdzhcs.zhihudaily.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HotFragment extends Fragment {

    private RecyclerView rvHot;
    private List<NewsItem> hotNews;
    private String tag="HotFragment";
    private NewsAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotNews=new ArrayList<NewsItem>();
    }

    public static HotFragment newInstance() {
        
        Bundle args = new Bundle();
        
        HotFragment fragment = new HotFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_hot, container, false);
        initViews(view);
        initEvents();
        loadHotNews();
        return view;
    }

    private void loadHotNewsFromInternet(final int num){

        if(hotNews==null){
            hotNews=new ArrayList<NewsItem>();
        }
        //生成日期字符串
        final String date = getDateString(num);

        //发起volley的json请求
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Api.HOT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("recent")) {
                                //如果存在 recent 数组 ，则继续解析
                                    JSONArray recent = response.getJSONArray("recent");
                                    for (int i = 0; i < recent.length(); i++) {
                                        String title = recent.getJSONObject(i).getString("title");
                                        String thumbnail = recent.getJSONObject(i).getString("thumbnail");
                                        String news_id = recent.getJSONObject(i).getString("news_id");
                                        List<String> images = new ArrayList<String>();
                                        images.add(thumbnail);
                                        NewsItem item = new NewsItem(news_id, title, images, null, date);
                                        if (!isExists(item)) {
                                            hotNews.add(item);
                                        }
                                        BufferUtil.saveHotNews2DB(item);
                                        adapter = new NewsAdapter(getActivity(), hotNews);
                                        adapter.setItemOnClickListener(new NewsAdapter.OnRecyclerViewOnClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                //点击后跳到阅读Activity
                                                new Aty(getActivity(), ArticleActivity.class)
                                                        .withSerializable(Constant.NEWS_ITEM, hotNews.get(position))
                                                        .start();
                                            }
                                        });
                                        rvHot.setAdapter(adapter);
                                        //停止刷新转动
                                        if (refreshLayout.isRefreshing()) {
                                            refreshLayout.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    refreshLayout.setRefreshing(false);
                                                }
                                            });
                                        }
                                    }
                                Logger.i("loadHotNewsFromInternet--hotNews.size()"+hotNews.size());
                                }else {
                                Logger.e("Json解析错误：没有stories 数组");
                            }
                        }catch (Exception e){
                            Logger.e("Json解析错误:"+e.toString());
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
        for(NewsItem item1:hotNews){
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
    private void loadHotNewsFromDB(int num){
        String date=getDateString(num);
        List<NewsItem> posts = BufferUtil.loadHotNewsFromDB(date);
        if(hotNews==null){
            hotNews=new ArrayList<NewsItem>();
        }
        hotNews.addAll(posts);
        //停止刷新旋转
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        adapter=new NewsAdapter(getActivity(), hotNews);
        adapter.setItemOnClickListener(new NewsAdapter.OnRecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击后跳到阅读Activity
                new Aty(getActivity(),ArticleActivity.class)
                        .withSerializable(Constant.NEWS_ITEM,hotNews.get(position))
                        .start();
            }
        });
        rvHot.setAdapter(adapter);
        Logger.i("loadHotNewsFromDB--hotNews.size()"+hotNews.size());
    }


    private void loadHotNews() {
        //从数据库中加载
        loadHotNewsFromDB(0);
        //从网络中获取
        loadHotNewsFromInternet(0);
    }

    private void initEvents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        hotNews.clear();
                        loadHotNews();
                    }
                });
            }
        });
    }

    private void initViews(View view) {
        rvHot= (RecyclerView) view.findViewById(R.id.rv_hot);
        rvHot.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getQueue().cancelAll(tag);
    }
}
