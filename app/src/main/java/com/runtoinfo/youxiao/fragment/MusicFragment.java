package com.runtoinfo.youxiao.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseChildPagerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.FragmentBoutiqueCourseMusicBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/7/26 0026.
 */

@SuppressLint("ValidFragment")
public class MusicFragment extends BaseFragment{

    public View view;
    public FragmentBoutiqueCourseMusicBinding binding;
    public BoutiqueCourseChildPagerAdapter viewPagerAdapter;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragmentList = new ArrayList<>();
    public List<String> iconPath = new ArrayList<>();
    public TextView textView;
    public ImageView imageView;
    public int type;

    public MusicFragment(int type){
        this.type = type;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_music, container, false);
        initTalLayoutView();
        return binding.getRoot();
    }

    public void initTalLayoutView(){

        if (type == -1) {
            binding.boutiqueCourseChildTablayout.setVisibility(View.GONE);
            fragmentList.add(new BoutiqueCourseChildFragment(-1));
            initTalLayoutData();
        } else {
            binding.boutiqueCourseChildTablayout.setVisibility(View.VISIBLE);
            courseTypeEntity.setCourseType(String.valueOf(type));
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("token", spUtils.getString(Entity.TOKEN));
            map.put("url", HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_CHILD_TYPE);

            HttpUtils.getChildType(handler, map);
        }

    }

    public void initTalLayoutData(){
//        String[] title = new String[]{"钢琴","小提琴","鼓","吉他","其他","琵琶","唢呐","二胡"};
//        int[] drawable = new int[]{R.drawable.boutique_music_piano, R.drawable.boutique_music_violin,
//                R.drawable.boutique_music_drum, R.drawable.boutique_music_guitar,
//                R.drawable.boutique_music_other, R.drawable.boutique_music_piano,
//                R.drawable.boutique_music_piano, R.drawable.boutique_music_piano,};
//        titles.addAll(Arrays.asList(title));
//
//        for (int j = 0; j< title.length; j++){
//            //BoutiqueCourseChildFragment fragment = new BoutiqueCourseChildFragment();
//            //fragmentList.add(new BoutiqueCourseChildFragment());
//        }

        viewPagerAdapter = new BoutiqueCourseChildPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.boutiqueMusicChildViewpager.setAdapter(viewPagerAdapter);
        binding.boutiqueMusicChildViewpager.setOffscreenPageLimit(titles.size());
        binding.boutiqueCourseChildTablayout.setupWithViewPager(binding.boutiqueMusicChildViewpager);

        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = binding.boutiqueCourseChildTablayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(R.layout.boutique_tablayout_item);
            if (i == 0){
                tab.getCustomView().findViewById(R.id.table_layout_item_textView).setSelected(true);
            }
            textView = tab.getCustomView().findViewById(R.id.table_layout_item_textView);
            imageView = tab.getCustomView().findViewById(R.id.table_layout_item_imageView);
            if (titles.size() > 0 && iconPath.size() > 0) {
                textView.setText(titles.get(i));
                HttpUtils.postPhoto(getActivity(), iconPath.get(i), imageView);
            }
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

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    List<Map<String, Object>> dataList = (List<Map<String, Object>>) msg.obj;
                    if (dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {
                            titles.add(dataList.get(i).get("name").toString());
                            fragmentList.add(new BoutiqueCourseChildFragment((int) dataList.get(i).get("id")));
                            iconPath.add(dataList.get(i).get("icon").toString());
                        }
                    }else{
                        fragmentList.add(new BoutiqueCourseChildFragment(-1));
                    }
                    initTalLayoutData();
                    break;
                case 404:
                    break;
            }
        }
    };

}
