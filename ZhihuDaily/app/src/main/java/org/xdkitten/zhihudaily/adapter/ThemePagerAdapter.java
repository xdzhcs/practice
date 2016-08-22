package org.xdkitten.zhihudaily.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import org.xdkitten.zhihudaily.entity.Theme;
import org.xdkitten.zhihudaily.ui.PagerFragment;

import java.util.List;

/**
 * Created by sanders on 2016/8/21.
 */
public class ThemePagerAdapter extends FragmentStatePagerAdapter {

    private List<Theme> themes;
    private List<PagerFragment> fragments;

    public ThemePagerAdapter(FragmentManager fm, List<Theme> themes, List<PagerFragment> fragments) {
        super(fm);
        this.themes=themes;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return themes.get(position).getName();
    }
}
