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
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.CourseDataEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueInChildRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.FragmentBoutiqueCourseInChildBinding;
import com.runtoinfo.youxiao.entity.BoutiqueRecycler;
import com.runtoinfo.youxiao.ui.GridSpacesItemDecoration;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/26 0026.
 */
@SuppressWarnings("all")
@SuppressLint("ValidFragment")
public class BoutiqueCourseInChildFragment extends BaseFragment {

    public FragmentBoutiqueCourseInChildBinding binding;
    public BoutiqueInChildRecyclerAdapter adapter;
    public List<CourseDataEntity> tempList;
    public int type;
    public List<CourseDataEntity> dataList = new ArrayList<>();
    public HttpUtils httpUtils;
    public int offset = 0, page = 1;
    public RequestDataEntity requestDataEntity;
    public Map<String, Object> requestMap = new HashMap<>();

    public BoutiqueCourseInChildFragment(int type){
        this.type = type;
    }
    public BoutiqueCourseInChildFragment(){
        super();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_in_child, container, false);
        httpUtils = new HttpUtils(getContext());
        initCourseData(offset);
        return binding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    public void initCourseData(int offSet){
        requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_CHILD_ALL);

        requestMap = new HashMap<>();
        if (type != -1) {
            courseTypeEntity.setMediaType(String.valueOf(type));
            requestMap.put("CourseType", courseTypeEntity.getCourseType());
            requestMap.put("CourseSubject", courseTypeEntity.getCourseSubject());
            requestMap.put("MediaType", courseTypeEntity.getMediaType());
            requestMap.put("SkipCount", offSet);
        }
        tempList = new ArrayList<>();
        httpUtils.getInChildData(handler,requestDataEntity, requestMap, tempList);
    }
    public void initView(){

        if (tempList != null && tempList.size() > 0 ){
            dataList.addAll(tempList);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                return;
            }
        }
        binding.boutiqueInChildRecycler.setGridLayout(2);
        binding.boutiqueInChildRecycler.addItemDecoration(new GridSpacesItemDecoration(30, true));
        adapter = new BoutiqueInChildRecyclerAdapter(getActivity(), dataList);
        binding.boutiqueInChildRecycler.setAdapter(adapter);

        binding.boutiqueInChildRecycler.setNestedScrollingEnabled(false);
        binding.boutiqueInChildRecycler.setOnPullLoadMoreListener(pullLoadMoreListener);
        adapter.setOnItemClickListener(onItemClickListener);
    }

    public PullLoadMoreRecyclerView.PullLoadMoreListener pullLoadMoreListener = new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {
            initCourseData(getMaxOrMin());
        }
    };

    public BoutiqueInChildRecyclerAdapter.OnItemClickListener onItemClickListener = new BoutiqueInChildRecyclerAdapter.OnItemClickListener() {
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
    };

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    initView();
                    break;
                case 500:
                    DialogMessage.showToast(getContext(), "请求失败，检查网络连接");
                    break;
                case 404:
                    DialogMessage.showToast(getContext(), "获取数据失败");
                    break;
            }
        }
    };

    /**
     * 获取偏移量
     * @return
     */
    public int getMaxOrMin(){
        page += 1;
        return (page - 1) * 10;
    }

    @Override
    public void onPause() {
        super.onPause();
        page = 1;
        offset = 0;
    }
}
