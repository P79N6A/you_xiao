package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseViewPagerAdapter;
import com.runtoinfo.youxiao.databinding.FragmentFineClassBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class FineClassFragment extends BaseFragment {

    FragmentFineClassBinding binding;
    public BoutiqueCourseViewPagerAdapter viewPagerAdapter;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragments = new ArrayList<>();
    public View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fine_class, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fine_class, container, false);
        initTableData();
        return binding.getRoot();
    }

    public void initTableData(){
        String[] title = new String[]{"全部","音乐","美术","体育","其他","数学","英语"};
        titles.addAll(Arrays.asList(title));
        for (int i =0; i < title.length; i++){
            fragments.add(new MusicFragment());
        }
        viewPagerAdapter = new BoutiqueCourseViewPagerAdapter(getFragmentManager(),fragments, titles);
        binding.boutiqueCourseViewpager.setAdapter(viewPagerAdapter);
        binding.boutiqueCourseViewpager.setOffscreenPageLimit(titles.size());
        binding.boutiqueCourseTablayout.setupWithViewPager(binding.boutiqueCourseViewpager);
    }

    public void initTableView(){

    }
}
