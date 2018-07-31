package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseChildPagerAdapter;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseViewPagerAdapter;
import com.runtoinfo.youxiao.databinding.FragmentBoutiqueCourseMusicBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2018/7/26 0026.
 */

public class MusicFragment extends BaseFragment{

    public View view;
    public FragmentBoutiqueCourseMusicBinding binding;
    public BoutiqueCourseChildPagerAdapter viewPagerAdapter;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragmentList = new ArrayList<>();
    public TextView textView;
    public ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_music, container, false);
        initTalLayoutData();
        return binding.getRoot();
    }

    public void initTalLayoutData(){
        String[] title = new String[]{"钢琴","小提琴","鼓","吉他","其他","琵琶","唢呐","二胡"};
        int[] drawable = new int[]{R.drawable.boutique_music_piano, R.drawable.boutique_music_violin,
                R.drawable.boutique_music_drum, R.drawable.boutique_music_guitar,
                R.drawable.boutique_music_other, R.drawable.boutique_music_piano,
                R.drawable.boutique_music_piano, R.drawable.boutique_music_piano,};
        titles.addAll(Arrays.asList(title));

        for (int j = 0; j< title.length; j++){
            //BoutiqueCourseChildFragment fragment = new BoutiqueCourseChildFragment();
            fragmentList.add(new BoutiqueCourseChildFragment());
        }

        viewPagerAdapter = new BoutiqueCourseChildPagerAdapter(getFragmentManager(), fragmentList);
        binding.boutiqueMusicChildViewpager.setAdapter(viewPagerAdapter);
        binding.boutiqueMusicChildViewpager.setOffscreenPageLimit(title.length);
        binding.boutiqueCourseChildTablayout.setupWithViewPager(binding.boutiqueMusicChildViewpager);

        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab tab = binding.boutiqueCourseChildTablayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(R.layout.boutique_tablayout_item);
            if (i == 0){
                tab.getCustomView().findViewById(R.id.table_layout_item_textView).setSelected(true);
            }
            textView = tab.getCustomView().findViewById(R.id.table_layout_item_textView);
            imageView = tab.getCustomView().findViewById(R.id.table_layout_item_imageView);
            textView.setText(title[i]);
            //textView.setTextColor(Color.parseColor("#339ef8"));
            imageView.setImageResource(drawable[i]);
        }



        binding.boutiqueCourseChildTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.table_layout_item_textView).setSelected(true);
                binding.boutiqueMusicChildViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
