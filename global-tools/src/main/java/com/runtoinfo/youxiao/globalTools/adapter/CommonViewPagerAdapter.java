package com.runtoinfo.youxiao.globalTools.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class CommonViewPagerAdapter extends FragmentPagerAdapter{
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    public CommonViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    public void setPageTitle(int position, String title){
        if(position >= 0 && position < titles.size())
        {
            titles.set(position, title);
            notifyDataSetChanged();
        }
    }
}
