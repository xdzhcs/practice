package org.xdkitten.zhihudaily.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.xdkitten.zhihudaily.R;
import org.xdkitten.zhihudaily.db.BufferUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    //private Switch scNightMode;
    private MaterialDialog dialog;
    private TextView tvClearCache;
    public SettingsFragment() {
        
    }


    public static SettingsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
        //
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(view);
        initEvents();
        return view;
    }

    //初始化事件
    private void initEvents() {
        //夜间模式状态改变
//        scNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//            }
//        });

        tvClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //“加载中”对话框
                dialog = new MaterialDialog.Builder(getActivity())
                        .content("清除中...")
                        .progress(true,0)
                        .build();
                dialog.show();
                //清除缓存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferUtil.clearAllBuffer(getActivity());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                }).start();

            }
        });

    }

    //初始化控件
    private void initViews(View view) {
       // scNightMode= (Switch) view.findViewById(R.id.sc_night_mode);
        //scNightMode.setChecked((Boolean) SPUtil.get(getActivity(), Constant.IS_NIGHT_MODE,false));
        tvClearCache= (TextView) view.findViewById(R.id.tv_clear_cache);
    }

}
