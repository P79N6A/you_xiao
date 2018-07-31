package com.runtoinfo.youxiao.fragment;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.CoursePunchAdapter;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.databinding.FragmentHomeBinding;
import com.runtoinfo.youxiao.entity.CourseEntity;
import com.runtoinfo.youxiao.ui.MyScrollView;
import com.runtoinfo.youxiao.ui.PopuMenu;
import com.runtoinfo.youxiao.ui.PopupWindowFragment;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiaojunchao on 2018/5/24 0024.
 */

public class HomeFragment extends BaseFragment implements MyScrollView.ScrollViewListener{

    public FragmentHomeBinding binding;
    public CoursePunchAdapter coursePunchAdapter;
    public List<CourseEntity> list;
    public PopupWindowFragment popupWindow;
    public int mHeight = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initRecyclerData();
        initView();
        return binding.getRoot();
    }


    public void initRecyclerData(){
        binding.fragmentHomeImagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, Object>> list = new ArrayList<>();
                popupWindow = new PopupWindowFragment(getContext(), getActivity());
                for (int i = 0; i < 5; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("text", "育雅学堂");
                    map.put("image", BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.login_school_logo));
                    list.add(map);
                }

                popupWindow.showPopupWindows(list, binding.fragmentHomeImagview);

                WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
                params.alpha=0.7f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        list = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            CourseEntity courseEntity = new CourseEntity();
            courseEntity.setCourseName("布米童艺跆拳道班零基础教育");
            courseEntity.setCourseTime("05-20 17:32");
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.home_taekwondo_img);
            courseEntity.setBitmap(drawable);
            list.add(courseEntity);
        }
        binding.homeRecyclerView.setHasFixedSize(true);
        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coursePunchAdapter = new CoursePunchAdapter(getContext(), list);
        binding.homeRecyclerView.setAdapter(coursePunchAdapter);

        binding.homeRecyclerView.setNestedScrollingEnabled(false);

        binding.homeRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                //ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });
    }

    public void initView(){
        binding.homeEmailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/information/informationActivity").navigation();
            }
        });

        binding.homeCourseListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });

        binding.homeActivityListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/event/eventActivity").navigation();
            }
        });

        binding.homeSchoolMovingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/schoolDynamics").navigation();
            }
        });

        //binding.homeMyScroll.setOnScrollChangeListener(this);

        ViewTreeObserver vto = binding.homeHeadImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = binding.homeHeadImage.getHeight();
                binding.homeMyScroll.setScrollViewListener(HomeFragment.this);
            }
        });
    }



    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {  //设置标题的背景颜色
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) 0, 144,151,166));
        } else if (y > 0 && y <= mHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / mHeight;
            float alpha = (255 * scale);
            //binding.homeTitleRelative.setTextColor(Color.argb((int) alpha, 255,255,255));
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) alpha, 139,185,247));
        } else {  //滑动到banner下面设置普通颜色
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) 255, 82,151,248));
        }

    }
}
