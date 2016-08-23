package xyz.xdzhcs.zhihudaily.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import xyz.xdzhcs.zhihudaily.R;
import xyz.xdzhcs.zhihudaily.entity.Theme;
import xyz.xdzhcs.zhihudaily.adapter.ThemePagerAdapter;
import xyz.xdzhcs.zhihudaily.app.App;
import xyz.xdzhcs.zhihudaily.db.BufferUtil;
import xyz.xdzhcs.zhihudaily.util.Api;
import xyz.xdzhcs.zhihudaily.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来嵌套PagerFragment
 */
public class ThemeFragment extends Fragment {

    private List<Theme> themes;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<PagerFragment> fragments;
    private ThemePagerAdapter adapter;
    private String tag="ThemeFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments=new ArrayList<PagerFragment>();
        themes=new ArrayList<Theme>();
    }

    public static ThemeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ThemeFragment fragment = new ThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_theme, container, false);
        initViews(view);
        initEvents();
        return view;
    }

    private void initEvents() {
        loadThemes();
    }

    private void loadThemes() {
        List<Theme> themeList=BufferUtil.loadThemesFromDB();
        if(themeList!=null&&themeList.size()!=0){
            //Logger.i("themes:"+themeList);
            for(Theme theme:themeList){
                themes.add(theme);
                fragments.add(PagerFragment.newInstance(theme));
            }

            //显示tab
            adapter=new ThemePagerAdapter(getFragmentManager(),themes,fragments);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            Logger.i("从数据库中加载主题列表");
        }else {
            //从网络上加载themes列表
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.THEMES, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //判断"others"数组是否存在
                    if (!response.isNull("others")) {
                        //如果 "other" 数组存在则开始解析
                        try {
                            JSONArray others = response.getJSONArray("others");
                            for (int i = 0; i < others.length(); i++) {
                                String id = others.getJSONObject(i).getString("id");
                                String color = others.getJSONObject(i).getString("color");
                                String thumbnail = others.getJSONObject(i).getString("thumbnail");
                                String name = others.getJSONObject(i).getString("name");
                                String description = others.getJSONObject(i).getString("description");
                                Theme theme = new Theme(color, thumbnail, description, id, name);
                                themes.add(theme);
                                fragments.add(PagerFragment.newInstance(theme));
                                //把主题保存到数据库
                                BufferUtil.saveTheme2DB(theme);
                            }
                            //显示tab
                            adapter = new ThemePagerAdapter(getFragmentManager(), themes, fragments);
                            viewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(viewPager);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logger.e("解析主题json失败");
                        }
                    } else {
                        Logger.e("解析主题json失败:不存在others数组");
                    }
                    //Logger.i("themes:"+themes);


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.e("从网络中获取主题列表失败:" + error.toString());
                }
            });
            request.setTag(tag);
            App.getQueue().add(request);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getQueue().cancelAll(tag);
    }

    /**
     * 获取相关控件的引用，初始化
     * @param view
     */
    private void initViews(View view) {
        tabLayout= (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager= (ViewPager) view.findViewById(R.id.view_pager);
    }

}
