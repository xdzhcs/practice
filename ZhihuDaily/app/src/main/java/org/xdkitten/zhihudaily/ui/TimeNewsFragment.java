package org.xdkitten.zhihudaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import org.xdkitten.zhihudaily.util.Api;
import org.xdkitten.zhihudaily.util.Aty;
import org.xdkitten.zhihudaily.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeNewsFragment extends Fragment {

    private List<NewsItem> newsItems =new ArrayList<NewsItem>();//新闻item列表
    private NewsAdapter adapter;   //适配器
    private SwipeRefreshLayout refreshLayout;   //刷新控件，可以触发下拉刷新
    private RecyclerView recyclerView;
    private String tag="TimeNewsFragment";  //Volley 的 request 的 tag
    private int loadedNum=0;    //加载内容的天数
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化UI控件
     * @param view
     */
    private void initViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_lastest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    public static TimeNewsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TimeNewsFragment fragment = new TimeNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * 设置控件的触发事件
     */
    private void initEvents(){
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getActivity(),"触发下拉刷新",Toast.LENGTH_SHORT).show();
                loadedNum=0;
                newsItems.clear();
                loadNews();

            }
        });
        //新闻列表
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果列表已经滑动到底部,则尝试加载更多
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager manager=(LinearLayoutManager)recyclerView.getLayoutManager();
                    //获取最后一个可见的item的position
                    int lastVisibleItemPosition=manager.findLastVisibleItemPosition();
                    //获取item的count
                    int itemNum=manager.getItemCount();
                    //如果最后一个可见的item就是最后一个item
                    if(lastVisibleItemPosition==itemNum-1){
                        loadMore();//加载更多
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Toast.makeText(getActivity(),"onScrolled",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_time_news, container, false);
        initViews(view);
        initEvents();
        loadNews();
        return view;
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

    /**
     * 从网络中加载新闻列表
     * @param num num表示当前已加载的次数,当num为0表示获取今天的内容，可能是第一次加载或者刷新操作
     */
    private void loadNewsFromInternet(final int num){

        if(newsItems==null){
            newsItems=new ArrayList<NewsItem>();
        }
        //生成日期字符串
        final String date = getDateString(num);
        //Logger.i("loadNewsFromInternet-->date:"+date);

        //发起volley的json请求
        String url= (0==num)?Api.LATEST:(Api.HISTORY+date);
        //Logger.i("url:"+url);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //如果获取的数据正常，则进行解析
                            if(!response.getString("date").isEmpty()){
                                JSONArray array=response.getJSONArray("stories");
                                for(int i=0;i<array.length();i++){
                                    //每一层循环解析出一个NewsItem信息
                                    JSONArray images = array.getJSONObject(i).getJSONArray("images");
                                    String id = array.getJSONObject(i).getString("id");
                                    String type = array.getJSONObject(i).getString("type");
                                    String title = array.getJSONObject(i).getString("title");
                                    List<String> imageList = new ArrayList<String>();
                                    for (int j = 0; j < images.length(); j++){
                                        String imgUrl = images.getString(j);
                                        imageList.add(imgUrl);
                                    }
                                    //添加到list中
                                    NewsItem item=new NewsItem(id,title,imageList,type,date);
                                    if(!isExists(item)){
                                        newsItems.add(item);
                                    }
                                    //Logger.i("item:"+item.toString());
                                    //保存到数据库中
                                    BufferUtil.saveTimeNews2DB(item);
                                    //loadedNum++;

                                }
                            }
                            Logger.i("loadNewsFromInternet->newsItems.size():"+newsItems.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                        if(num==0) {
                            //如果是首次加载或者刷新
                            adapter = new NewsAdapter(getActivity(), newsItems);
                            adapter.setItemOnClickListener(new NewsAdapter.OnRecyclerViewOnClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //点击后跳到阅读Activity
                                    new Aty(getActivity(), ArticleActivity.class)
                                            .withSerializable(Constant.NEWS_ITEM, newsItems.get(position))
                                            .start();
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
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
        for(NewsItem item1:newsItems){
            if (item1.getId().equals(item.getId())){
                return true;
            }
        }
        return false;
    }
    private void loadNewsFromDB(int num){
        String date=getDateString(num);
        //Logger.i("loadNewsFromDB-->date:"+date);
        //Logger.i("newsItems.size():"+newsItems.size());
        List<NewsItem> posts = BufferUtil.loadNewsFromDB(date);
        if(newsItems==null){
            newsItems=new ArrayList<NewsItem>();
        }
        newsItems.addAll(posts);
        //停止刷新旋转
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        if(num==0){
            //如果是首次加载或者刷新
            adapter=new NewsAdapter(getActivity(), newsItems);
            adapter.setItemOnClickListener(new NewsAdapter.OnRecyclerViewOnClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //点击后跳到阅读Activity
                    new Aty(getActivity(),ArticleActivity.class)
                            .withSerializable(Constant.NEWS_ITEM,newsItems.get(position))
                            .start();
                }
            });
            recyclerView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        //loadedNum++;
        //Logger.i("loadNewsFromDB->newsItems.size():"+newsItems.size());
    }

    /**
     *
     */
    private void loadNews(){
        //Logger.i("loadNews:loadedNum"+loadedNum);
        //先尝试从数据库加载
        loadNewsFromDB(0);
        //再从网络加载
        loadNewsFromInternet(0);
        loadedNum++;
    }

    public void startActivityWithObject(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 加载更多
     */
    private void loadMore(){
       // Logger.i("loadMore:loadedNum"+loadedNum);
        //生成日期字符串
        //先尝试从数据库加载
        loadNewsFromDB(loadedNum);
        //再从网络加载
        loadNewsFromInternet(loadedNum);
        loadedNum++;
    }
}
