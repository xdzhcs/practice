package org.xdkitten.zhihudaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xdkitten.zhihudaily.R;
import org.xdkitten.zhihudaily.adapter.LastestAdapter;
import org.xdkitten.zhihudaily.entity.LastestPost;
import org.xdkitten.zhihudaily.util.Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LastestFragment extends Fragment {

    //Volley请求队列
    private RequestQueue queue;

    private List<LastestPost> list=new ArrayList<LastestPost>();
    private LastestAdapter adapter;
    //UI控件
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    //加载内容的天数
    private int loadedNum=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化volley请求队列
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }
    //初始化UI控件
    private void initViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_lastest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    //设置控件的触发事件
    private void setListeners(){
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getActivity(),"触发下拉刷新",Toast.LENGTH_SHORT).show();
                list.clear();
                loadPostInfo();

            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager manager=(LinearLayoutManager)recyclerView.getLayoutManager();
                    //获取最后一个可见的item的position
                    int lastVisibleItemPosition=manager.findLastVisibleItemPosition();
                    //获取item的count
                    int itemNum=manager.getItemCount();
                    //如果最后一个可见的item就是最后一个item
                    if(lastVisibleItemPosition==itemNum-1){
                        loadMore();
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

        View view= inflater.inflate(R.layout.fragment_lastest, container, false);
        initViews(view);
        loadPostInfo();
        setListeners();
        return view;
    }
    //通过API返回的JSON信息，解析完成后存到list中
    void loadPostInfo(){
        //发起volley的json请求
         JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Api.LATEST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //如果获取的数据正常，则进行解析
                            if(!response.getString("date").isEmpty()){
                                JSONArray array=response.getJSONArray("stories");
                                for(int i=0;i<array.length();i++){
                                    //每一层循环解析出一个LastestPost信息
                                    JSONArray images = array.getJSONObject(i).getJSONArray("images");
                                    String id = array.getJSONObject(i).getString("id");
                                    Log.i("info",id);
                                    String type = array.getJSONObject(i).getString("type");
                                    String title = array.getJSONObject(i).getString("title");
                                    List<String> imageList = new ArrayList<String>();
                                    for (int j = 0; j < images.length(); j++){
                                        String imgUrl = images.getString(j);
                                        imageList.add(imgUrl);
                                    }
                                    //添加到list中
                                    LastestPost lp=new LastestPost(id,title,imageList,type);
                                    //Log.i("info",lp.toString());
                                    list.add(lp);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                        adapter=new LastestAdapter(getActivity(),list);
                        adapter.setItemOnClickListener(new LastestAdapter.OnRecyclerViewOnClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Toast.makeText(getActivity(),"你点击了:"+list.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                                Log.i("info","你点击了:"+list.get(position).getTitle());
                                startActivity(new Intent(getActivity(),ZhihuReadActivity.class));
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("info",error.toString());
                    }
                });
        queue.add(request);
        loadedNum++;
    }

    private void loadMore(){
        //生成日期字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-loadedNum);
        String timeString = sdf.format(calendar.getTime());
        Log.i("info","time:"+timeString);
        //final String date = format.format(d);
        //发起volley的json请求
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                Api.HISTORY+timeString,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //如果获取的数据正常，则进行解析
                            if(!response.getString("date").isEmpty()){
                                JSONArray array=response.getJSONArray("stories");
                                for(int i=0;i<array.length();i++){
                                    //每一层循环解析出一个LastestPost信息
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
                                    LastestPost lp=new LastestPost(id,title,imageList,type);
                                    //Log.i("info",lp.toString());
                                    list.add(lp);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                        //通知adapter更新UI
                        adapter.notifyDataSetChanged();
                        loadedNum++;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("info",error.toString());
                    }
                });
        queue.add(request);
    }
}
