package com.runtoinfo.youxiao.fragment;

import android.annotation.SuppressLint;
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

/**
 * Created by Qjc on 2018/5/24 0024.
 */

public class TopicsFragment extends BaseFragment {

    public List<View> listView = new ArrayList<>();
    public ViewPager viewPager;
    public ImageView mImageView;
    public LinearLayout mLinear;
    private int mDistance;
    private ImageView mOne_dot;
    private ImageView mTwo_dot;
    private ImageView mThree_dot;
    public ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    public int currentItem;
    public FragmentTopicsBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_topics, null);
        //binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_topics);
        initViewPager(view);
        viewPages();
        setOnClickListener();
        startImageViewScroll();
        return view;
    }

    public void initViewPager(View view){
        viewPager = view.findViewById(R.id.topics_viewpage);
        mLinear = view.findViewById(R.id.topics_linear_layout);
        mImageView = view.findViewById(R.id.topics_iv_light_dots);
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
            viewPager.setCurrentItem(currentItem);
        }
    };
    public void setOnClickListener(){
        mOne_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        mTwo_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        mThree_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
    }
    /**
     * 图片左右滑动引导页
     */
    public void viewPages(){
        initData();
        viewPager.setAdapter(new ViewPageAdapter(listView));
        addDots();
        moveDots();
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    public void initData(){
        //LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < 3; i++)
        {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.topics_img_banner);
            listView.add(imageView);
        }
    }

    private void moveDots() {
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                mDistance = mLinear.getChildAt(1).getLeft() - mLinear.getChildAt(0).getLeft();
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = mDistance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mImageView.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = mDistance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mImageView.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addDots() {
        mOne_dot = new ImageView(getActivity());
        mOne_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        mLinear.addView(mOne_dot, layoutParams);
        mTwo_dot = new ImageView(getActivity());
        mTwo_dot.setImageResource(R.drawable.gray_dot);
        mLinear.addView(mTwo_dot, layoutParams);
        mThree_dot = new ImageView(getActivity());
        mThree_dot.setImageResource(R.drawable.gray_dot);
        mLinear.addView(mThree_dot, layoutParams);
        //setClickListener();
    }
}
