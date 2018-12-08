package com.runtoinfo.youxiao.fragment;

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
import android.view.animation.Animation;

import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.FineClassCourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseViewPagerAdapter;
import com.runtoinfo.youxiao.databinding.FragmentFineClassBinding;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.ui.SetTabLayoutWidth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

@SuppressWarnings("all")
public class FineClassFragment extends BaseFragment {

    FragmentFineClassBinding binding;
    public BoutiqueCourseViewPagerAdapter viewPagerAdapter;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragments = new ArrayList<>();
    public View view;
    public List<FineClassCourseEntity> dataList = new ArrayList<>();
    public boolean isGetData;
    private boolean mHasLoadedOnce = false;
    private boolean isPrepared = false;
    private HttpUtils httpUtils;

    public FineClassFragment(){super();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fine_class, container, false);
        DensityUtil.setViewHeight(getActivity(), binding.findClassHeadLayout, 0);
        DensityUtil.setMargin(getActivity(), binding.homeSearchRelative, DensityUtil.dip2px(getContext(), 10));
        httpUtils = new HttpUtils(getContext());
        initTableView();
        lazyLoad();
        return binding.getRoot();
    }

    public void initTableData(){

        SetTabLayoutWidth.reflex(binding.boutiqueCourseTablayout);
        viewPagerAdapter = new BoutiqueCourseViewPagerAdapter(getFragmentManager(),fragments, titles);
        binding.boutiqueCourseViewpager.setAdapter(viewPagerAdapter);
        binding.boutiqueCourseViewpager.setOffscreenPageLimit(titles.size());
        binding.boutiqueCourseTablayout.setupWithViewPager(binding.boutiqueCourseViewpager);

        binding.boutiqueCourseTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 0){
                    Entity.courseType = String.valueOf(dataList.get(tab.getPosition()).getId());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //请求大分类
    public void initTableView(){
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_TYPE);
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        httpUtils.getAllCourseType(handler,requestDataEntity, dataList);
    }

    public Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    titles.add("全部");
                    fragments.add(new MusicFragment(""));
                    if (dataList.size() > 0){
                        for (int i = 0; i < dataList.size(); i++) {
                            titles.add(String.valueOf(dataList.get(i).getName()));
                            fragments.add(new MusicFragment(String.valueOf(dataList.get(i).getId())));
                        }
                    }
                    initTableData();
                    break;
                case 404:
                    //DialogMessage.showToast(getContext(), "请求失败");
                    break;
                case 500:
                    //DialogMessage.showToast(getContext(), "请求失败，无法获取数据");
                    break;
            }
        }
    };

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //这里可以做网络请求或者需要的数据刷新操作
            //refresh(FineClassFragment.this);
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    protected void lazyLoad() {
        if (mHasLoadedOnce || !isPrepared)
            return;
        mHasLoadedOnce = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHasLoadedOnce = false;
        isPrepared = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }
}
