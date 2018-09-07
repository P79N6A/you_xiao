package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivityEventDetailsBinding;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

@Route(path = "/event/eventDetails")
public class ActivitiesEventDetails extends EventBaseActivity {

    ActivityEventDetailsBinding binding;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
    }

    @SuppressLint("SetTextI18n")
    public void initData(){
        String json = getIntent().getExtras().getString(IntentDataType.DATA);
        int type = getIntent().getIntExtra(IntentDataType.TYPE, 0);
        MyEventEntity eventEntity = new Gson().fromJson(json, new TypeToken<MyEventEntity>(){}.getType());
        binding.activityEventAddress.setText("地点: " + eventEntity.getLocation());
        binding.activityEventDescription.setText(eventEntity.getContent());
        HttpUtils.postPhoto(this, eventEntity.getCover(), binding.activityEventImg);
        binding.activityEventTime.setText("时间: " + eventEntity.getStartDate().split("T")[0]);
        binding.activityEventName.setText(eventEntity.getName());
        if (type == 1){
            
        }
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
