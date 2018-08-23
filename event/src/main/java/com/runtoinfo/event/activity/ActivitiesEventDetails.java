package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivityEventDetailsBinding;
import com.runtoinfo.event.entity.EventEntity;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.utils.HttpUtils;

@Route(path = "/event/eventDetails")
public class ActivitiesEventDetails extends EventBaseActivity {

    ActivityEventDetailsBinding binding;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
    }

    @SuppressLint("SetTextI18n")
    public void initData(){
        String json = getIntent().getExtras().getString("json");
        EventEntity eventEntity = new Gson().fromJson(json, new TypeToken<EventEntity>(){}.getType());
        binding.activityEventAddress.setText("地点: " + eventEntity.getLocation());
        binding.activityEventDescription.setText(eventEntity.getContent());
        HttpUtils.postPhoto(this, eventEntity.getCover(), binding.activityEventImg);
        binding.activityEventTime.setText("时间: " + eventEntity.getStartDate().split("T")[0]);
        binding.activityEventName.setText(eventEntity.getEventName());
    }

    public void initEvent(){
        binding.activitySignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitiesEventDetails.this, ActivitiesSignUp.class));
            }
        });
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
