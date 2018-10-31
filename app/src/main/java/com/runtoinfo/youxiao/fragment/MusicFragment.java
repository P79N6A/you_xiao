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
import com.runtoinfo.httpUtils.bean.FineClassCourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
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
    public HttpUtils httpUtils;
    public List<FineClassCourseEntity> dataList = new ArrayList<>();

    public MusicFragment(int type){
        this.type = type;
    }
    public MusicFragment(){
        super();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_music, container, false);
        httpUtils = new HttpUtils(getContext());
        initTalLayoutView();
        return binding.getRoot();
    }

    @Override
    protected void lazyLoad() {

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
            map.put("ParentId", type);
            map.put("CategoryId", 111);

            RequestDataEntity requestDataEntity = new RequestDataEntity();
            requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_TYPE);
            requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
            requestDataEntity.setType(type);

            httpUtils.getChildType(handler,requestDataEntity, map, dataList);
        }

    }

    public void initTalLayoutData(){

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
                httpUtils.postPhoto(getActivity(), iconPath.get(i), imageView);
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

                    if (dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {
                            titles.add(dataList.get(i).getName());
                            fragmentList.add(new BoutiqueCourseChildFragment( dataList.get(i).getId()));
                            iconPath.add(dataList.get(i).getCategoryName());
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
