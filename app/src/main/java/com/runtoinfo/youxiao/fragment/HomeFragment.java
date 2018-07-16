package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.CoursePunchAdapter;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.databinding.FragmentHomeBinding;
import com.runtoinfo.youxiao.entity.CourseEntity;
import com.runtoinfo.youxiao.ui.PopuMenu;
import com.runtoinfo.youxiao.ui.PopupWindowFragment;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaojunchao on 2018/5/24 0024.
 */

public class HomeFragment extends BaseFragment {

    //public ActivityMainBinding binding;
    public FragmentHomeBinding binding;
    public CoursePunchAdapter coursePunchAdapter;
    public List<CourseEntity> list;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.fragmentHomeImagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                PopupWindowFragment popuMenu = new PopupWindowFragment(getContext());
                list.add("北京大学");
                list.add("清华大学");
                list.add("山东大学");
                list.add("复旦大学");
                list.add("吉林大学");
                popuMenu.showPopupWindows(list, Gravity.NO_GRAVITY, 20, 160);
            }
        });
        list = new ArrayList<>();
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseName("布米童艺跆拳道班零基础教育");
        courseEntity.setCourseTime("05-20 17:32");
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.home_taekwondo_img);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.home_taekwondo_img);
        courseEntity.setBitmap(drawable);
        list.add(courseEntity);

        binding.homeRecyclerView.setHasFixedSize(true);
        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coursePunchAdapter = new CoursePunchAdapter(getContext(), list);
        binding.homeRecyclerView.setAdapter(coursePunchAdapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
