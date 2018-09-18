package com.runtoinfo.youxiao.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.TopiceHttpResultEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.TopicsArticleAdapter;
import com.runtoinfo.youxiao.adapter.ViewPageAdapter;
import com.runtoinfo.youxiao.databinding.FragmentTopicsBinding;
import com.runtoinfo.youxiao.entity.SchoolDynamicsEntity;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Qjc on 2018/5/24 0024.
 */

public class TopicsFragment extends BaseFragment {

    public List<View> listView = new ArrayList<>();
    public List<TopiceHttpResultEntity> resultList = new ArrayList<>();
    public ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    public int currentItem;
    public FragmentTopicsBinding binding;
    public ViewPageAdapter adapter;
    public TopicsArticleAdapter articleAdapter;
    public boolean isGetData;
    private boolean mHasLoadedOnce = false;
    private boolean isPrepared = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_topics, container, false);
        viewPages();
        startImageViewScroll();
        getAllArticle();
        lazyLoad();
        return binding.getRoot();
    }

    public void startImageViewScroll(){
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                currentItem = (currentItem + 1) % listView.size();
                mHandler.sendEmptyMessage(0);
            }
        }, 2, 2, TimeUnit.MINUTES);
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    binding.topicsViewpage.setCurrentItem(currentItem);
                    break;
                case 200:
                    initItemsData();
                    initEvent();
                    break;
                case 400:
                    DialogMessage.showToast(getContext(), "数据处理错误");
                    break;
            }

        }
    };

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

    public void initItemsData(){
        if (resultList.size() > 0) {
            binding.topicsRecyclerview.setHasFixedSize(true);
            binding.topicsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            articleAdapter = new TopicsArticleAdapter(getActivity(), resultList, R.layout.topics_recyclerview_items);
            binding.topicsRecyclerview.setAdapter(articleAdapter);
        }else{
            DialogMessage.showToast(getContext(), "请求数据失败");
        }
    }
    public void initEvent(){
        if (articleAdapter != null)
        articleAdapter.setOnItemClickListener(new UniversalRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TopiceHttpResultEntity topics = articleAdapter.getList().get(position);
                String coverType = topics.getCoverType();
                switch (coverType){
                    case "0":
                        SchoolDynamicsEntity schoolEntity = new SchoolDynamicsEntity();
                        schoolEntity.setPublishTime(topics.getPublishTime());
                        schoolEntity.setContent(topics.getContentpublic());
                        schoolEntity.setReadNumber(Integer.valueOf(topics.getReplyNumber()));
                        schoolEntity.setTile(topics.getTitle());
                        String data = new Gson().toJson(schoolEntity);

                        ARouter.getInstance().build("/main/schoolDynamics").withString(IntentDataType.INTENT_KEY, IntentDataType.TOPICS)
                        .withString(IntentDataType.DATA, data);
                        break;
                    case "1"://视频
                        break;
                    case "null":
                        SchoolDynamicsEntity schoolEntity1 = new SchoolDynamicsEntity();
                        schoolEntity1.setPublishTime(topics.getPublishTime());
                        schoolEntity1.setContent(topics.getContentpublic());
                        //schoolEntity1.setReadNumber(Integer.valueOf(topics.getReplyNumber()));
                        schoolEntity1.setTile(topics.getTitle());
                        schoolEntity1.setId(topics.getId());
                        String data1 = new Gson().toJson(schoolEntity1);

                        ARouter.getInstance().build("/main/schoolDynamics").withString(IntentDataType.INTENT_KEY, IntentDataType.TOPICS)
                                .withString(IntentDataType.DATA, data1).navigation();
                        break;
                }
            }
        });
    }

    /**
     * 图片左右滑动引导页
     */
    public void viewPages(){
        initData();
        adapter = new ViewPageAdapter(listView);
        binding.topicsViewpage.setAdapter(adapter);
        binding.topicsIndicator.setViewPager(binding.topicsViewpage);
    }

    public void initData(){
        listView.clear();
        for (int i = 0; i < 3; i++)
        {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(R.drawable.topics_img_banner);
            listView.add(imageView);
        }
    }

    public void getAllArticle(){
        if (resultList.size() > 0) resultList.clear();
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
        requestDataEntity.setCourseId(spUtils.getInt(Entity.COURSE_ID));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.COURSE_TOPICS);
        HttpUtils.getTopics(mHandler, requestDataEntity, resultList);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //这里可以做网络请求或者需要的数据刷新操作
            //refresh(TopicsFragment.this);
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }
}
