package com.runtoinfo.personal_center.activities;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.httpUtils.CenterEntity.CourseRecordEntity;
import com.runtoinfo.httpUtils.CenterEntity.LearnTrackEntity;
import com.runtoinfo.httpUtils.CenterEntity.LeaveRecordEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.LeaveEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.adapter.CourseRecordAdapter;
import com.runtoinfo.personal_center.adapter.LearningTrackAdapter;
import com.runtoinfo.personal_center.adapter.LeaveAdapter;
import com.runtoinfo.personal_center.databinding.ContentMySomeRecordBinding;
import com.runtoinfo.personal_center.fragment.TNFragment;
import com.runtoinfo.youxiao.globalTools.adapter.CommonViewPagerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;

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
    public List<CourseRecordEntity> courseList = new ArrayList<>();
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

    public void initResult(){
        dataType = getIntent().getStringExtra(IntentDataType.DATA);
        switch (dataType){
            case "courseRecord":
                binding.someRecordTitle.setText("上课记录");
                hideView(false);
                break;
            case "leaveRecord":
                binding.someRecordTitle.setText("请假记录");
                hideView(false);
                requestLeaveRecord();
                break;
            case "learnTrack":
                binding.someRecordTitle.setText("学习轨迹");
                hideView(false);
                requestLearnTacks();
                break;
            case "collection":
                binding.someRecordTitle.setText("我的收藏");
                hideView(true);
                includeLayout();
                break;
        }
    }

   public void initLeaveAdapter(){
        leaveAdapter = new LeaveAdapter(MySomeRecord.this, leaveEntityList, R.layout.record_leave_item_layout);
        binding.myRecordRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.myRecordRecycler.setAdapter(leaveAdapter);
        binding.myRecordRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
   }

   public void initLearnAdapter(){
       learningTrackAdapter = new LearningTrackAdapter(MySomeRecord.this, learnList, R.layout.record_study_item_layout);
       binding.myRecordRecycler.setLayoutManager(new LinearLayoutManager(this));
       binding.myRecordRecycler.setAdapter(learningTrackAdapter);
       binding.myRecordRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
   }

   public void initCourseAdapter(){
       courseRecordAdapter = new CourseRecordAdapter(MySomeRecord.this, courseList, R.layout.record_course_item_latyou);
       binding.myRecordRecycler.setLayoutManager(new LinearLayoutManager(this));
       binding.myRecordRecycler.setAdapter(courseRecordAdapter);
       binding.myRecordRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
   }

   public void hideView(boolean flag){
       binding.recordInclude.setVisibility(flag ? View.VISIBLE : View.GONE);
       binding.myRecordRecycler.setVisibility(flag ? View.GONE : View.VISIBLE);
   }

   public void initRequestData(){
       requestDataEntity = new RequestDataEntity();
       requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
       requestDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
       requestDataEntity.setTenantId(spUtils.getInt(Entity.TENANT_ID));
   }

   //学习轨迹
    public void requestLearnTacks(){
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_LEARN_TACKS);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("MaxResultCount", 10);
        requestMap.put("SkipCount", 0);

        httpUtils.getLearnTacks(handler, requestDataEntity, requestMap, learnList);
    }

    //请假记录
    public void requestLeaveRecord(){
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_LEAVE_RECORD);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("UserId", spUtils.getInt(Entity.USER_ID));
        requestMap.put("Sorting", "data desc");

        httpUtils.getLeaveRecord(handler, requestDataEntity, requestMap, leaveEntityList);
    }

   public void includeLayout(){
       fragments.add(new TNFragment(news, 0));
       fragments.add(new TNFragment(topics, 1));
       fragments.add(new TNFragment(courses, 5));

       collectionTile.add("新闻(" + news + ")");
       collectionTile.add("专题(" + topics + ")");
       collectionTile.add("课程(" + courses + ")");


       viewPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(), fragments, collectionTile);
       binding.personalCollectionViewPager.setAdapter(viewPagerAdapter);
       binding.personalCollectionViewPager.setOffscreenPageLimit(collectionTile.size());
       binding.personalCollectionTabLayout.setupWithViewPager(binding.personalCollectionViewPager);
   }

   public void initEvent(){
       binding.personalFeedbackBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });
   }

   public Handler handler = new Handler(Looper.getMainLooper()){
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                   initLearnAdapter();
                   break;
               case 1:
                   initLeaveAdapter();
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


}
