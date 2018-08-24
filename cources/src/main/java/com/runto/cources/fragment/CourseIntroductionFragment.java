package com.runto.cources.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runto.cources.R;
import com.runto.cources.databinding.FragmentCourseIntroductionBinding;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

@SuppressLint("ValidFragment")
public class CourseIntroductionFragment extends Fragment{

    public FragmentCourseIntroductionBinding binding;
    public String msg;

    public CourseIntroductionFragment(String msg){
        this.msg = msg;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_course_introduction, container, false);
        binding.childCourseIntroduction.setText(msg);
        return binding.getRoot();
    }
}
