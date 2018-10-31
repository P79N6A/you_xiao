package com.runtoinfo.youxiao.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseViewPagerAdapter;
import com.runtoinfo.youxiao.databinding.FragmentBoutiqueCourseChildBinding;
import com.runtoinfo.youxiao.ui.SetTabLayoutWidth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2018/7/26 0026.
 */

@SuppressLint("ValidFragment")
public class BoutiqueCourseChildFragment extends BaseFragment {

    public FragmentBoutiqueCourseChildBinding binding;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragmentList = new ArrayList<>();
    public BoutiqueCourseViewPagerAdapter viewPagerAdapter;
    public int type;
    public BoutiqueCourseChildFragment(){
        super();
    }
    public BoutiqueCourseChildFragment(int type){
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_child, container, false);
        initChildData();
        return binding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    public void initChildData(){
        SetTabLayoutWidth.reflex(binding.courseChildSecondTablayout);
        courseTypeEntity.setCourseSubject(String.valueOf(type));
        String[] title = new String[]{"全部","视频","音频","曲谱"};
        titles.addAll(Arrays.asList(title));

        for (int i = 0; i < title.length; i++){
            BoutiqueCourseInChildFragment fragment = new BoutiqueCourseInChildFragment(i-1);
            fragmentList.add(fragment);
        }

        viewPagerAdapter = new BoutiqueCourseViewPagerAdapter(getFragmentManager(), fragmentList, titles);
        binding.courseChildSecondViewpager.setAdapter(viewPagerAdapter);
        binding.courseChildSecondViewpager.setOffscreenPageLimit(titles.size());
        binding.courseChildSecondTablayout.setupWithViewPager(binding.courseChildSecondViewpager);
    }
}
