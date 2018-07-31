package com.runto.cources.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runto.cources.R;
import com.runto.cources.databinding.ActivityBoutiqueCourseDetailsBinding;
import com.runto.cources.databinding.FragmentCourseListBinding;
import com.runto.cources.fragment.CourseIntroductionFragment;
import com.runto.cources.fragment.CourseListFragment;
import com.runtoinfo.youxiao.common_ui.adapter.CommonViewPagerAdapter;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = "/course/boutiqueCourseDetails")
public class BoutiqueCourseDetails extends FragmentActivity {

    public ActivityBoutiqueCourseDetailsBinding binding;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragmentList = new ArrayList<>();
    public CommonViewPagerAdapter viewPagerAdapter;
    public int clickCount = 0;
    public View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boutique_course_details);
        initData();
    }

    public void initData(){
        String[] title = new String[]{"介绍","目录"};
        titles.addAll(Arrays.asList(title));

        CourseIntroductionFragment introductionFragment = new CourseIntroductionFragment();
        CourseListFragment listFragment = new CourseListFragment();
        fragmentList.add(introductionFragment);
        fragmentList.add(listFragment);

        viewPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        binding.boutiqueCourseIntroductionVpager.setAdapter(viewPagerAdapter);
        binding.boutiqueCourseIntroductionTablayout.setupWithViewPager(binding.boutiqueCourseIntroductionVpager);

        binding.collectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount == 0) {
                    clickCount++;
                    binding.boutiqueCourseCollectionImage.setImageResource(R.drawable.boutique_course_collectioned);
                }else{
                    clickCount = 0;
                    binding.boutiqueCourseCollectionImage.setImageResource(R.drawable.boutique_course_collection);
                }
            }
        });

        binding.courseLeaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.boutiqueCourseDetailsVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                binding.boutiqueCourseDetailsVideoView.setVisibility(View.GONE);
                binding.boutiqueCourseDetailsImageView.setVisibility(View.VISIBLE);
            }
        });

    }
    /**
     * 视频引导页
     * @param url 视频路径
     */
    public void videoView(String url){
        binding.boutiqueCourseDetailsImageView.setVisibility(View.GONE);
        binding.boutiqueCourseDetailsVideoView.setVisibility(View.VISIBLE);

        Uri uri = Uri.parse(url);
        binding.boutiqueCourseDetailsVideoView.setVideoURI(uri);
        binding.boutiqueCourseDetailsVideoView.requestFocus();
        binding.boutiqueCourseDetailsVideoView.start();
    }

}
