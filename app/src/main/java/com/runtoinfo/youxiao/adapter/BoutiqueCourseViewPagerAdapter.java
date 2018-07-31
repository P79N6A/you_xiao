package com.runtoinfo.youxiao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

public class BoutiqueCourseViewPagerAdapter extends FragmentPagerAdapter {

    private String [] title = {"one","two","three","four"};
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    public BoutiqueCourseViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentList, List<String> titles) {
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

}
