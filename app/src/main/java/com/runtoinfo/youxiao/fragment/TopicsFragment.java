package com.runtoinfo.youxiao.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ViewPageAdapter;
import com.runtoinfo.youxiao.databinding.FragmentTopicsBinding;
import com.runtoinfo.youxiao.ui.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import myapplication.MyApplication;

/**
 * Created by Qjc on 2018/5/24 0024.
 */

public class TopicsFragment extends BaseFragment {

    public List<View> listView = new ArrayList<>();

    public ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    public int currentItem;
    public FragmentTopicsBinding binding;
    public ViewPageAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_topics, container, false);
        viewPages();
        startImageViewScroll();
        return binding.getRoot();
    }

    public void startImageViewScroll(){
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                currentItem = (currentItem + 1) % listView.size();
                mHandler.sendEmptyMessage(0);
            }
        }, 2, 2, java.util.concurrent.TimeUnit.SECONDS);
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            binding.topicsViewpage.setCurrentItem(currentItem);
        }
    };

    /**
     * 图片左右滑动引导页
     */
    public void viewPages(){
        initData();
        adapter = new ViewPageAdapter(listView);
        binding.topicsViewpage.setAdapter(adapter);
        binding.topicsIndicator.setViewPager(binding.topicsViewpage);
    }

    public void initData(){
        listView.clear();
        for (int i = 0; i < 3; i++)
        {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(R.drawable.topics_img_banner);
            listView.add(imageView);
        }
    }
}
