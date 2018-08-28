package com.runtoinfo.youxiao.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.bean.CourseDataEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueInChildRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.FragmentBoutiqueCourseInChildBinding;
import com.runtoinfo.youxiao.entity.BoutiqueRecycler;
import com.runtoinfo.youxiao.ui.GridSpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

@SuppressLint("ValidFragment")
public class BoutiqueCourseInChildFragment extends BaseFragment {

    public FragmentBoutiqueCourseInChildBinding binding;
    public BoutiqueInChildRecyclerAdapter adapter;
    public List<BoutiqueRecycler> recyclerList = new ArrayList<>();
    public int type;
    public List<CourseDataEntity> dataList = new ArrayList<>();
    public BoutiqueCourseInChildFragment(int type){
        this.type = type;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_in_child, container, false);
        initCourseData();
        return binding.getRoot();
    }

    public void initCourseData(){

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_CHILD_ALL);
        requestMap.put("token", spUtils.getString(Entity.TOKEN));
        if (type != -1) {
            courseTypeEntity.setMediaType(String.valueOf(type));
            requestMap.put("CourseType", courseTypeEntity.getCourseType());
            requestMap.put("CourseSubject", courseTypeEntity.getCourseSubject());
            requestMap.put("MediaType", courseTypeEntity.getMediaType());
        }
        HttpUtils.getInChildData(handler, requestMap, dataList);
    }
    public void initView(){

//        String[] title = new String[]{"钢琴培训从入门到精通","钢琴培训从入门到精通","钢琴培训从入门到精通","钢琴培训从入门到精通"};
//        String[] time = new String[]{"7月20日 10:00","7月21日 11:00","8月30日 08:00","9月1日 10:00"};
//        String[] price = new String[]{"¥450","¥450","¥450","¥450"};
//        String[] num = new String[]{"300人购买","300人购买","300人购买","300人购买"};
//        int[] drawable = new int[]{R.drawable.boutique_recycler_1, R.drawable.boutique_recycler_2, R.drawable.boutique_recycler_1, R.drawable.boutique_recycler_2};

//        for (int i = 0; i < dataList.size(); i++){
//            BoutiqueRecycler recycler = new BoutiqueRecycler();
//            CourseDataEntity dataEntity = dataList.get(i);
//            recycler.setPicPath(dataEntity.getCover());
//            recycler.setTitle(dataEntity.getName());
//            recycler.setPrice(dataEntity.getPrice());
//            recycler.setNumber(dataEntity.getPurchasedNumber());
//            recycler.setTime(dataEntity.getStartTime());
//            recyclerList.add(recycler);
//        }
        binding.boutiqueInChildRecycler.setHasFixedSize(true);
        binding.boutiqueInChildRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        binding.boutiqueInChildRecycler.addItemDecoration(new GridSpacesItemDecoration(30, true));
        adapter = new BoutiqueInChildRecyclerAdapter(getActivity(), dataList);
        binding.boutiqueInChildRecycler.setAdapter(adapter);

        binding.boutiqueInChildRecycler.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener(new BoutiqueInChildRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                CourseDataEntity entity = dataList.get(position);
                String json = new Gson().toJson(entity);
                switch (entity.getMediaType()){
                    case 0:
                    case 1:
                        ARouter.getInstance().build("/course/boutiqueCourseDetails").withString("json", json).navigation();
                        break;
                    case 2:
                        ARouter.getInstance().build("/electronic/electronicScore").withString("json", json).navigation();
                        break;
                }



            }
        });
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    initView();
                    break;
                case 404:
                    DialogMessage.showToast(getContext(), "请求失败，检查网络连接");
                    break;
                case 500:
                    DialogMessage.showToast(getContext(), "获取数据失败");
                    break;
            }
        }
    };

}
