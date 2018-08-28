package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueCourseViewPagerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.FragmentFineClassBinding;
import com.runtoinfo.youxiao.ui.SetTabLayoutWidth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class FineClassFragment extends BaseFragment {

    FragmentFineClassBinding binding;
    public BoutiqueCourseViewPagerAdapter viewPagerAdapter;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragments = new ArrayList<>();
    public View view;
    public List<Map<String, Object>> dataList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fine_class, container, false);
        initTableView();
        return binding.getRoot();
    }

    public void initTableData(){

        SetTabLayoutWidth.reflex(binding.boutiqueCourseTablayout);

//        String[] title = new String[]{"全部","音乐","美术","体育","其他","数学","英语"};
//        titles.addAll(Arrays.asList(title));
//        for (int i =0; i < titles.size(); i++){
//            fragments.add(new MusicFragment());
//        }
        viewPagerAdapter = new BoutiqueCourseViewPagerAdapter(getFragmentManager(),fragments, titles);
        binding.boutiqueCourseViewpager.setAdapter(viewPagerAdapter);
        binding.boutiqueCourseViewpager.setOffscreenPageLimit(titles.size());
        binding.boutiqueCourseTablayout.setupWithViewPager(binding.boutiqueCourseViewpager);
    }

    public void initTableView(){
        HttpUtils.getAllCourseType(handler,HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_TYPE, dataList, spUtils.getString(Entity.TOKEN));
    }

    public Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    titles.add("全部");
                    fragments.add(new MusicFragment(-1));
                    if (dataList.size() > 0){
                        for (int i = 0; i < dataList.size(); i++) {
                            titles.add(String.valueOf(dataList.get(i).get("name")));
                            fragments.add(new MusicFragment((int) dataList.get(i).get("id")));
                        }
                    } else {
                        fragments.add(new MusicFragment(-1));
                    }
                    initTableData();
                    break;
                case 404:
                    DialogMessage.showToast(getContext(), "请求失败");
                    break;
                case 500:
                    DialogMessage.showToast(getContext(), "请求失败，无法获取数据");
                    break;
            }
        }
    };
}
