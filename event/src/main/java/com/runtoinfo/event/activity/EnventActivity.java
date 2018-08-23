package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.event.R;
import com.runtoinfo.event.adapter.EventRecyclerAdapter;
import com.runtoinfo.event.databinding.ActivityActivitiesMainBinding;
import com.runtoinfo.event.entity.EventEntity;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.common_ui.adapter.UniversalRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/event/eventActivity")
public class EnventActivity extends EventBaseActivity{

    public ActivityActivitiesMainBinding binding;
    public List<EventEntity> dataList = new ArrayList<>();
    public EventRecyclerAdapter adapter;
    public UniversalRecyclerAdapter.OnItemClickListener onItemClickListener = new UniversalRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            EventEntity eventEntity = dataList.get(position);
            String json = new Gson().toJson(eventEntity);
            ARouter.getInstance().build("/event/eventDetails").withString("json", json).navigation();
        }
    };

    @SuppressLint("CommitPrefEdits")
    public void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities_main);
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        HttpUtils.getEventAll(mHandler, HttpEntity.MAIN_URL + HttpEntity.SCHOOL_CAMPAIGN);
    }

    @Override
    protected void initData() {
        String userId = getIntent().getExtras().getString("USER_ID", null);
        sp.edit().putString("USER_ID", userId).apply();
    }

    @Override
    protected void initEvent() {

    }

    public void initRecyclerView(){
        if (dataList.size() > 0) {
            adapter = new EventRecyclerAdapter(EnventActivity.this, dataList, R.layout.mian_activity_recycler_item);
            binding.activityEventRecycler.setLayoutManager(new LinearLayoutManager(this));
            binding.activityEventRecycler.setAdapter(adapter);

            adapter.setOnItemClickListener(onItemClickListener);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try{
                        dataList.clear();
                        JSONObject json = new JSONObject(msg.obj.toString());
                        JSONObject result = json.getJSONObject("result");
                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++){
                            EventEntity eventEntity = new EventEntity();
                            JSONObject item = items.getJSONObject(i);
                            eventEntity.setContent(item.getString("content"));
                            eventEntity.setCover(item.getString("cover"));
                            eventEntity.setEndString(item.getString("endTime"));
                            eventEntity.setStartDate(item.getString("startDate"));
                            eventEntity.setEventName(item.getString("name"));
                            eventEntity.setLocation(item.getString("location"));
                            eventEntity.setIntroduction(item.getString("introduction"));
                            eventEntity.setId(item.getInt("id"));
                            dataList.add(eventEntity);
                        }

                        initRecyclerView();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
