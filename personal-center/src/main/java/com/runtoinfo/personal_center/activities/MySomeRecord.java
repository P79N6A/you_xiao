package com.runtoinfo.personal_center.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.httpUtils.CenterEntity.LearnTrackEntity;
import com.runtoinfo.httpUtils.CenterEntity.LeaveRecordEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.CourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.adapter.CourseRecordAdapter;
import com.runtoinfo.personal_center.adapter.LearningTrackAdapter;
import com.runtoinfo.personal_center.adapter.LeaveAdapter;
import com.runtoinfo.personal_center.databinding.ContentMySomeRecordBinding;
import com.runtoinfo.personal_center.fragment.CourseFragment;
import com.runtoinfo.personal_center.fragment.NewsFragment;
import com.runtoinfo.personal_center.fragment.TopicsFragment;
import com.runtoinfo.youxiao.globalTools.adapter.CommonViewPagerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/center/mySomeRecord")
public class MySomeRecord extends BaseActivity {

    public ContentMySomeRecordBinding binding;
    public String dataType;
    public List<LearnTrackEntity> learnList = new ArrayList<>();
    public List<LeaveRecordEntity> leaveEntityList = new ArrayList<>();
    public List<CourseEntity> courseList = new ArrayList<>();
    public LearningTrackAdapter learningTrackAdapter;
    public LeaveAdapter leaveAdapter;
    public CourseRecordAdapter courseRecordAdapter;
    public LayoutInflater layoutInflater;
    public List<String> collectionTile = new ArrayList<>();
    public List<Fragment> fragments = new ArrayList<>();
    public int news = 0, courses = 0, topics = 0;
    public CommonViewPagerAdapter viewPagerAdapter;
    public RequestDataEntity requestDataEntity;
    public HttpUtils httpUtils;

    public List tempList;
    public int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(MySomeRecord.this, R.layout.content_my_some_record);
        httpUtils = new HttpUtils(getBaseContext());
        layoutInflater = LayoutInflater.from(this);
        initRequestData();
        initResult();
        initEvent();

    }

    public void initResult() {
        dataType = getIntent().getStringExtra(IntentDataType.DATA);
        switch (dataType) {
            case "courseRecord":
                binding.someRecordTitle.setText("上课记录");
                hideView(false);
                requestCourseRecord(1);
                break;
            case "leaveRecord":
                binding.someRecordTitle.setText("请假记录");
                hideView(false);
                requestLeaveRecord(1);
                break;
            case "learnTrack":
                binding.someRecordTitle.setText("学习轨迹");
                hideView(false);
                requestLearnTacks(1);
                break;
            case "collection":
                binding.someRecordTitle.setText("我的收藏");
                hideView(true);
                includeLayout();
                break;
        }
    }

    //请假
    public void initLeaveAdapter() {
        leaveAdapter = new LeaveAdapter(MySomeRecord.this, leaveEntityList, R.layout.record_leave_item_layout);
        binding.myRecordRecycler.setLinearLayout();
        binding.myRecordRecycler.setAdapter(leaveAdapter);
        binding.myRecordRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
    }
    //学习轨迹
    public void initLearnAdapter() {
        learningTrackAdapter = new LearningTrackAdapter(MySomeRecord.this, learnList, R.layout.record_study_item_layout);
        binding.myRecordRecycler.setLinearLayout();
        binding.myRecordRecycler.setAdapter(learningTrackAdapter);
        binding.myRecordRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
    }
    //上课记录
    public void initCourseAdapter() {
        courseRecordAdapter = new CourseRecordAdapter(MySomeRecord.this, courseList, R.layout.record_course_item_layout);
        binding.myRecordRecycler.setLinearLayout();
        binding.myRecordRecycler.setAdapter(courseRecordAdapter);
        binding.myRecordRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
        binding.myRecordRecycler.setOnPullLoadMoreListener(pullLoadMoreListener);
    }

    public void hideView(boolean flag) {
        binding.recordInclude.setVisibility(flag ? View.VISIBLE : View.GONE);
        binding.myRecordRecycler.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    public void initRequestData() {
        requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
        requestDataEntity.setTenantId(spUtils.getInt(Entity.TENANT_ID));
    }

    public PullLoadMoreRecyclerView.PullLoadMoreListener pullLoadMoreListener = new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {
            page = 1;
            switch (dataType) {
                case "courseRecord":
                    requestCourseRecord(page);
                    break;
                case "leaveRecord":
                    requestLeaveRecord(page);
                    break;
                case "learnTrack":
                    requestLearnTacks(page);
                    break;
            }
        }

        @Override
        public void onLoadMore() {
            page += 1;
            switch (dataType) {
                case "courseRecord":
                    requestCourseRecord(page);
                    break;
                case "leaveRecord":
                    requestLeaveRecord(page);
                    break;
                case "learnTrack":
                    requestLearnTacks(page);
                    break;
            }
        }
    };

    //学习轨迹
    public void requestLearnTacks(int page) {
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_LEARN_TACKS);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("MaxResultCount", 10);
        requestMap.put("SkipCount", DensityUtil.getOffSet(page));
        tempList = new ArrayList();
        httpUtils.getLearnTacks(handler, requestDataEntity, requestMap, tempList);
    }

    //请假记录
    public void requestLeaveRecord(int page) {
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_LEAVE_RECORD);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("UserId", spUtils.getInt(Entity.USER_ID));
        requestMap.put("MaxResultCount", 10);
        requestMap.put("SkipCount", DensityUtil.getOffSet(page));
        tempList = new ArrayList();
        httpUtils.getLeaveRecord(handler, requestDataEntity, requestMap, tempList);
    }

    //上课记录
    public void requestCourseRecord(int page) {
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_COURSE_RECORD);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("skipCount", DensityUtil.getOffSet(page));
        requestMap.put("maxResultCount", 10);
        tempList = new ArrayList();
        httpUtils.getCourseRecord(handler, requestDataEntity, requestMap, tempList);
    }

    //我的收藏
    public void includeLayout() {
        NewsFragment newsFragment = new NewsFragment(0);
        CourseFragment courseFragment = new CourseFragment(5);
        TopicsFragment topicsFragment = new TopicsFragment(1);

        fragments.add(newsFragment);
        fragments.add(topicsFragment);
        fragments.add(courseFragment);

        collectionTile.add("新闻(" + news + ")");
        collectionTile.add("专题(" + topics + ")");
        collectionTile.add("课程(" + courses + ")");


        viewPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(), fragments, collectionTile);
        newsFragment.setAdapter(viewPagerAdapter);
        topicsFragment.setAdapter(viewPagerAdapter);
        courseFragment.setAdapter(viewPagerAdapter);
        binding.personalCollectionViewPager.setAdapter(viewPagerAdapter);
        binding.personalCollectionViewPager.setOffscreenPageLimit(collectionTile.size());
        binding.personalCollectionTabLayout.setupWithViewPager(binding.personalCollectionViewPager);
    }

    public void initEvent() {
        binding.personalFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void hideNothingView(boolean flag, int drawableId, String msg) {
        binding.recordNothingLayout.setVisibility(flag ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(msg))
            binding.recordNothingDetails.setText(msg);
        if (drawableId != 0)
            binding.recordNothing.setImageResource(drawableId);
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (tempList != null){
                        binding.myRecordRecycler.setPullLoadMoreCompleted();
                        leaveEntityList.addAll(tempList);
                        if (leaveAdapter != null){
                            leaveAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    if (learnList.size() > 0) {
                        hideNothingView(false, 0, "");
                        initLearnAdapter();
                    } else {
                        hideNothingView(true, R.drawable.record_course_null, "暂时没有学习记录哦");
                    }
                    break;
                case 1:

                    if (tempList != null){
                        binding.myRecordRecycler.setPullLoadMoreCompleted();
                        learnList.addAll(tempList);
                        if (learningTrackAdapter != null){
                            learningTrackAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    if (leaveEntityList.size() > 0){
                        hideNothingView(false, 0, "");
                        initLeaveAdapter();
                    }else{
                        hideNothingView(true, R.drawable.record_leave_null, "暂时没有请假记录哦");
                    }
                    break;
                case 2:
                    if (tempList != null){
                        binding.myRecordRecycler.setPullLoadMoreCompleted();
                        courseList.addAll(tempList);
                        if (courseRecordAdapter != null){
                            courseRecordAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    if (courseList.size() > 0) {
                        hideNothingView(false, 0, "");
                        initCourseAdapter();
                    }else{
                        hideNothingView(true, R.drawable.record_course_null, "暂时没有上课记录哦");
                    }
                    break;
                case 500:
                    break;
                case 400:
                    break;
                case 404:
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        page = 1;
        tempList.clear();
    }
}
